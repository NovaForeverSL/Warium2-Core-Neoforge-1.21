package net.warium.particles;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.vibey.vel.api.particle.TextureSheetMultiParticle;
import org.slf4j.Logger;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class ScaledExplosionParticles extends TextureSheetMultiParticle {
    public static final Logger LOGGER = LogUtils.getLogger();
    protected final Set<Integer> darkCloudList = new HashSet<>();
    protected final Set<Integer> flameCloudList = new HashSet<>();
    protected ScaledExplosionParticles(ClientLevel level, double x, double y, double z, SpriteSet sprites, Minecraft minecraft, float radius){
        super(level, x, y, z, minecraft);
        //radius *= 1.5f;
        this.lifetime = 100;
        this.hasPhysics = false;
        this.gravity = 0.0F;
        this.quadSize = 0.05f * radius * 2;
        this.xd = 0;
        this.yd = 0.1;
        this.zd = 0;

        TextureAtlasSprite darkSmoke = sprites.get(6, 11);

        int count = (int) radius * 10;

        int sphereStart = addSubParticlesOnSphere(count, radius / 2);
        for(int i = sphereStart; i < sphereStart + count; ++i) {
            getSubParticle(i).setUvOverride(
                    darkSmoke.getU0(), darkSmoke.getU1(),
                    darkSmoke.getV0(), darkSmoke.getV1()
            );
            this.getSubParticle(i).setScale(2F).setAlpha(1F);
            darkCloudList.add(i);
        }



    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {

        for (int i : darkCloudList){
            SubParticle sp = this.getSubParticle(i);
            sp.moveTo(sp.lx, sp.ly + 0.1F, sp.lz);
        }

//        this.xo = this.x;
//        this.yo = this.y;
//        this.zo = this.z;
//        this.x += this.xd;
//        this.y += this.yd;
//        this.z += this.zd;

//        if (this.age++ >= this.lifetime) {
//            this.remove();
//        }
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
