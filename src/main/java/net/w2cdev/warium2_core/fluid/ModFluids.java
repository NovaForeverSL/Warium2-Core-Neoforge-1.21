package net.w2cdev.warium2_core.fluid;

import java.util.function.Supplier;
import net.w2cdev.warium2_core.Warium2_Core;
import net.w2cdev.warium2_core.block.ModBlocks;
import net.w2cdev.warium2_core.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Warium2_Core.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, Warium2_Core.MODID);

    public static final DeferredHolder<FluidType, FluidType> DIESEL_TYPE = registerFluidType("diesel");
    public static final DeferredHolder<Fluid, Fluid> DIESEL = FLUIDS.register("diesel", () -> new BaseFlowingFluid.Source(dieselProperties()));
    public static final DeferredHolder<Fluid, Fluid> FLOWING_DIESEL = FLUIDS.register("flowing_diesel", () -> new BaseFlowingFluid.Flowing(dieselProperties()));

    public static final DeferredHolder<FluidType, FluidType> KEROSENE_TYPE = registerFluidType("kerosene");
    public static final DeferredHolder<Fluid, Fluid> KEROSENE = FLUIDS.register("kerosene", () -> new BaseFlowingFluid.Source(keroseneProperties()));
    public static final DeferredHolder<Fluid, Fluid> FLOWING_KEROSENE = FLUIDS.register("flowing_kerosene", () -> new BaseFlowingFluid.Flowing(keroseneProperties()));

    public static final DeferredHolder<FluidType, FluidType> OIL_TYPE = registerFluidType("oil");
    public static final DeferredHolder<Fluid, Fluid> OIL = FLUIDS.register("oil", () -> new BaseFlowingFluid.Source(oilProperties()));
    public static final DeferredHolder<Fluid, Fluid> FLOWING_OIL = FLUIDS.register("flowing_oil", () -> new BaseFlowingFluid.Flowing(oilProperties()));

    public static final DeferredHolder<FluidType, FluidType> PETROL_TYPE = registerFluidType("petrol");
    public static final DeferredHolder<Fluid, Fluid> PETROL = FLUIDS.register("petrol", () -> new BaseFlowingFluid.Source(petrolProperties()));
    public static final DeferredHolder<Fluid, Fluid> FLOWING_PETROL = FLUIDS.register("flowing_petrol", () -> new BaseFlowingFluid.Flowing(petrolProperties()));

    public static final DeferredHolder<FluidType, FluidType> SULFURIC_ACID_TYPE = registerFluidType("sulfuric_acid");
    public static final DeferredHolder<Fluid, Fluid> SULFURIC_ACID = FLUIDS.register("sulfuric_acid", () -> new BaseFlowingFluid.Source(sulfuricAcidProperties()));
    public static final DeferredHolder<Fluid, Fluid> FLOWING_SULFURIC_ACID = FLUIDS.register("flowing_sulfuric_acid", () -> new BaseFlowingFluid.Flowing(sulfuricAcidProperties()));

    private ModFluids() {
    }

    private static DeferredHolder<FluidType, FluidType> registerFluidType(final String name) {
        return FLUID_TYPES.register(name, () -> new FluidType(FluidType.Properties.create()
                .descriptionId("fluid_type." + Warium2_Core.MODID + "." + name)
                .canConvertToSource(false)
                .canHydrate(false)
                .viscosity(1500)
                .density(1200)));
    }

    private static BaseFlowingFluid.Properties defaultFluidProperties(
            final Supplier<? extends FluidType> fluidType,
            final Supplier<? extends Fluid> source,
            final Supplier<? extends Fluid> flowing,
            final Supplier<? extends Item> bucket,
            final Supplier<? extends LiquidBlock> block
    ) {
        return new BaseFlowingFluid.Properties(fluidType, source, flowing)
                .bucket(bucket)
                .block(block)
                .tickRate(10)
                .slopeFindDistance(4)
                .levelDecreasePerBlock(1)
                .explosionResistance(100.0F);
    }

    private static BaseFlowingFluid.Properties dieselProperties() {
        return defaultFluidProperties(DIESEL_TYPE, DIESEL, FLOWING_DIESEL, ModItems.BUCKET_DIESEL, ModBlocks.DIESEL);
    }

    private static BaseFlowingFluid.Properties keroseneProperties() {
        return defaultFluidProperties(KEROSENE_TYPE, KEROSENE, FLOWING_KEROSENE, ModItems.BUCKET_KEROSENE, ModBlocks.KEROSENE);
    }

    private static BaseFlowingFluid.Properties oilProperties() {
        return defaultFluidProperties(OIL_TYPE, OIL, FLOWING_OIL, ModItems.BUCKET_OIL, ModBlocks.OIL)
                .tickRate(20)
                .slopeFindDistance(2)
                .levelDecreasePerBlock(2);
    }

    private static BaseFlowingFluid.Properties petrolProperties() {
        return defaultFluidProperties(PETROL_TYPE, PETROL, FLOWING_PETROL, ModItems.BUCKET_PETROL, ModBlocks.PETROL)
                .tickRate(8);
    }

    private static BaseFlowingFluid.Properties sulfuricAcidProperties() {
        return defaultFluidProperties(SULFURIC_ACID_TYPE, SULFURIC_ACID, FLOWING_SULFURIC_ACID, ModItems.BUCKET_SULFURIC_ACID, ModBlocks.SULFURIC_ACID)
                .tickRate(12)
                .explosionResistance(50.0F);
    }

    public static void register(final IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }

    @EventBusSubscriber(modid = Warium2_Core.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ClientExtensions {
        private ClientExtensions() {
        }

        @SubscribeEvent
        public static void registerClientExtensions(final RegisterClientExtensionsEvent event) {
            registerFluid(event, DIESEL_TYPE.get(), "diesel");
            registerFluid(event, KEROSENE_TYPE.get(), "kerosene");
            registerFluid(event, OIL_TYPE.get(), "oil");
            registerFluid(event, PETROL_TYPE.get(), "petrol");
            registerFluid(event, SULFURIC_ACID_TYPE.get(), "acid");
        }

        private static void registerFluid(final RegisterClientExtensionsEvent event, final FluidType type, final String textureStem) {
            event.registerFluidType(new IClientFluidTypeExtensions() {
                private final ResourceLocation still = ResourceLocation.fromNamespaceAndPath(Warium2_Core.MODID, "block/fluid_" + textureStem + "_still");
                private final ResourceLocation flowing = ResourceLocation.fromNamespaceAndPath(Warium2_Core.MODID, "block/fluid_" + textureStem + "_flow");

                @Override
                public ResourceLocation getStillTexture() {
                    return still;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return flowing;
                }
            }, type);
        }
    }
}
