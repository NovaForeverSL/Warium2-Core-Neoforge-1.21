package net.warium.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.warium.block.custom.PaintableBlock;
import net.warium.data.WariumItemTags;
import net.warium.item.ModItems;

@Mixin(DyedItemColor.class)
public class DyedItemColorMixin {

    @Redirect(
        method = "applyDyes",
        at = @At(
            value = "INVOKE",
            target = """
                Lnet/minecraft/world/item/ItemStack;set(\
                Lnet/minecraft/core/component/DataComponentType;\
                Ljava/lang/Object;\
                )Ljava/lang/Object;"""
        )
    )
    private static Object warium$injectPaintable(ItemStack stack, DataComponentType<DyedItemColor> component, Object value) {
        DyedItemColor color = (DyedItemColor)value;

        if (stack.is(WariumItemTags.PAINTABLE)) {
            if (stack.is(ModItems.PAINT_GUN))
                stack.setDamageValue(0);
            
            int rgb = color.rgb();

            return stack.set(
                component,
                new DyedItemColor(
                    (PaintableBlock.stateToRGB(PaintableBlock.rgbToState(rgb >> 16 & 0xFF)) << 16) |
                    (PaintableBlock.stateToRGB(PaintableBlock.rgbToState(rgb >> 8  & 0xFF)) <<  8) |
                     PaintableBlock.stateToRGB(PaintableBlock.rgbToState(rgb       & 0xFF)),
                    color.showInTooltip()
                )
            );
        }

        return stack.set(component, color);
    }
}