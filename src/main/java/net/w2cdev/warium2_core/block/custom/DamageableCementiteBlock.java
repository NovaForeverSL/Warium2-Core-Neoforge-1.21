package net.w2cdev.warium2_core.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;


public class DamageableCementiteBlock extends Block {
    public static final int MAX_DAMAGE_STAGE = 4;
    public static final IntegerProperty DAMAGE = IntegerProperty.create("damage", 0, MAX_DAMAGE_STAGE);
    public static final BooleanProperty SNOWY = BooleanProperty.create("snowy");

    public DamageableCementiteBlock(final BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(DAMAGE, 0)
                .setValue(SNOWY, false));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DAMAGE, SNOWY);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(final BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(SNOWY, isSnowLayer(context.getLevel().getBlockState(context.getClickedPos().above())));
    }

    @Override
    protected BlockState updateShape(
            final BlockState state,
            final Direction direction,
            final BlockState neighborState,
            final LevelAccessor level,
            final BlockPos pos,
            final BlockPos neighborPos
    ) {
        if (direction == Direction.UP) {
            return state.setValue(SNOWY, isSnowLayer(neighborState));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }


    @Override
    protected void neighborChanged(
            final BlockState state,
            final Level level,
            final BlockPos pos,
            final Block neighborBlock,
            final BlockPos neighborPos,
            final boolean movedByPiston
    ) {
        if (!level.isClientSide && neighborPos.equals(pos.above())) {
            final boolean snowy = isSnowLayer(level.getBlockState(pos.above()));
            if (state.getValue(SNOWY) != snowy) {
                level.setBlock(pos, state.setValue(SNOWY, snowy), Block.UPDATE_CLIENTS);
            }
        }

        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    private static boolean isSnowLayer(final BlockState state) {
        return state.getBlock() instanceof SnowLayerBlock;
    }

    public void applyDamage(final ServerLevel level, final BlockPos pos, final BlockState state, final int amount) {
        if (amount <= 0) {
            return;
        }

        final int nextStage = state.getValue(DAMAGE) + amount;
        if (nextStage > MAX_DAMAGE_STAGE) {
            level.destroyBlock(pos, false);
            return;
        }

        level.setBlock(pos, state.setValue(DAMAGE, nextStage), Block.UPDATE_CLIENTS);
    }
}
