package net.warium.data;

import net.warium.Warium;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class WariumItemTags {
    private WariumItemTags() {}
    
    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Warium.MODID, name));
    }

    public static final TagKey<Item> PAINTABLE = create("paintable");
}
