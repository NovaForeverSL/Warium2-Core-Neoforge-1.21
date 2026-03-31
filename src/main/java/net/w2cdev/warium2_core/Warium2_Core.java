package net.w2cdev.warium2_core;

import com.mojang.logging.LogUtils;
import java.util.List;
import net.w2cdev.warium2_core.block.ModBlocks;
import net.w2cdev.warium2_core.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(Warium2_Core.MODID)
public class Warium2_Core {
    public static final String MODID = "warium2_core";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register(MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.warium2_core"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.INGOT_ZINC.get().getDefaultInstance())
            .displayItems((parameters, output) -> ModItems.ITEMS.getEntries().forEach(item -> output.accept(item.get())))
            .build());

    public Warium2_Core(final IEventBus modEventBus, final ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_ZINC_ITEM.getAsBoolean()) {
            LOGGER.info("ZINC ITEM >> {}", BuiltInRegistries.ITEM.getKey(ModItems.INGOT_ZINC.get()));
        }

        Config.ITEM_STRINGS.get().forEach(item -> LOGGER.info("ITEM >> {}", item));
    }

    private void addCreative(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            List.of(
                    ModItems.INGOT_ZINC,
                    ModItems.INGOT_BERYLLIUM,
                    ModItems.INGOT_NICKEL,
                    ModItems.INGOT_LEAD,
                    ModItems.RAW_ZINC,
                    ModItems.RAW_BERYLLIUM,
                    ModItems.RAW_LEAD,
                    ModItems.RAW_NICKEL,
                    ModItems.RAW_URANIUM
            ).forEach(event::accept);
        }

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            List.of(

                    ModItems.BAUXITE,
                    ModItems.ORE_BERYLLIUM,
                    ModItems.ORE_BERYLLIUM_DEEPSLATE,
                    ModItems.ORE_LEAD,
                    ModItems.ORE_LEAD_DEEPSLATE,
                    ModItems.ORE_NICKEL,
                    ModItems.ORE_NICKEL_DEEPSLATE,
                    ModItems.ORE_URANIUM,
                    ModItems.ORE_URANIUM_DEEPSLATE,
                    ModItems.ORE_ZINC,
                    ModItems.ORE_ZINC_DEEPSLATE,
                    ModItems.BLOCK_BERYLLIUM,
                    ModItems.BLOCK_LEAD,
                    ModItems.BLOCK_NICKEL,
                    ModItems.BLOCK_ZINC,
                    ModItems.RAW_BLOCK_BERYLLIUM,
                    ModItems.RAW_BLOCK_LEAD,
                    ModItems.RAW_BLOCK_NICKEL,
                    ModItems.RAW_BLOCK_URANIUM,
                    ModItems.RAW_BLOCK_ZINC,
                    ModItems.CEMENTITE_REINFORCED,
                    ModItems.CEMENTITE_CRACKED,
                    ModItems.CEMENTITE_DAMAGED,
                    ModItems.CEMENTITE_FRACTURED,
                    ModItems.CEMENTITE_DESTROYED,
                    ModItems.CEMENTITE_OVERGROWN,
                    ModItems.REBAR
            ).forEach(event::accept);
        }
    }

    @SubscribeEvent
    public void onServerStarting(final ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}

