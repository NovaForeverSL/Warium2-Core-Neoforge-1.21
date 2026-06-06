package net.warium;

import com.mojang.logging.LogUtils;
import net.warium.block.ModBlocks;
import net.warium.item.ModItems;
import net.warium.fluid.ModFluids;
import net.warium.block.entity.ModBlockEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.warium.registry.WariumParticles;
import net.warium.registry.WariumSoundEvents;
import org.slf4j.Logger;

@Mod(Warium.MODID)
public class Warium {
    public static final String MODID = "warium";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Warium(final IEventBus modEventBus, final ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModFluids.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        WariumParticles.register(modEventBus);
        WariumSoundEvents.SOUND_EVENTS.register(modEventBus);


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