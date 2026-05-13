package net.warium2_core.lib.projectile.vpl.base;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.List;

public class SimpleProjectile extends Projectile {

    public static final double DEFAULT_AIR_DRAG         = 0.99;
    public static final double DEFAULT_GRAVITY          = 0.015;
    public static final double DEFAULT_COLLISION_MARGIN = 0.10;
    public static final int    DEFAULT_MAX_LIFETIME     = 1200;

    private int ticksAlive = 0;

    public SimpleProjectile(EntityType<? extends SimpleProjectile> type, Level level) {
        super(type, level);
        this.noCulling = true;
    }

    public SimpleProjectile(EntityType<? extends SimpleProjectile> type, Level level,
                            Vec3 position, Vec3 velocity) {
        this(type, level);
        this.setPos(position.x, position.y, position.z);
        this.setDeltaMovement(velocity);
        this.updateRotation();
        this.setNoGravity(true);
    }

    protected double getAirDrag()         { return DEFAULT_AIR_DRAG; }
    protected double getProjectileGravity()         { return DEFAULT_GRAVITY; }
    protected double getCollisionMargin() { return DEFAULT_COLLISION_MARGIN; }
    protected int    getMaxLifetime()     { return DEFAULT_MAX_LIFETIME; }

    protected void onHitEntity(Entity target) {}

    protected void onHitBlock(BlockHitResult hit) {
        this.discard();
    }

    protected void onTick() {}

    @Override
    public final void tick() {
        this.baseTick();

        if (++ticksAlive > getMaxLifetime()) {
            this.discard();
            return;
        }

        Vec3 motion     = this.getDeltaMovement();
        Vec3 currentPos = this.position();
        Vec3 nextPos    = currentPos.add(motion);

        if (!this.level().isClientSide) {
            BlockHitResult blockHit = this.level().clip(new ClipContext(
                    currentPos, nextPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    this
            ));

            if (blockHit.getType() != HitResult.Type.MISS) {
                onHitBlock(blockHit);
                return;
            }

            AABB searchBox = buildSearchBox(currentPos, nextPos);
            List<Entity> nearby = this.level().getEntities(
                    this, searchBox,
                    e -> e.isAlive() && e.isPickable()
            );

            if (!nearby.isEmpty()) {
                Entity target = closestIntersecting(nearby, searchBox);
                if (target != null) {
                    onHitEntity(target);
                    this.discard();
                    return;
                }
            }
        }

        this.setPos(nextPos.x, nextPos.y, nextPos.z);

        this.setDeltaMovement(
                motion.x * getAirDrag(),
                motion.y - getProjectileGravity(),
                motion.z * getAirDrag()
        );

        this.updateRotation();
        onTick();
    }

    private AABB buildSearchBox(Vec3 from, Vec3 to) {
        double margin = getCollisionMargin();
        return new AABB(
                Math.min(from.x, to.x) - margin,
                Math.min(from.y, to.y) - margin,
                Math.min(from.z, to.z) - margin,
                Math.max(from.x, to.x) + margin,
                Math.max(from.y, to.y) + margin,
                Math.max(from.z, to.z) + margin
        );
    }

    private Entity closestIntersecting(List<Entity> candidates, AABB searchBox) {
        Entity best     = null;
        double bestDist = Double.MAX_VALUE;
        Vec3   origin   = this.position();

        for (Entity e : candidates) {
            if (!searchBox.intersects(e.getBoundingBox())) continue;
            double dist = e.position().distanceToSqr(origin);
            if (dist < bestDist) {
                bestDist = dist;
                best     = e;
            }
        }
        return best;
    }

    @Override
    protected void updateRotation() {
        Vec3   motion = this.getDeltaMovement();
        double hDist  = motion.horizontalDistance();
        if (hDist > 0.001) {
            this.setYRot(lerpRotation(this.yRotO, (float) (Mth.atan2(motion.x, motion.z) * Mth.RAD_TO_DEG)));
            this.setXRot(lerpRotation(this.xRotO, (float) (Mth.atan2(motion.y, hDist)   * Mth.RAD_TO_DEG)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity);
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.setDeltaMovement(packet.getXa(), packet.getYa(), packet.getZa());
        this.updateRotation();
        this.ticksAlive = 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.ticksAlive = tag.getInt("Age");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.ticksAlive);
    }

    @Override public void    checkDespawn()                              {}
    @Override public boolean shouldRenderAtSqrDistance(double distance) { return true; }
    @Override public boolean shouldBeSaved()                            { return false; }
    @Override public boolean isPickable()                               { return false; }
    @Override public boolean isPushable()                               { return false; }
    @Override public boolean canBeCollidedWith()                        { return false; }

    protected float getEyeHeight(Pose pose, EntityDimensions dimensions) { return 0.0f; }

    public int getTicksAlive() { return ticksAlive; }
}