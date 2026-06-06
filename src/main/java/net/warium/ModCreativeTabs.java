package net.warium;

import net.warium.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Set;

public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Warium.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RESOURCES_TAB =
            CREATIVE_MODE_TABS.register("resources", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.warium.resources"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.INGOT_ZINC.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        final Set<Item> constructionItems = Set.of(
                                ModItems.CEMENTITE_REINFORCED.get(),
                                ModItems.CEMENTITE_OVERGROWN.get(),
                                ModItems.CEMENTITE_STRUCTURAL.get(),
                                ModItems.REBAR.get()
                        );

                        ModItems.ITEMS.getEntries().stream()
                                .map(entry -> entry.get())
                                .filter(item -> !constructionItems.contains(item))
                                .forEach(output::accept);
                    })
                    .build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CONSTRUCTIONS_TAB =
            CREATIVE_MODE_TABS.register("constructions", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.warium.constructions"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.CEMENTITE_REINFORCED.get().getDefaultInstance())
                    .displayItems((parameters, output) -> List.of(
                            ModItems.CEMENTITE_REINFORCED,
                            ModItems.CEMENTITE_OVERGROWN,
                            ModItems.CEMENTITE_STRUCTURAL,
                            ModItems.REBAR
                    ).forEach(output::accept))
                    .build());

    private ModCreativeTabs() {}

    public static void register(final IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
        eventBus.addListener(ModCreativeTabs::addToVanillaTabs);
    }

    private static void addToVanillaTabs(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            List.of(
                    ModItems.INGOT_ZINC,
                    ModItems.INGOT_BERYLLIUM,
                    ModItems.INGOT_NICKEL,
                    ModItems.INGOT_LEAD,
                    ModItems.RAW_ZINC,
                    ModItems.RAW_BERYLLIUM,
                    ModItems.RAW_LEAD,
                    ModItems.RAW_NICKEL,
                    ModItems.RAW_URANIUM,
                    ModItems.INGOT_STEEL,
                    ModItems.INGOT_ALUMINUM,
                    ModItems.PAINT_GUN
            ).forEach(event::accept);
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            List.of(
                    ModItems.PAINT_GUN
            ).forEach(event::accept);
        }

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            List.of(
                    ModItems.BAUXITE,
                    ModItems.TRINITITE,
                    ModItems.ORE_BERYLLIUM,
                    ModItems.ORE_BERYLLIUM_DEEPSLATE,
                    ModItems.ORE_LEAD,
                    ModItems.ORE_LEAD_DEEPSLATE,
                    ModItems.ORE_NICKEL,
                    ModItems.ORE_NICKEL_DEEPSLATE,
                    ModItems.ORE_URANIUM,
                    ModItems.ORE_URANIUM_DEEPSLATE,
                    ModItems.ORE_ZINC,
                    ModItems.ORE_ZINC_DEEPSLATE,
                    ModItems.BLOCK_BERYLLIUM,
                    ModItems.BLOCK_LEAD,
                    ModItems.BLOCK_NICKEL,
                    ModItems.BLOCK_ZINC,
                    ModItems.RAW_BLOCK_BERYLLIUM,
                    ModItems.RAW_BLOCK_LEAD,
                    ModItems.RAW_BLOCK_NICKEL,
                    ModItems.RAW_BLOCK_URANIUM,
                    ModItems.RAW_BLOCK_ZINC,
                    ModItems.BLOCK_STEEL,
                    ModItems.BLOCK_ALUMINUM,
                    ModItems.CEMENTITE_REINFORCED,
                    ModItems.CEMENTITE_OVERGROWN,
                    ModItems.CEMENTITE_STRUCTURAL,
                    ModItems.REBAR
            ).forEach(event::accept);
        }
    }
}