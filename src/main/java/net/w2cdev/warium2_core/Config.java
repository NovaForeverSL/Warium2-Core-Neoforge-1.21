package net.w2cdev.warium2_core;

import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue LOG_ZINC_ITEM = BUILDER
            .comment("Whether to log the zinc item id during common setup.")
            .define("logZincItem", true);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to validate as resource locations.")
            .defineListAllowEmpty(
                    "items",
                    List.of("minecraft:iron_ingot", Warium2_Core.MODID + ":zinc"),
                    () -> "",
                    Config::validateItemName
            );

    public static final ModConfigSpec SPEC = BUILDER.build();

    private Config() {
    }

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
