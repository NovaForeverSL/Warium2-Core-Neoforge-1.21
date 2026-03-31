package net.w2cdev.warium2_core.item;

import net.w2cdev.warium2_core.Warium2_Core;
import net.w2cdev.warium2_core.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Warium2_Core.MODID);

    public static final DeferredItem<Item> INGOT_ZINC = ITEMS.registerSimpleItem("ingot_zinc", new Item.Properties());
    public static final DeferredItem<Item> RAW_ZINC = ITEMS.registerSimpleItem("raw_zinc", new Item.Properties());

    public static final DeferredItem<Item> INGOT_BERYLLIUM = ITEMS.registerSimpleItem("ingot_beryllium", new Item.Properties());
    public static final DeferredItem<Item> RAW_BERYLLIUM = ITEMS.registerSimpleItem("raw_beryllium", new Item.Properties());

    public static final DeferredItem<Item> INGOT_LEAD = ITEMS.registerSimpleItem("ingot_lead", new Item.Properties());
    public static final DeferredItem<Item> RAW_LEAD = ITEMS.registerSimpleItem("raw_lead", new Item.Properties());

    public static final DeferredItem<Item> INGOT_NICKEL = ITEMS.registerSimpleItem("ingot_nickel", new Item.Properties());
    public static final DeferredItem<Item> RAW_NICKEL = ITEMS.registerSimpleItem("raw_nickel", new Item.Properties());

    public static final DeferredItem<Item> RAW_URANIUM = ITEMS.registerSimpleItem("raw_uranium", new Item.Properties());

    public static final DeferredItem<BlockItem> ORE_BERYLLIUM = registerBlockItem("ore_beryllium", ModBlocks.ORE_BERYLLIUM);
    public static final DeferredItem<BlockItem> ORE_BERYLLIUM_DEEPSLATE = registerBlockItem("ore_beryllium_deepslate", ModBlocks.ORE_BERYLLIUM_DEEPSLATE);
    public static final DeferredItem<BlockItem> BLOCK_BERYLLIUM = registerBlockItem("block_beryllium", ModBlocks.BLOCK_BERYLLIUM);
    public static final DeferredItem<BlockItem> RAW_BLOCK_BERYLLIUM = registerBlockItem("raw_block_beryllium", ModBlocks.RAW_BLOCK_BERYLLIUM);

    public static final DeferredItem<BlockItem> ORE_LEAD = registerBlockItem("ore_lead", ModBlocks.ORE_LEAD);
    public static final DeferredItem<BlockItem> ORE_LEAD_DEEPSLATE = registerBlockItem("ore_lead_deepslate", ModBlocks.ORE_LEAD_DEEPSLATE);
    public static final DeferredItem<BlockItem> BLOCK_LEAD = registerBlockItem("block_lead", ModBlocks.BLOCK_LEAD);
    public static final DeferredItem<BlockItem> RAW_BLOCK_LEAD = registerBlockItem("raw_block_lead", ModBlocks.RAW_BLOCK_LEAD);

    public static final DeferredItem<BlockItem> ORE_NICKEL = registerBlockItem("ore_nickel", ModBlocks.ORE_NICKEL);
    public static final DeferredItem<BlockItem> ORE_NICKEL_DEEPSLATE = registerBlockItem("ore_nickel_deepslate", ModBlocks.ORE_NICKEL_DEEPSLATE);
    public static final DeferredItem<BlockItem> BLOCK_NICKEL = registerBlockItem("block_nickel", ModBlocks.BLOCK_NICKEL);
    public static final DeferredItem<BlockItem> RAW_BLOCK_NICKEL = registerBlockItem("raw_block_nickel", ModBlocks.RAW_BLOCK_NICKEL);

    public static final DeferredItem<BlockItem> ORE_URANIUM = registerBlockItem("ore_uranium", ModBlocks.ORE_URANIUM);
    public static final DeferredItem<BlockItem> ORE_URANIUM_DEEPSLATE = registerBlockItem("ore_uranium_deepslate", ModBlocks.ORE_URANIUM_DEEPSLATE);
    public static final DeferredItem<BlockItem> RAW_BLOCK_URANIUM = registerBlockItem("raw_block_uranium", ModBlocks.RAW_BLOCK_URANIUM);

    public static final DeferredItem<BlockItem> ORE_ZINC = registerBlockItem("ore_zinc", ModBlocks.ORE_ZINC);
    public static final DeferredItem<BlockItem> ORE_ZINC_DEEPSLATE = registerBlockItem("ore_zinc_deepslate", ModBlocks.ORE_ZINC_DEEPSLATE);
    public static final DeferredItem<BlockItem> BLOCK_ZINC = registerBlockItem("block_zinc", ModBlocks.BLOCK_ZINC);
    public static final DeferredItem<BlockItem> RAW_BLOCK_ZINC = registerBlockItem("raw_block_zinc", ModBlocks.RAW_BLOCK_ZINC);

    public static final DeferredItem<BlockItem> BAUXITE = registerBlockItem("bauxite", ModBlocks.BAUXITE);
    public static final DeferredItem<BlockItem> TRINITITE = registerBlockItem("trinitite", ModBlocks.TRINITITE);
    
    public static final DeferredItem<BlockItem> CEMENTITE_REINFORCED = registerBlockItem("cementite_reinforced", ModBlocks.CEMENTITE_REINFORCED);
    public static final DeferredItem<BlockItem> CEMENTITE_CRACKED = registerBlockItem("cementite_cracked", ModBlocks.CEMENTITE_CRACKED);
    public static final DeferredItem<BlockItem> CEMENTITE_DAMAGED = registerBlockItem("cementite_damaged", ModBlocks.CEMENTITE_DAMAGED);
    public static final DeferredItem<BlockItem> CEMENTITE_FRACTURED = registerBlockItem("cementite_fractured", ModBlocks.CEMENTITE_FRACTURED);
    public static final DeferredItem<BlockItem> CEMENTITE_DESTROYED = registerBlockItem("cementite_destroyed", ModBlocks.CEMENTITE_DESTROYED);
    public static final DeferredItem<BlockItem> CEMENTITE_OVERGROWN = registerBlockItem("cementite_overgrown", ModBlocks.CEMENTITE_OVERGROWN);
    public static final DeferredItem<BlockItem> REBAR = registerBlockItem("rebar", ModBlocks.REBAR);

    private ModItems() {
    }

    private static DeferredItem<BlockItem> registerBlockItem(final String name, final DeferredBlock<?> block) {
        return ITEMS.registerSimpleBlockItem(name, block);
    }

    public static void register(final IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
