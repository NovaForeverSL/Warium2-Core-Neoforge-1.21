package net.warium.client;

import net.warium.Warium;
import net.warium.block.ModBlocks;
import net.warium.block.entity.PaintableMetalBlockEntity;
import net.warium.item.ModItems;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = Warium.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModColorHandlers {
    private ModColorHandlers() {
    }

    @SubscribeEvent
    public static void registerBlockColors(final RegisterColorHandlersEvent.Block event) {
        event.register((state, getter, pos, tintIndex) -> {
            if (tintIndex != 0 || getter == null || pos == null) {
                return 0xFFFFFF;
            }

            final BlockEntity blockEntity = getter.getBlockEntity(pos);
            if (blockEntity instanceof PaintableMetalBlockEntity metalBlockEntity) {
                return metalBlockEntity.getColor();
            }

            return PaintableMetalBlockEntity.DEFAULT_COLOR;
        }, ModBlocks.PLATING_STEEL.get(), ModBlocks.PLATING_ALUMINUM.get());
    }

    @SubscribeEvent
    public static void registerItemColors(final RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? DyedItemColor.getOrDefault(stack, 0xFFFFFFFF) : 0xFFFFFFFF,
                ModItems.PAINT_GUN.get(), ModItems.PLATING_STEEL.get(), ModItems.PLATING_ALUMINUM.get());
    }
}
