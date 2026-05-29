package net.warium2_core.lib.explosion;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.entity.projectile.Projectile;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

public class BaseExplosion {

    private static final ExplosionDamageCalculator DEFAULT_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();
    private static final float RAYS_PER_SURFACE_BLOCK = 2.0f;
    private static final int   MIN_RAY_COUNT          = 32;
    private static final float STEP_SIZE              = 0.3f;
    private static final float BASE_ATTENUATION       = 0.175f;

    private static final int[] DIR_STEP_X = new int[6];
    private static final int[] DIR_STEP_Y = new int[6];
    private static final int[] DIR_STEP_Z = new int[6];

    static {
        Direction[] dirs = Direction.values();
        for (int i = 0; i < dirs.length; i++) {
            DIR_STEP_X[i] = dirs[i].getStepX();
            DIR_STEP_Y[i] = dirs[i].getStepY();
            DIR_STEP_Z[i] = dirs[i].getStepZ();
        }
    }

    private static final class Handle extends Explosion {

        private final ObjectArrayList<BlockPos> sharedToBlow;

        Handle(
                Level level,
                @Nullable Entity source,
                @Nullable DamageSource damageSource,
                @Nullable ExplosionDamageCalculator damageCalculator,
                double x, double y, double z,
                float radius, boolean fire,
                Explosion.BlockInteraction blockInteraction,
                ParticleOptions small, ParticleOptions large,
                Holder<SoundEvent> sound,
                ObjectArrayList<BlockPos> sharedToBlow
        ) {
            super(level, source, damageSource, damageCalculator,
                    x, y, z, radius, fire, blockInteraction, small, large, sound);
            this.sharedToBlow = sharedToBlow;
        }

        @Override public List<BlockPos> getToBlow() { return sharedToBlow; }
        @Override public void clearToBlow()          { sharedToBlow.clear(); }
    }

    protected final Level    level;
    protected final double   x, y, z;
    protected final float    radius;
    protected final boolean  fire;
    protected final Explosion.BlockInteraction blockInteraction;
    protected final DamageSource               damageSource;
    protected final ExplosionDamageCalculator  damageCalculator;
    protected final ParticleOptions smallExplosionParticles;
    protected final ParticleOptions largeExplosionParticles;
    protected final Holder<SoundEvent>         explosionSound;
    protected final float scaleX, scaleY, scaleZ;

    protected final ObjectArrayList<BlockPos> toBlow     = new ObjectArrayList<>();
    protected final Map<Player, Vec3>         hitPlayers = Maps.newHashMap();

    protected final Handle handle;

    @Nullable protected final Entity source;

