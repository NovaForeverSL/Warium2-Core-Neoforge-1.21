package net.warium.block.custom;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;

public class PaintableBlock extends Block {
    public static final DyedItemColor DEFAULT_ITEM_COLOR = new DyedItemColor(0xFFFFFF, true);
    public static final int
    ALUMINUM_COLOR = 0xB7B7B7,
    STEEL_COLOR    = 0x929292;
    public static final IntegerProperty
    RED   = IntegerProperty.create("red",   0, 7),
    GREEN = IntegerProperty.create("green", 0, 7),
    BLUE  = IntegerProperty.create("blue",  0, 7);
    public final DyedItemColor defaultColor;

    /**
     * @param properties
     * @param defaultColor WARNING! Only use colors from {@link #rgbToState} switch
     */
    public PaintableBlock(Properties properties, int defaultColor) {
        super(properties);
        this.defaultColor = new DyedItemColor(defaultColor, true);
        registerDefaultState(setColor(this.stateDefinition.any(), defaultColor));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RED, GREEN, BLUE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return setColor(defaultBlockState(), getColor(context.getItemInHand()));
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);

        if (state != defaultBlockState())
            setColor(stack, getColor(state));

        return stack;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ItemStack stack = new ItemStack(this);

        if (state != defaultBlockState())
            setColor(stack, getColor(state));
        
        return List.of(stack);
    }

    public static DyedItemColor getDefaultColor(ItemStack stack) {
        return stack.getItem() instanceof BlockItem block && block.getBlock() instanceof PaintableBlock paintable
        ? paintable.defaultColor
        : DEFAULT_ITEM_COLOR;
    }

    public static BlockState setColor(BlockState state, int color) {
        return state
            .setValue(RED,   rgbToState(color >> 16 & 0xFF))
            .setValue(GREEN, rgbToState(color >> 8  & 0xFF))
            .setValue(BLUE,  rgbToState(color       & 0xFF));
    }

    public static DyedItemColor setColor(ItemStack stack, int color) {
        DyedItemColor defaultColor = getDefaultColor(stack);
        return stack.set(
            DataComponents.DYED_COLOR,
            color == defaultColor.rgb() ? defaultColor : new DyedItemColor(color, true)
        );
    }

    public static int getColor(BlockState state) {
        return (stateToRGB(state.getValue(PaintableBlock.RED))   << 16) |
               (stateToRGB(state.getValue(PaintableBlock.GREEN)) <<  8) |
                stateToRGB(state.getValue(PaintableBlock.BLUE));
    }

    public static int stateToRGB(int value) {
        return switch (value) {
            case 0  -> 0x00;
            case 1  -> 0x25;
            case 2  -> 0x49;
            case 3  -> 0x6E;
            case 4  -> 0x92;
            case 5  -> 0xB7;
            case 6  -> 0xDB;
            default -> 0xFF;
        };
    }

    public static int rgbToState(int value) {
        return switch (value) {
            case 0x00 -> 0;
            case 0x25 -> 1;
            case 0x49 -> 2;
            case 0x6E -> 3;
            case 0x92 -> 4;
            case 0xB7 -> 5;
            case 0xDB -> 6;
            case 0xFF -> 7;
            default   -> Math.round(value * 7 / 255f);
        };
    }

    public static int getColor(ItemStack stack) {
        return stack.getOrDefault(DataComponents.DYED_COLOR, getDefaultColor(stack)).rgb();
    }
}