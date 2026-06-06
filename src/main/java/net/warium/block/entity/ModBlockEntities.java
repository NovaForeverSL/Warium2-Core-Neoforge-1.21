package net.warium.block.entity;

import net.warium.Warium;
import net.warium.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Warium.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PaintableMetalBlockEntity>> PAINTABLE_METAL_BLOCK = BLOCK_ENTITY_TYPES.register(
            "paintable_metal_block",
            () -> BlockEntityType.Builder.of(PaintableMetalBlockEntity::new, ModBlocks.PLATING_STEEL.get(), ModBlocks.PLATING_ALUMINUM.get()).build(null)
    );

    private ModBlockEntities() {
    }

    public static void register(final IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
