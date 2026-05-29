package net.warium2_core.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class WariumSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "warium2_core");

    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_RUMBLE =
            register("explosionrumble");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_SMALL =
            register("explosionsmall");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_SMALL_FAR =
            register("explosionsmallfar");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_LARGE =
            register("explosionlarge");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_LARGE_FAR =
            register("explosionlargefar");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_COLOSSAL =
            register("explosioncolossal");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION_COLOSSAL_FAR =
            register("explosioncolossalfar");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () ->
                SoundEvent.createVariableRangeEvent(
                        ResourceLocation.fromNamespaceAndPath("warium2_core", name)));
    }
}
