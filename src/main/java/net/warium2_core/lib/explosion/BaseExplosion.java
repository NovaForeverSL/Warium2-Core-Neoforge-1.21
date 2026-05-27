package net.warium2_core.lib.explosion;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BaseExplosion {

    private static final ExplosionDamageCalculator DEFAULT_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();

    private static final float RAYS_PER_SURFACE_BLOCK = 6.0f;
    private static final int   MIN_RAY_COUNT          = 128;
    private static final float STEP_SIZE              = 0.3f;
    private static final float BASE_ATTENUATION       = 0.225f;

    private final Level level;
    private final double x, y, z;
    private final float radius;
    private final boolean fire;
    private final Explosion.BlockInteraction blockInteraction;
    private final DamageSource damageSource;
    private final ExplosionDamageCalculator damageCalculator;
    private final ParticleOptions smallExplosionParticles;
    private final ParticleOptions largeExplosionParticles;
    private final Holder<SoundEvent> explosionSound;

    @Nullable private final Entity source;

    private final ObjectArrayList<BlockPos> toBlow = new ObjectArrayList<>();

    private final Map<Player, Vec3> hitPlayers = Maps.newHashMap();

    private final Explosion vanillaHandle;

    public BaseExplosion(
            Level level,
            @Nullable Entity source,
            @Nullable DamageSource damageSource,
            @Nullable ExplosionDamageCalculator damageCalculator,
            double x, double y, double z,
            float radius,
            boolean fire,
            Explosion.BlockInteraction blockInteraction,
            ParticleOptions smallExplosionParticles,
            ParticleOptions largeExplosionParticles,
            Holder<SoundEvent> explosionSound
    ) {
        this.level                  = level;
        this.source                 = source;
        this.x                      = x;
        this.y                      = y;
        this.z                      = z;
        this.radius                 = radius;
        this.fire                   = fire;
        this.blockInteraction       = blockInteraction;
        this.damageSource           = damageSource != null ? damageSource
                : level.damageSources().explosion(source, getIndirectSourceEntity(source));
        this.damageCalculator       = damageCalculator != null ? damageCalculator
                : makeDamageCalculator(source);
        this.smallExplosionParticles = smallExplosionParticles;
        this.largeExplosionParticles = largeExplosionParticles;
        this.explosionSound         = explosionSound;
        this.vanillaHandle          = new Explosion(
                level, source, this.damageSource, this.damageCalculator,
                x, y, z, radius, fire, blockInteraction,
                smallExplosionParticles, largeExplosionParticles, explosionSound
        );
    }

    public BaseExplosion(
            Level level,
            @Nullable Entity source,
            double x, double y, double z,
            float radius,
            boolean fire,
            Explosion.BlockInteraction blockInteraction
    ) {
        this(
                level, source,
                null, null,
                x, y, z, radius, fire, blockInteraction,
                ParticleTypes.EXPLOSION,
                ParticleTypes.EXPLOSION_EMITTER,
                SoundEvents.GENERIC_EXPLODE
        );
    }

    public void explode() {
        this.level.gameEvent(this.source, GameEvent.EXPLODE, new Vec3(this.x, this.y, this.z));

        Set<BlockPos>             toDestroy  = new HashSet<>();
        Map<BlockPos, BlockState> stateCache = new HashMap<>();
        Map<BlockPos, FluidState> fluidCache = new HashMap<>();

        int    rayCount   = rayCount(this.radius);
        double goldenAngle = Math.PI * (3.0 - Math.sqrt(5.0));

        for (int i = 0; i < rayCount; i++) {
            // Fibonacci sphere
            double ny    = 1.0 - (i / (double)(rayCount - 1)) * 2.0;
            double nr    = Math.sqrt(Math.max(0.0, 1.0 - ny * ny));
            double theta = goldenAngle * i;
            double dx    = Math.cos(theta) * nr;
            double dz    = Math.sin(theta) * nr;

            float  power = this.radius * (0.7f + this.level.random.nextFloat() * 0.6f);
            double cx    = this.x;
            double cy    = this.y;
            double cz    = this.z;

            while (power > 0.0f) {
                BlockPos pos = BlockPos.containing(cx, cy, cz);
                if (!this.level.isInWorldBounds(pos)) break;

                BlockState state = stateCache.computeIfAbsent(pos, this.level::getBlockState);
                FluidState fluid = fluidCache.computeIfAbsent(pos, this.level::getFluidState);

                Optional<Float> resistance =
                        this.damageCalculator.getBlockExplosionResistance(this.vanillaHandle, this.level, pos, state, fluid);

                if (resistance.isPresent()) {
                    power -= (resistance.get() + 0.3f) * 0.3f;
                }

                if (power > 0.0f
                        && this.damageCalculator.shouldBlockExplode(this.vanillaHandle, this.level, pos, state, power)) {
                    toDestroy.add(pos);
                }

                cx    += dx * STEP_SIZE;
                cy    += ny * STEP_SIZE;
                cz    += dz * STEP_SIZE;
                power -= BASE_ATTENUATION;
            }
        }

        this.toBlow.addAll(toDestroy);

        float  diameter = this.radius * 2.0f;
        int    minX     = Mth.floor(this.x - diameter - 1.0);
        int    maxX     = Mth.floor(this.x + diameter + 1.0);
        int    minY     = Mth.floor(this.y - diameter - 1.0);
        int    maxY     = Mth.floor(this.y + diameter + 1.0);
        int    minZ     = Mth.floor(this.z - diameter - 1.0);
        int    maxZ     = Mth.floor(this.z + diameter + 1.0);
        List<Entity> entities = this.level.getEntities(
                this.source, new AABB(minX, minY, minZ, maxX, maxY, maxZ));

        EventHooks.onExplosionDetonate(this.level, this.vanillaHandle, entities, diameter);

        Vec3 center = new Vec3(this.x, this.y, this.z);

        for (Entity entity : entities) {
            if (entity.ignoreExplosion(this.vanillaHandle)) continue;

            double dist = Math.sqrt(entity.distanceToSqr(center)) / diameter;
            if (dist > 1.0) continue;

            double ex = entity.getX() - this.x;
            double ey = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
            double ez = entity.getZ() - this.z;
            double len = Math.sqrt(ex * ex + ey * ey + ez * ez);
            if (len == 0.0) continue;

            ex /= len; ey /= len; ez /= len;

            if (this.damageCalculator.shouldDamageEntity(this.vanillaHandle, entity)) {
                entity.hurt(this.damageSource,
                        this.damageCalculator.getEntityDamageAmount(this.vanillaHandle, entity));
            }

            double seenPercent = Explosion.getSeenPercent(center, entity);
            double knockback   = (1.0 - dist) * seenPercent
                    * this.damageCalculator.getKnockbackMultiplier(entity);

            if (entity instanceof LivingEntity living) {
                knockback *= 1.0 - living.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE);
            }

            Vec3 impulse = new Vec3(ex * knockback, ey * knockback, ez * knockback);
            impulse = EventHooks.getExplosionKnockback(this.level, this.vanillaHandle, entity, impulse);
            entity.setDeltaMovement(entity.getDeltaMovement().add(impulse));

            if (entity instanceof Player player
                    && !player.isSpectator()
                    && (!player.isCreative() || !player.getAbilities().flying)) {
                this.hitPlayers.put(player, impulse);
            }

            entity.onExplosionHit(this.source);
        }
    }

    public void finalizeExplosion(boolean spawnParticles) {
        if (this.level.isClientSide) {
            this.level.playLocalSound(
                    this.x, this.y, this.z,
                    this.explosionSound.value(),
                    SoundSource.BLOCKS,
                    4.0f,
                    (1.0f + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2f) * 0.7f,
                    false
            );
        }

        boolean interacts = this.blockInteraction != Explosion.BlockInteraction.KEEP;

        if (spawnParticles) {
            ParticleOptions particle = (this.radius >= 2.0f && interacts)
                    ? this.largeExplosionParticles
                    : this.smallExplosionParticles;
            this.level.addParticle(particle, this.x, this.y, this.z, 1.0, 0.0, 0.0);
        }

        if (interacts) {
            this.level.getProfiler().push("explosion_blocks");
            List<Pair<ItemStack, BlockPos>> drops = new ArrayList<>();
            Util.shuffle(this.toBlow, this.level.random);

            for (BlockPos pos : this.toBlow) {
                this.level.getBlockState(pos)
                        .onExplosionHit(this.level, pos, this.vanillaHandle,
                                (stack, dropPos) -> addOrAppendStack(drops, stack, dropPos));
            }

            for (Pair<ItemStack, BlockPos> pair : drops) {
                Block.popResource(this.level, pair.getSecond(), pair.getFirst());
            }

            this.level.getProfiler().pop();
        }

        if (this.fire) {
            for (BlockPos pos : this.toBlow) {
                if (this.level.random.nextInt(3) == 0
                        && this.level.getBlockState(pos).isAir()
                        && this.level.getBlockState(pos.below()).isSolidRender(this.level, pos.below())) {
                    this.level.setBlockAndUpdate(pos, BaseFireBlock.getState(this.level, pos));
                }
            }
        }
    }

    private static int rayCount(float radius) {
        int scaled = (int)(RAYS_PER_SURFACE_BLOCK * 4.0f * (float)Math.PI * radius * radius);
        return Math.max(MIN_RAY_COUNT, scaled);
    }

    private static void addOrAppendStack(List<Pair<ItemStack, BlockPos>> drops, ItemStack stack, BlockPos pos) {
        for (int i = 0; i < drops.size(); i++) {
            Pair<ItemStack, BlockPos> pair = drops.get(i);
            ItemStack existing = pair.getFirst();
            if (ItemEntity.areMergable(existing, stack)) {
                drops.set(i, Pair.of(ItemEntity.merge(existing, stack, 16), pair.getSecond()));
                if (stack.isEmpty()) return;
            }
        }
        drops.add(Pair.of(stack, pos));
    }

    private ExplosionDamageCalculator makeDamageCalculator(@Nullable Entity entity) {
        return entity == null ? DEFAULT_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(entity);
    }

    @Nullable
    private static LivingEntity getIndirectSourceEntity(@Nullable Entity source) {
        if (source == null)               return null;
        if (source instanceof PrimedTnt t) return t.getOwner();
        if (source instanceof LivingEntity l) return l;
        if (source instanceof net.minecraft.world.entity.projectile.Projectile p) {
            Entity owner = p.getOwner();
            if (owner instanceof LivingEntity l) return l;
        }
        return null;
    }


    public List<BlockPos>       getToBlow()        { return this.toBlow; }
    public Map<Player, Vec3>    getHitPlayers()    { return this.hitPlayers; }
    public float                radius()           { return this.radius; }
    public Vec3                 center()           { return new Vec3(this.x, this.y, this.z); }
    @Nullable public Entity     getDirectSourceEntity()   { return this.source; }
    @Nullable public LivingEntity getIndirectSourceEntity() { return getIndirectSourceEntity(this.source); }
    public Explosion.BlockInteraction getBlockInteraction() { return this.blockInteraction; }
    public boolean              interactsWithBlocks() { return this.blockInteraction != Explosion.BlockInteraction.KEEP; }
}