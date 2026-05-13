package net.warium2_core.lib.projectile.vpl.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.warium2_core.Warium2_Core;

public class Entitys {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Warium2_Core.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<SimpleBullet>> BULLET =
            ENTITY_TYPES.register("bullet", () -> EntityType.Builder.<SimpleBullet>of(
                            SimpleBullet::new,
                            MobCategory.MISC
                    )
                    .sized(0.1f, 0.1f)
                    .updateInterval(10)
                    .setShouldReceiveVelocityUpdates(true)
                    .fireImmune()
                    .build("bullet"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
