package net.warium.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.warium.Warium;
import net.warium.particles.ScaledExplosionParticles;
import net.warium.registry.WariumParticles;

@EventBusSubscriber(modid = Warium.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(WariumParticles.ScaledExplosionParticles.get(), ScaledExplosionParticles.Provider::new);
    }
}
