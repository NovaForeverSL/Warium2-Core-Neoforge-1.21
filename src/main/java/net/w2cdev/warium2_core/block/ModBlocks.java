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

    public static final DeferredBlock<Block> ORE_BERYLLIUM = registerSimpleBlock("ore_beryllium", Blocks.IRON_ORE);
    public static final DeferredBlock<Block> ORE_BERYLLIUM_DEEPSLATE = registerSimpleBlock("ore_beryllium_deepslate", Blocks.DEEPSLATE_IRON_ORE);
    public static final DeferredBlock<Block> BLOCK_BERYLLIUM = registerSimpleBlock("block_beryllium", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> RAW_BLOCK_BERYLLIUM = registerSimpleBlock("raw_block_beryllium", Blocks.RAW_IRON_BLOCK);

    public static final DeferredBlock<Block> ORE_LEAD = registerSimpleBlock("ore_lead", Blocks.IRON_ORE);
    public static final DeferredBlock<Block> ORE_LEAD_DEEPSLATE = registerSimpleBlock("ore_lead_deepslate", Blocks.DEEPSLATE_IRON_ORE);
    public static final DeferredBlock<Block> BLOCK_LEAD = registerSimpleBlock("block_lead", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> RAW_BLOCK_LEAD = registerSimpleBlock("raw_block_lead", Blocks.RAW_IRON_BLOCK);

    public static final DeferredBlock<Block> ORE_NICKEL = registerSimpleBlock("ore_nickel", Blocks.IRON_ORE);
    public static final DeferredBlock<Block> ORE_NICKEL_DEEPSLATE = registerSimpleBlock("ore_nickel_deepslate", Blocks.DEEPSLATE_IRON_ORE);
    public static final DeferredBlock<Block> BLOCK_NICKEL = registerSimpleBlock("block_nickel", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> RAW_BLOCK_NICKEL = registerSimpleBlock("raw_block_nickel", Blocks.RAW_IRON_BLOCK);

    public static final DeferredBlock<Block> ORE_URANIUM = registerSimpleBlock("ore_uranium", Blocks.IRON_ORE);
    public static final DeferredBlock<Block> ORE_URANIUM_DEEPSLATE = registerSimpleBlock("ore_uranium_deepslate", Blocks.DEEPSLATE_IRON_ORE);
    public static final DeferredBlock<Block> RAW_BLOCK_URANIUM = registerSimpleBlock("raw_block_uranium", Blocks.RAW_IRON_BLOCK);

    public static final DeferredBlock<Block> ORE_ZINC = registerSimpleBlock("ore_zinc", Blocks.IRON_ORE);
    public static final DeferredBlock<Block> ORE_ZINC_DEEPSLATE = registerSimpleBlock("ore_zinc_deepslate", Blocks.DEEPSLATE_IRON_ORE);
    public static final DeferredBlock<Block> BLOCK_ZINC = registerSimpleBlock("block_zinc", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> RAW_BLOCK_ZINC = registerSimpleBlock("raw_block_zinc", Blocks.RAW_IRON_BLOCK);

    public static final DeferredBlock<Block> BAUXITE = registerSimpleBlock("bauxite", Blocks.IRON_ORE);
    public static final DeferredBlock<Block> TRINITITE = registerSimpleBlock("trinitite", Blocks.GLASS);

    public static final DeferredBlock<Block> CEMENTITE_REINFORCED = registerSimpleBlock("cementite_reinforced", Blocks.STONE);
    public static final DeferredBlock<Block> CEMENTITE_CRACKED = registerSimpleBlock("cementite_cracked", Blocks.STONE);
    public static final DeferredBlock<Block> CEMENTITE_DAMAGED = registerSimpleBlock("cementite_damaged", Blocks.STONE);
    public static final DeferredBlock<Block> CEMENTITE_FRACTURED = registerSimpleBlock("cementite_fractured", Blocks.STONE);
    public static final DeferredBlock<Block> CEMENTITE_DESTROYED = registerSimpleBlock("cementite_destroyed", Blocks.STONE);
    public static final DeferredBlock<Block> CEMENTITE_OVERGROWN = registerSimpleBlock("cementite_overgrown", Blocks.STONE);

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
