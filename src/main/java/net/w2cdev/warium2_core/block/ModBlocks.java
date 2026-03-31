package net.w2cdev.warium2_core.block;

import net.w2cdev.warium2_core.Warium2_Core;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Warium2_Core.MODID);

    public static final DeferredBlock<Block> BERYLLIUM_ORE = registerSimpleBlock("beryllium_ore", Blocks.IRON_ORE);
    public static final DeferredBlock<Block> BERYLLIUM_BLOCK = registerSimpleBlock("beryllium_block", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> RAW_BERYLLIUM_BLOCK = registerSimpleBlock("raw_beryllium_block", Blocks.RAW_IRON_BLOCK);

    public static final DeferredBlock<Block> LEAD_ORE = registerSimpleBlock("lead_ore", Blocks.IRON_ORE);
    public static final DeferredBlock<Block> DEEPSLATE_LEAD_ORE = registerSimpleBlock("deepslate_lead_ore", Blocks.DEEPSLATE_IRON_ORE);
    public static final DeferredBlock<Block> LEAD_BLOCK = registerSimpleBlock("lead_block", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> RAW_LEAD_BLOCK = registerSimpleBlock("raw_lead_block", Blocks.RAW_IRON_BLOCK);

    public static final DeferredBlock<Block> LITHIUM_ORE = registerSimpleBlock("lithium_ore", Blocks.IRON_ORE);
    public static final DeferredBlock<Block> LITHIUM_BLOCK = registerSimpleBlock("lithium_block", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> RAW_LITHIUM_BLOCK = registerSimpleBlock("raw_lithium_block", Blocks.RAW_IRON_BLOCK);

    public static final DeferredBlock<Block> NICKEL_ORE = registerSimpleBlock("nickel_ore", Blocks.IRON_ORE);
    public static final DeferredBlock<Block> NICKEL_BLOCK = registerSimpleBlock("nickel_block", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> RAW_NICKEL_BLOCK = registerSimpleBlock("raw_nickel_block", Blocks.RAW_IRON_BLOCK);

    public static final DeferredBlock<Block> URANIUM_ORE = registerSimpleBlock("uranium_ore", Blocks.IRON_ORE);
    public static final DeferredBlock<Block> URANIUM_BLOCK = registerSimpleBlock("uranium_block", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> RAW_URANIUM_BLOCK = registerSimpleBlock("raw_uranium_block", Blocks.RAW_IRON_BLOCK);

    public static final DeferredBlock<Block> ZINC_ORE = registerSimpleBlock("zinc_ore", Blocks.IRON_ORE);
    public static final DeferredBlock<Block> ZINC_BLOCK = registerSimpleBlock("zinc_block", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> RAW_ZINC_BLOCK = registerSimpleBlock("raw_zinc_block", Blocks.RAW_IRON_BLOCK);

    public static final DeferredBlock<Block> BAUXITE = registerSimpleBlock("bauxite", Blocks.IRON_ORE);

    public static final DeferredBlock<Block> REINFORCED_CONCRETE = registerSimpleBlock("reinforced_concrete", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> CRACKED_CONCRETE = registerSimpleBlock("cracked_concrete", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> DAMAGED_CONCRETE = registerSimpleBlock("damaged_concrete", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> FRACTURED_CONCRETE = registerSimpleBlock("fractured_concrete", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> DESTROYED_CONCRETE = registerSimpleBlock("destroyed_concrete", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> OVERGROWN_REINFORCED_CONCRETE = registerSimpleBlock("overgrown_reinforced_concrete", Blocks.IRON_BLOCK);

    public static final DeferredBlock<IronBarsBlock> REBAR = BLOCKS.registerBlock(
            "rebar",
            IronBarsBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).noOcclusion()
    );

    private ModBlocks() {
    }

    private static DeferredBlock<Block> registerSimpleBlock(final String name, final Block template) {
        return BLOCKS.registerSimpleBlock(name, BlockBehaviour.Properties.ofFullCopy(template));
    }

    public static void register(final IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
