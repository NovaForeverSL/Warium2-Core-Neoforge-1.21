package net.warium.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.warium.block.custom.PaintableBlock;

public class PaintGun extends Item {

    public PaintGun() {
        super(new Properties().stacksTo(1).durability(100));
    }

    @Override
    public int getDamage(ItemStack stack) {
        return stack.has(DataComponents.DYED_COLOR) ? super.getDamage(stack) : 0;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.has(DataComponents.DYED_COLOR);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return PaintableBlock.getColor(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();

        if (player != null) {
            ItemStack stack = context.getItemInHand();
            
            if (stack.has(DataComponents.DYED_COLOR)) {
                Level level = context.getLevel();
                BlockPos pos = context.getClickedPos();
                BlockState state = level.getBlockState(pos);

                if (state.getBlock() instanceof PaintableBlock) {
                    int color = PaintableBlock.getColor(stack);
                    int blockColor = PaintableBlock.getColor(state);
                    
                    if (color != blockColor) {
                        level.playSound(player, pos, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS, 1, 1);

                        if (!level.isClientSide) {
                            level.setBlock(pos, PaintableBlock.setColor(state, color), Block.UPDATE_CLIENTS);
                            int damage = stack.getDamageValue() + 1;
                            stack.setDamageValue(damage);

                            if (damage >= stack.getMaxDamage())
                                stack.remove(DataComponents.DYED_COLOR);
                        }

                        return InteractionResult.CONSUME;
                    }
                }
            }
        }

        return super.useOn(context);
    }
}