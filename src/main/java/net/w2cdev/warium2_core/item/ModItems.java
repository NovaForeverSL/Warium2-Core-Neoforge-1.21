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

    public static final DeferredItem<Item> ZINC = ITEMS.registerSimpleItem("zinc", new Item.Properties());
    public static final DeferredItem<Item> RAW_ZINC = ITEMS.registerSimpleItem("raw_zinc", new Item.Properties());

    public static final DeferredItem<Item> BERYLLIUM_INGOT = ITEMS.registerSimpleItem("beryllium_ingot", new Item.Properties());
    public static final DeferredItem<Item> RAW_BERYLLIUM = ITEMS.registerSimpleItem("raw_beryllium", new Item.Properties());

    public static final DeferredItem<Item> LEAD_INGOT = ITEMS.registerSimpleItem("lead_ingot", new Item.Properties());
    public static final DeferredItem<Item> RAW_LEAD = ITEMS.registerSimpleItem("raw_lead", new Item.Properties());

    public static final DeferredItem<Item> LITHIUM_INGOT = ITEMS.registerSimpleItem("lithium_ingot", new Item.Properties());
    public static final DeferredItem<Item> RAW_LITHIUM = ITEMS.registerSimpleItem("raw_lithium", new Item.Properties());

    public static final DeferredItem<Item> NICKEL_INGOT = ITEMS.registerSimpleItem("nickel_ingot", new Item.Properties());
    public static final DeferredItem<Item> RAW_NICKEL = ITEMS.registerSimpleItem("raw_nickel", new Item.Properties());

    public static final DeferredItem<Item> URANIUM_INGOT = ITEMS.registerSimpleItem("uranium_ingot", new Item.Properties());
    public static final DeferredItem<Item> RAW_URANIUM = ITEMS.registerSimpleItem("raw_uranium", new Item.Properties());

    public static final DeferredItem<BlockItem> BERYLLIUM_ORE = registerBlockItem("beryllium_ore", ModBlocks.BERYLLIUM_ORE);
    public static final DeferredItem<BlockItem> BERYLLIUM_BLOCK = registerBlockItem("beryllium_block", ModBlocks.BERYLLIUM_BLOCK);
    public static final DeferredItem<BlockItem> RAW_BERYLLIUM_BLOCK = registerBlockItem("raw_beryllium_block", ModBlocks.RAW_BERYLLIUM_BLOCK);

    public static final DeferredItem<BlockItem> LEAD_ORE = registerBlockItem("lead_ore", ModBlocks.LEAD_ORE);
    public static final DeferredItem<BlockItem> DEEPSLATE_LEAD_ORE = registerBlockItem("deepslate_lead_ore", ModBlocks.DEEPSLATE_LEAD_ORE);
    public static final DeferredItem<BlockItem> LEAD_BLOCK = registerBlockItem("lead_block", ModBlocks.LEAD_BLOCK);
    public static final DeferredItem<BlockItem> RAW_LEAD_BLOCK = registerBlockItem("raw_lead_block", ModBlocks.RAW_LEAD_BLOCK);

    public static final DeferredItem<BlockItem> LITHIUM_ORE = registerBlockItem("lithium_ore", ModBlocks.LITHIUM_ORE);
    public static final DeferredItem<BlockItem> LITHIUM_BLOCK = registerBlockItem("lithium_block", ModBlocks.LITHIUM_BLOCK);
    public static final DeferredItem<BlockItem> RAW_LITHIUM_BLOCK = registerBlockItem("raw_lithium_block", ModBlocks.RAW_LITHIUM_BLOCK);

    public static final DeferredItem<BlockItem> NICKEL_ORE = registerBlockItem("nickel_ore", ModBlocks.NICKEL_ORE);
    public static final DeferredItem<BlockItem> NICKEL_BLOCK = registerBlockItem("nickel_block", ModBlocks.NICKEL_BLOCK);
    public static final DeferredItem<BlockItem> RAW_NICKEL_BLOCK = registerBlockItem("raw_nickel_block", ModBlocks.RAW_NICKEL_BLOCK);

    public static final DeferredItem<BlockItem> URANIUM_ORE = registerBlockItem("uranium_ore", ModBlocks.URANIUM_ORE);
    public static final DeferredItem<BlockItem> URANIUM_BLOCK = registerBlockItem("uranium_block", ModBlocks.URANIUM_BLOCK);
    public static final DeferredItem<BlockItem> RAW_URANIUM_BLOCK = registerBlockItem("raw_uranium_block", ModBlocks.RAW_URANIUM_BLOCK);

    public static final DeferredItem<BlockItem> ZINC_ORE = registerBlockItem("zinc_ore", ModBlocks.ZINC_ORE);
    public static final DeferredItem<BlockItem> ZINC_BLOCK = registerBlockItem("zinc_block", ModBlocks.ZINC_BLOCK);
    public static final DeferredItem<BlockItem> RAW_ZINC_BLOCK = registerBlockItem("raw_zinc_block", ModBlocks.RAW_ZINC_BLOCK);

    public static final DeferredItem<BlockItem> BAUXITE = registerBlockItem("bauxite", ModBlocks.BAUXITE);
    public static final DeferredItem<BlockItem> REINFORCED_CONCRETE = registerBlockItem("reinforced_concrete", ModBlocks.REINFORCED_CONCRETE);
    public static final DeferredItem<BlockItem> CRACKED_CONCRETE = registerBlockItem("cracked_concrete", ModBlocks.CRACKED_CONCRETE);
    public static final DeferredItem<BlockItem> DAMAGED_CONCRETE = registerBlockItem("damaged_concrete", ModBlocks.DAMAGED_CONCRETE);
    public static final DeferredItem<BlockItem> FRACTURED_CONCRETE = registerBlockItem("fractured_concrete", ModBlocks.FRACTURED_CONCRETE);
    public static final DeferredItem<BlockItem> DESTROYED_CONCRETE = registerBlockItem("destroyed_concrete", ModBlocks.DESTROYED_CONCRETE);
    public static final DeferredItem<BlockItem> OVERGROWN_REINFORCED_CONCRETE = registerBlockItem("overgrown_reinforced_concrete", ModBlocks.OVERGROWN_REINFORCED_CONCRETE);
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
