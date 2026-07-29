package net.warium.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.warium.Warium;
import net.warium.block.ModBlocks;
import net.warium.block.custom.PaintableBlock;
import net.warium.item.ModItems;

@EventBusSubscriber(modid = Warium.MODID, value = Dist.CLIENT)
public final class ColorEvents {

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
            (state, level, pos, tintIndex) -> PaintableBlock.getColor(state) | 0xFF000000,
            ModBlocks.PLATING_ALUMINUM.get(),
            ModBlocks.PLATING_STEEL.get()
        );
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
            (stack, tintIndex) -> PaintableBlock.getColor(stack) | 0xFF000000,
            ModItems.PLATING_ALUMINUM.get(),
            ModItems.PLATING_STEEL.get()
        );
    }
}
