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
            .icon(() -> ModItems.ZINC.get().getDefaultInstance())
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
            LOGGER.info("ZINC ITEM >> {}", BuiltInRegistries.ITEM.getKey(ModItems.ZINC.get()));
        }

        Config.ITEM_STRINGS.get().forEach(item -> LOGGER.info("ITEM >> {}", item));
    }

    private void addCreative(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            List.of(
                    ModItems.ZINC,
                    ModItems.RAW_ZINC,
                    ModItems.BERYLLIUM_INGOT,
                    ModItems.RAW_BERYLLIUM,
                    ModItems.LEAD_INGOT,
                    ModItems.RAW_LEAD,
                    ModItems.LITHIUM_INGOT,
                    ModItems.RAW_LITHIUM,
                    ModItems.NICKEL_INGOT,
                    ModItems.RAW_NICKEL,
                    ModItems.URANIUM_INGOT,
                    ModItems.RAW_URANIUM
            ).forEach(event::accept);
        }

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            List.of(
                    ModItems.BERYLLIUM_ORE,
                    ModItems.BERYLLIUM_BLOCK,
                    ModItems.RAW_BERYLLIUM_BLOCK,
                    ModItems.LEAD_ORE,
                    ModItems.DEEPSLATE_LEAD_ORE,
                    ModItems.LEAD_BLOCK,
                    ModItems.RAW_LEAD_BLOCK,
                    ModItems.LITHIUM_ORE,
                    ModItems.LITHIUM_BLOCK,
                    ModItems.RAW_LITHIUM_BLOCK,
                    ModItems.NICKEL_ORE,
                    ModItems.NICKEL_BLOCK,
                    ModItems.RAW_NICKEL_BLOCK,
                    ModItems.URANIUM_ORE,
                    ModItems.URANIUM_BLOCK,
                    ModItems.RAW_URANIUM_BLOCK,
                    ModItems.ZINC_ORE,
                    ModItems.ZINC_BLOCK,
                    ModItems.RAW_ZINC_BLOCK,
                    ModItems.BAUXITE,
                    ModItems.REINFORCED_CONCRETE,
                    ModItems.CRACKED_CONCRETE,
                    ModItems.DAMAGED_CONCRETE,
                    ModItems.FRACTURED_CONCRETE,
                    ModItems.DESTROYED_CONCRETE,
                    ModItems.OVERGROWN_REINFORCED_CONCRETE,
                    ModItems.REBAR
            ).forEach(event::accept);
        }
    }

    @SubscribeEvent
    public void onServerStarting(final ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}
