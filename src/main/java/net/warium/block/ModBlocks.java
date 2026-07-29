package net.warium.block;

import net.warium.Warium;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.PushReaction;
import net.warium.block.custom.*;
import net.warium.fluid.ModFluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Warium.MODID);

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

    public static final DeferredBlock<Block> BLOCK_STEEL = registerSimpleBlock("block_steel", Blocks.IRON_BLOCK);
    public static final DeferredBlock<Block> BLOCK_ALUMINUM = registerSimpleBlock("block_aluminum", Blocks.IRON_BLOCK);

    public static final DeferredBlock<PaintableBlock> PLATING_STEEL = BLOCKS.register(
        "plating_steel",
        () -> new PaintableBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops(),
            PaintableBlock.STEEL_COLOR
        )
    );

    public static final DeferredBlock<PaintableBlock> PLATING_ALUMINUM = BLOCKS.register(
        "plating_aluminum",
        () -> new PaintableBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops(),
            PaintableBlock.ALUMINUM_COLOR
        )
    );

    public static final DeferredBlock<Block> BAUXITE = registerSimpleBlock("bauxite", Blocks.IRON_ORE);
    public static final DeferredBlock<Block> TRINITITE = registerSimpleBlock("trinitite", Blocks.GLASS);

    public static final DeferredBlock<LiquidBlock> DIESEL = registerFluidBlock("diesel", ModFluids.DIESEL);
    public static final DeferredBlock<LiquidBlock> KEROSENE = registerFluidBlock("kerosene", ModFluids.KEROSENE);
    public static final DeferredBlock<LiquidBlock> OIL = registerFluidBlock("oil", ModFluids.OIL);
    public static final DeferredBlock<LiquidBlock> PETROL = registerFluidBlock("petrol", ModFluids.PETROL);
    public static final DeferredBlock<LiquidBlock> SULFURIC_ACID = registerFluidBlock("sulfuric_acid", ModFluids.SULFURIC_ACID);

    public static final DeferredBlock<DamageableCementiteBlock> CEMENTITE_REINFORCED = BLOCKS.registerBlock(
        "cementite_reinforced",
        DamageableCementiteBlock::new,
        BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(4.0F, 12.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<DamageableCementiteBlock> CEMENTITE_OVERGROWN = BLOCKS.registerBlock(
        "cementite_overgrown",
        DamageableCementiteBlock::new,
        BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(4.0F, 12.0F)
                    .requiresCorrectToolForDrops()
    );

     public static final DeferredBlock<DamageableCementiteBlock> CEMENTITE_STRUCTURAL = BLOCKS.registerBlock(
        "cementite_structural",
        DamageableCementiteBlock::new,
        BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(60.0F, 42.0F)
            .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<IronBarsBlock> REBAR = BLOCKS.registerBlock(
        "rebar",
        IronBarsBlock::new,
        BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).noOcclusion()
                .requiresCorrectToolForDrops()
    );

    private ModBlocks() {
    }

    private static DeferredBlock<LiquidBlock> registerFluidBlock(final String name, final net.neoforged.neoforge.registries.DeferredHolder<net.minecraft.world.level.material.Fluid, net.minecraft.world.level.material.Fluid> fluid) {
        return BLOCKS.registerBlock(
            name,
            properties -> new LiquidBlock((FlowingFluid) fluid.get(), properties),
            BlockBehaviour.Properties.of()
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
        );
    }

    private static DeferredBlock<Block> registerSimpleBlock(final String name, final Block template) {
        return BLOCKS.registerSimpleBlock(
            name,
            BlockBehaviour.Properties.ofFullCopy(template).requiresCorrectToolForDrops()
        );
    }

    public static void register(final IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