    public BaseExplosion(
            Level level,
            @Nullable Entity source,
            @Nullable DamageSource damageSource,
            @Nullable ExplosionDamageCalculator damageCalculator,
            double x, double y, double z,
            float radius,
            float scaleX, float scaleY, float scaleZ,
            boolean fire,
            Explosion.BlockInteraction blockInteraction,
            ParticleOptions smallExplosionParticles,
            ParticleOptions largeExplosionParticles,
            Holder<SoundEvent> explosionSound
    ) {
        this.level  = level;
        this.source = source;
        this.x = x; this.y = y; this.z = z;
        this.radius = radius;
        this.scaleX = scaleX; this.scaleY = scaleY; this.scaleZ = scaleZ;
        this.fire             = fire;
        this.blockInteraction = blockInteraction;
        this.damageSource = damageSource != null ? damageSource
                : level.damageSources().explosion(source, getIndirectSourceEntityStatic(source));
        this.damageCalculator = damageCalculator != null ? damageCalculator
                : makeDamageCalculator(source);
        this.smallExplosionParticles = smallExplosionParticles;
        this.largeExplosionParticles = largeExplosionParticles;
        this.explosionSound          = explosionSound;
        this.handle = new Handle(
                level, source, this.damageSource, this.damageCalculator,
                x, y, z, radius, fire, blockInteraction,
                smallExplosionParticles, largeExplosionParticles, explosionSound,
                this.toBlow
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
        this(level, source, null, null, x, y, z, radius,
                1.0f, 1.0f, 1.0f,
                fire, blockInteraction,
                ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER,
                SoundEvents.GENERIC_EXPLODE);
    }

    public BaseExplosion(
            Level level,
            @Nullable Entity source,
            double x, double y, double z,
            float radius,
            float scaleX, float scaleY, float scaleZ,
            boolean fire,
            Explosion.BlockInteraction blockInteraction
    ) {
        this(level, source, null, null, x, y, z, radius,
                scaleX, scaleY, scaleZ,
                fire, blockInteraction,
                ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER,
                SoundEvents.GENERIC_EXPLODE);
    }



    protected void onExplodeStart() {}

    protected float onRayStartPower(int rayIndex) {
        float maxScale = Math.max(this.scaleX, Math.max(this.scaleY, this.scaleZ));
        return this.radius * maxScale * (0.7f + this.level.random.nextFloat() * 0.6f);
    }

    protected boolean onBlockHit(BlockPos pos, BlockState state, float rayPower) {
        return true;
    }

    protected void onBlockMarkedForDestruction(BlockPos pos) {}

    protected void onFillInterior(LongOpenHashSet destroySet) {}

    protected boolean onEntityHit(Entity entity, double seenPct, Vec3 impulse) {
        return true;
    }

    protected void onExplodeEnd(LongOpenHashSet destroySet) {}

    protected void onBlocksFinalized(List<BlockPos> blown) {}

    protected void onFirePlaced(BlockPos pos) {}



    public void explode() {
        onExplodeStart();

        this.level.gameEvent(this.source, GameEvent.EXPLODE, new Vec3(this.x, this.y, this.z));

        LongOpenHashSet toDestroyLongs = new LongOpenHashSet();
        Object2ObjectOpenHashMap<BlockPos, BlockState> stateCache = new Object2ObjectOpenHashMap<>();
        Object2ObjectOpenHashMap<BlockPos, FluidState> fluidCache = new Object2ObjectOpenHashMap<>();

        int    rayCount    = rayCount(this.radius, this.scaleX, this.scaleY, this.scaleZ);
        double goldenAngle = Math.PI * (3.0 - Math.sqrt(5.0));
        float  maxScale    = Math.max(this.scaleX, Math.max(this.scaleY, this.scaleZ));

        float extX = this.radius * this.scaleX;
        float extY = this.radius * this.scaleY;
        float extZ = this.radius * this.scaleZ;

        double invExtX = 1.0 / extX;
        double invExtY = 1.0 / extY;
        double invExtZ = 1.0 / extZ;

        for (int i = 0; i < rayCount; i++) {
            double ny  = 1.0 - (i / (double)(rayCount - 1)) * 2.0;
            double nr  = Math.sqrt(Math.max(0.0, 1.0 - ny * ny));
            double theta = goldenAngle * i;
            double dx  = Math.cos(theta) * nr;
            double dz  = Math.sin(theta) * nr;

            double stepX = dx * STEP_SIZE * this.scaleX;
            double stepY = ny * STEP_SIZE * this.scaleY;
            double stepZ = dz * STEP_SIZE * this.scaleZ;

            float  power   = onRayStartPower(i);
            double cx = this.x, cy = this.y, cz = this.z;

            BlockPos lastPos             = null;
            float    cachedResistance    = 0f;
            boolean  cachedShouldExplode = false;

            while (power > 0.0f) {
                BlockPos pos = BlockPos.containing(cx, cy, cz);
                if (!this.level.isInWorldBounds(pos)) break;

                if (!pos.equals(lastPos)) {
                    lastPos = pos;
                    BlockState state = stateCache.computeIfAbsent(pos, this.level::getBlockState);
                    FluidState fluid = fluidCache.computeIfAbsent(pos, this.level::getFluidState);

                    Optional<Float> resistance =
                            this.damageCalculator.getBlockExplosionResistance(this.handle, this.level, pos, state, fluid);

                    if (resistance.isPresent()) {
                        if (!onBlockHit(pos, state, power)) {
                            cachedResistance    = 0f;
                            cachedShouldExplode = false;
                        } else {
                            cachedResistance    = (resistance.get() + 0.3f) * 0.3f;
                            cachedShouldExplode = this.damageCalculator.shouldBlockExplode(this.handle, this.level, pos, state, power);
                        }
                    } else {
                        cachedResistance    = 0f;
                        cachedShouldExplode = false;
                    }
                }

                power -= cachedResistance;

                if (power > 0.0f && cachedShouldExplode) {
                    if (toDestroyLongs.add(lastPos.asLong())) {
                        onBlockMarkedForDestruction(lastPos);
                    }
                }

                cx += stepX;
                cy += stepY;
                cz += stepZ;
                power -= BASE_ATTENUATION;
            }
        }

        onFillInterior(toDestroyLongs);

        Queue<BlockPos> queue  = new ArrayDeque<>();
        BlockPos        origin = BlockPos.containing(this.x, this.y, this.z);
        queue.add(origin);
        toDestroyLongs.add(origin.asLong());

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            int curX = current.getX(), curY = current.getY(), curZ = current.getZ();

            for (int d = 0; d < 6; d++) {
                int nx = curX + DIR_STEP_X[d];
                int ny = curY + DIR_STEP_Y[d];
                int nz = curZ + DIR_STEP_Z[d];

                double fx = (nx + 0.5 - this.x) * invExtX;
                double fy = (ny + 0.5 - this.y) * invExtY;
                double fz = (nz + 0.5 - this.z) * invExtZ;
                if (fx * fx + fy * fy + fz * fz > 1.0) continue;

                BlockPos neighbor     = new BlockPos(nx, ny, nz);
                long     neighborLong = neighbor.asLong();
                if (toDestroyLongs.contains(neighborLong)) continue;

                BlockState state = stateCache.computeIfAbsent(neighbor, this.level::getBlockState);
                Optional<Float> resistance = this.damageCalculator.getBlockExplosionResistance(
                        this.handle, this.level, neighbor, state,
                        fluidCache.computeIfAbsent(neighbor, this.level::getFluidState));

                if (resistance.isPresent() && resistance.get() > 0) continue;

                toDestroyLongs.add(neighborLong);
                queue.add(neighbor);
            }
        }

        for (long packed : toDestroyLongs) {
            this.toBlow.add(BlockPos.of(packed));
        }

        int minX = Mth.floor(this.x - extX - 1.0), maxX = Mth.floor(this.x + extX + 1.0);
        int minY = Mth.floor(this.y - extY - 1.0), maxY = Mth.floor(this.y + extY + 1.0);
        int minZ = Mth.floor(this.z - extZ - 1.0), maxZ = Mth.floor(this.z + extZ + 1.0);

        List<Entity> entities = this.level.getEntities(
                this.source, new AABB(minX, minY, minZ, maxX, maxY, maxZ));

        float maxDiameter = Math.max(extX, Math.max(extY, extZ)) * 2.0f;
        EventHooks.onExplosionDetonate(this.level, this.handle, entities, maxDiameter);

        Vec3 center = new Vec3(this.x, this.y, this.z);

        for (Entity entity : entities) {
            if (entity.ignoreExplosion(this.handle)) continue;

            double ex = entity.getX() - this.x;
            double ey = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
            double ez = entity.getZ() - this.z;

            double lenSq = ex * ex + ey * ey + ez * ez;
            if (lenSq == 0.0) continue;

            double exs = ex * invExtX, eys = ey * invExtY, ezs = ez * invExtZ;
            double ellipsoidDistSq = exs * exs + eys * eys + ezs * ezs;
            if (ellipsoidDistSq > 1.0) continue;

            double ellipsoidDist = Math.sqrt(ellipsoidDistSq);
            double invLen = 1.0 / Math.sqrt(lenSq);
            double ndx = ex * invLen, ndy = ey * invLen, ndz = ez * invLen;

            double seenPercent = Explosion.getSeenPercent(center, entity);
            double knockback   = (1.0 - ellipsoidDist) * seenPercent
                    * this.damageCalculator.getKnockbackMultiplier(entity);

            if (entity instanceof LivingEntity living) {
                knockback *= 1.0 - living.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE);
            }

            Vec3 impulse = new Vec3(ndx * knockback, ndy * knockback, ndz * knockback);
            impulse = EventHooks.getExplosionKnockback(this.level, this.handle, entity, impulse);

            if (!onEntityHit(entity, seenPercent, impulse)) continue;

            if (this.damageCalculator.shouldDamageEntity(this.handle, entity)) {
                entity.hurt(this.damageSource,
                        this.damageCalculator.getEntityDamageAmount(this.handle, entity));
            }

            entity.setDeltaMovement(entity.getDeltaMovement().add(impulse));

            if (entity instanceof Player player
                    && !player.isSpectator()
                    && (!player.isCreative() || !player.getAbilities().flying)) {
                this.hitPlayers.put(player, impulse);
            }

            entity.onExplosionHit(this.source);
        }

