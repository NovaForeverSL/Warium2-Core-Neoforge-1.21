package net.warium.particles;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.vibey.vel.api.particle.TextureSheetMultiParticle;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class ScaledExplosionParticles extends TextureSheetMultiParticle {
    public static final Logger LOGGER = LogUtils.getLogger();
    protected ScaledExplosionParticles(ClientLevel level, double x, double y, double z, SpriteSet sprites, Minecraft minecraft, float radius){
        super(level, x, y, z, minecraft);
        radius *= 2;
        this.lifetime = 100;
        this.hasPhysics = false;
        this.gravity = 0.0F;
        this.quadSize = 0.05f * radius;
        this.pickSprite(sprites);

        int count = (int) radius * 10;

        LOGGER.info("spawn sphere");
        int sphereStart = addSubParticlesOnSphere(count, radius / 2);
        for(int i = sphereStart; i < sphereStart + count; ++i) {
            this.getSubParticle(i).setScale(1F).setAlpha(0.7F);
        }

        addSubParticleAtOrigin();
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        LOGGER.info("tick");
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new ScaledExplosionParticles(level, x, y, z, this.sprites, Minecraft.getInstance(), (float) dx);
        }
    }
}
