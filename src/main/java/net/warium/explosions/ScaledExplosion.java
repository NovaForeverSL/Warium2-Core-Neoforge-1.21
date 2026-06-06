package net.warium.explosions;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.warium.lib.explosion.BaseExplosion;
import net.warium.registry.WariumParticles;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = "warium", value = Dist.CLIENT)
public class ScaledExplosion extends BaseExplosion {

    private static final double SPEED_OF_SOUND   = 343.0;
    private static final double BLOCKS_PER_TICK   = SPEED_OF_SOUND / 20.0;

    public static final Logger LOGGER = LogUtils.getLogger();

    private static class DelayedSound {
        int ticksRemaining;
        final double x, y, z;
        final String soundPath;
        final float volume, pitch;

        DelayedSound(int ticks, double x, double y, double z, String soundPath, float volume, float pitch) {
            this.ticksRemaining = ticks;
            this.x = x; this.y = y; this.z = z;
            this.soundPath = soundPath; this.volume = volume; this.pitch = pitch;
        }
    }

    private static final List<DelayedSound> DELAYED_SOUNDS = new ArrayList<>();

    private enum SoundTier {
        SMALL   ( 3.0,          1.15f, 1.2f, "warium:explosionsmallfar",     30F,  1f,    1.05f, "warium:explosionsmall",     3F,  1f,    1.05f),
        MEDIUM  (10.0,          1.15f, 1.2f, "warium:explosionlargefar",     30F,  1.15f, 1.2f,  "warium:explosionlarge",     4F,  1.15f, 1.2f ),
        LARGE   (20.0,          1.15f, 1.2f, "warium:explosionlargefar",     64F,  1.15f, 1.2f,  "warium:explosionlarge",     6F,  1.15f, 1.2f ),
        MEGA    (30.0,          1.15f, 1.2f, "warium:explosionlargefar",     64F,  1.15f, 1.2f,  "warium:explosionlarge",     8F,  1.15f, 1.2f ),
        COLOSSAL(Double.MAX_VALUE, 0.95f, 1.0f, "warium:explosioncolossalfar", 400F, 1.15f, 1.2f, "warium:explosioncolossal", 32F,  1.15f, 1.2f );

        final double maxPower;
        final float rumbleMin, rumbleMax;
        final String farSound, nearSound;
        final float farVol,  nearVol;
        final float farMinP, farMaxP, nearMinP, nearMaxP;

        SoundTier(double maxPower,
                  float rMin, float rMax,
                  String far, float fV, float fMinP, float fMaxP,
                  String near, float nV, float nMinP, float nMaxP) {
            this.maxPower  = maxPower;
            this.rumbleMin = rMin; this.rumbleMax = rMax;
            this.farSound  = far;  this.farVol    = fV;  this.farMinP  = fMinP; this.farMaxP  = fMaxP;
            this.nearSound = near; this.nearVol   = nV;  this.nearMinP = nMinP; this.nearMaxP = nMaxP;
        }

        static SoundTier get(double power) {
            for (SoundTier t : values()) if (power <= t.maxPower) return t;
            return COLOSSAL;
        }
    }

    public ScaledExplosion(
            Level level,
            @Nullable Entity source,
            double x, double y, double z,
            float radius,
            float scaleX, float scaleY, float scaleZ,
            boolean fire,
            Explosion.BlockInteraction blockInteraction
    ) {
        super(level, source, x, y, z, radius, scaleX, scaleY, scaleZ, fire, blockInteraction);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void playSound(boolean playSound) {
        if (!playSound) return;

        LOGGER.info("sound started");

        double power = this.radius;
        double x = this.x, y = this.y, z = this.z;

        Minecraft      mc   = Minecraft.getInstance();
        RandomSource   rand = RandomSource.create();
        SoundTier      tier = SoundTier.get(power);

        int tickDelay = 0;
        if (mc.player != null) {
            double distance = Math.sqrt(mc.player.distanceToSqr(x, y, z));
            tickDelay = (int) Math.floor(distance / BLOCKS_PER_TICK);
        }

        double pitchDivider = power / 10.0;
        boolean colossal    = (tier == SoundTier.COLOSSAL);

        float rumblePitch = (float) Mth.nextDouble(rand, tier.rumbleMin, tier.rumbleMax)
                - (colossal ? 0 : (float) pitchDivider);
        float farPitch    = (float) Mth.nextDouble(rand, tier.farMinP,   tier.farMaxP)
                - (colossal ? 0 : (float) (pitchDivider / 2.0));
        float nearPitch   = (float) Mth.nextDouble(rand, tier.nearMinP,  tier.nearMaxP)
                - (colossal ? 0 : (float) (pitchDivider / 3.0));

        float rumbleVol = (power >= 30) ? 800F : 200F;
        queueSound(tickDelay, x, y, z, "warium:explosionrumble", rumbleVol, rumblePitch);
        queueSound(tickDelay, x, y, z, tier.farSound,  tier.farVol,  farPitch);
        queueSound(tickDelay, x, y, z, tier.nearSound, tier.nearVol, nearPitch);
        LOGGER.info("sound finished");
    }

    @Override
    public void spawnParticles(boolean spawnParticles, boolean interacts) {
        LOGGER.info("spawn particles");
        level.addParticle(
                WariumParticles.ScaledExplosionParticles.get(),
                this.x,
                this.y,
                this.z,
                radius, 0, 0
        );
        LOGGER.info("spawn particles finished");
    }

    @Override
    public void finalizeExplosion(boolean spawnParticles, boolean playSound, boolean spawnDrops) {
        super.finalizeExplosion(spawnParticles, playSound, spawnDrops);
    }

    private static void queueSound(int delay, double x, double y, double z,
                                   String soundPath, float volume, float pitch) {
        if (delay <= 0) {
            playSoundDirectly(x, y, z, soundPath, volume, pitch);
        } else {
            synchronized (DELAYED_SOUNDS) {
                DELAYED_SOUNDS.add(new DelayedSound(delay, x, y, z, soundPath, volume, pitch));
            }
        }
    }

    private static void playSoundDirectly(double x, double y, double z,
                                          String soundPath, float volume, float pitch) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        SoundEvent event = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(soundPath));
        if (event != null) {
            level.playLocalSound(x, y, z, event, SoundSource.BLOCKS, volume, pitch, false);
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().level == null) {
            synchronized (DELAYED_SOUNDS) { DELAYED_SOUNDS.clear(); }
            return;
        }
        synchronized (DELAYED_SOUNDS) {
            Iterator<DelayedSound> it = DELAYED_SOUNDS.iterator();
            while (it.hasNext()) {
                DelayedSound s = it.next();
                if (--s.ticksRemaining <= 0) {
                    playSoundDirectly(s.x, s.y, s.z, s.soundPath, s.volume, s.pitch);
                    it.remove();
                }
            }
        }
    }
}