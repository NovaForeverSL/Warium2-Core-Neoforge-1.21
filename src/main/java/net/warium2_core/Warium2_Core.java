package net.warium2_core;

import com.mojang.logging.LogUtils;
import net.warium2_core.block.ModBlocks;
import net.warium2_core.item.ModItems;
import net.warium2_core.fluid.ModFluids;
import net.warium2_core.block.entity.ModBlockEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(Warium2_Core.MODID)
public class Warium2_Core {
    public static final String MODID = "warium2_core";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Warium2_Core(final IEventBus modEventBus, final ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModFluids.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

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

    @SubscribeEvent
    public void onServerStarting(final ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }
}