        onExplodeEnd(toDestroyLongs);
    }

    @OnlyIn(Dist.CLIENT)
    public void playSound(boolean playSound){
        if (playSound && this.level.isClientSide) {
            this.level.playLocalSound(
                    this.x, this.y, this.z,
                    this.explosionSound.value(),
                    SoundSource.BLOCKS,
                    4.0f,
                    (1.0f + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2f) * 0.7f,
                    false
            );
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void spawnParticles(boolean spawnParticles, boolean interacts){
        if (spawnParticles) {
            ParticleOptions particle = (this.radius >= 2.0f && interacts)
                    ? this.largeExplosionParticles : this.smallExplosionParticles;
            this.level.addParticle(particle, this.x, this.y, this.z, 1.0, 0.0, 0.0);
        }
    }

    public void interact(boolean interacts, boolean spawnDrops){
        if (interacts) {
            this.level.getProfiler().push("explosion_blocks");

            if (spawnDrops) {
                List<Pair<ItemStack, BlockPos>> drops = new ArrayList<>();
                Util.shuffle(this.toBlow, this.level.random);

                for (BlockPos pos : this.toBlow) {
                    this.level.getBlockState(pos)
                            .onExplosionHit(this.level, pos, this.handle,
                                    (stack, dropPos) -> addOrAppendStack(drops, stack, dropPos));
                }

                for (Pair<ItemStack, BlockPos> pair : drops) {
                    Block.popResource(this.level, pair.getSecond(), pair.getFirst());
                }
            } else {
                for (BlockPos pos : this.toBlow) {
                    this.level.setBlock(pos,
                            net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),
                            Block.UPDATE_CLIENTS | Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_MOVE_BY_PISTON);
                }
            }

            this.level.getProfiler().pop();
        }
    }

    public void flames(){
        if (this.fire) {
            for (BlockPos pos : this.toBlow) {
                if (this.level.random.nextInt(3) == 0
                        && this.level.getBlockState(pos).isAir()
                        && this.level.getBlockState(pos.below()).isSolidRender(this.level, pos.below())) {
                    this.level.setBlockAndUpdate(pos, BaseFireBlock.getState(this.level, pos));
                    onFirePlaced(pos);
                }
            }
        }
    }

    public void finalizeExplosion(boolean spawnParticles, boolean playSound, boolean spawnDrops) {
        playSound(playSound);

        boolean interacts = this.blockInteraction != Explosion.BlockInteraction.KEEP;

        if (this.level.isClientSide) {
            spawnParticles(spawnParticles, interacts);
        }

        interact(interacts, spawnDrops);

        onBlocksFinalized(java.util.Collections.unmodifiableList(this.toBlow));

        flames();
    }

    private static int rayCount(float radius, float sx, float sy, float sz) {
        double p  = 1.6075;
        double ab = radius * sx * radius * sy;
        double ac = radius * sx * radius * sz;
        double bc = radius * sy * radius * sz;
        double area = 4.0 * Math.PI * Math.pow(
                (Math.pow(ab, p) + Math.pow(ac, p) + Math.pow(bc, p)) / 3.0,
                1.0 / p
        );
        return Math.max(MIN_RAY_COUNT, (int)(RAYS_PER_SURFACE_BLOCK * area));
    }

    private static void addOrAppendStack(
            List<Pair<ItemStack, BlockPos>> drops, ItemStack stack, BlockPos pos) {
        for (int i = 0; i < drops.size(); i++) {
            Pair<ItemStack, BlockPos> pair     = drops.get(i);
            ItemStack                  existing = pair.getFirst();
            if (ItemEntity.areMergable(existing, stack)) {
                drops.set(i, Pair.of(ItemEntity.merge(existing, stack, 16), pair.getSecond()));
                if (stack.isEmpty()) return;
            }
        }
        drops.add(Pair.of(stack, pos));
    }

    private ExplosionDamageCalculator makeDamageCalculator(@Nullable Entity entity) {
        return entity == null ? DEFAULT_DAMAGE_CALCULATOR
                : new EntityBasedExplosionDamageCalculator(entity);
    }

    private static @Nullable LivingEntity getIndirectSourceEntityStatic(@Nullable Entity source) {
        if (source == null)                 return null;
        if (source instanceof PrimedTnt t)  return t.getOwner();
        if (source instanceof LivingEntity l) return l;
        if (source instanceof Projectile p) {
            Entity owner = p.getOwner();
            if (owner instanceof LivingEntity l) return l;
        }
        return null;
    }

    public List<BlockPos>       getToBlow()                 { return this.toBlow; }
    public Map<Player, Vec3>    getHitPlayers()             { return this.hitPlayers; }
    public float                radius()                    { return this.radius; }
    public Vec3                 center()                    { return new Vec3(this.x, this.y, this.z); }
    public Explosion            asExplosion()               { return this.handle; }
    public Explosion.BlockInteraction getBlockInteraction() { return this.blockInteraction; }
    public boolean              interactsWithBlocks()       { return this.blockInteraction != Explosion.BlockInteraction.KEEP; }

    @Nullable public Entity        getDirectSourceEntity()  { return this.source; }
    @Nullable public LivingEntity  getIndirectSourceEntity(){ return getIndirectSourceEntityStatic(this.source); }
}