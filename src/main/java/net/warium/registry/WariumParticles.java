package net.warium.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warium.Warium;

public class WariumParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, Warium.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ScaledExplosionParticles =
            PARTICLE_TYPES.register("scaled_explosion_particles", () -> new SimpleParticleType(true)); //ALWAYS OVERRIDE!!!

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
