package net.warium.lib.explosion;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class ExampleExplosion extends BaseExplosion {

    public ExampleExplosion(
            Level level,
            @Nullable Entity source,
            double x, double y, double z,
            float radius,
            boolean fire,
            Explosion.BlockInteraction blockInteraction
    ) {
        super(level, source, x, y, z, radius, fire, blockInteraction);
    }

    public ExampleExplosion(
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

    public ExampleExplosion(
            Level level,
            @Nullable Entity source,
            @Nullable DamageSource damageSource,
            @Nullable ExplosionDamageCalculator damageCalculator,
            double x, double y, double z,
            float radius,
            float scaleX, float scaleY, float scaleZ,
            boolean fire,
            Explosion.BlockInteraction blockInteraction
    ) {
        super(level, source, damageSource, damageCalculator,
                x, y, z, radius, scaleX, scaleY, scaleZ,
                fire, blockInteraction,
                net.minecraft.core.particles.ParticleTypes.EXPLOSION,
                net.minecraft.core.particles.ParticleTypes.EXPLOSION_EMITTER,
                net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE);
    }

    @Override
    protected void onExplodeStart() {
        super.onExplodeStart();
    }

    @Override
    protected float onRayStartPower(int rayIndex) {
        return super.onRayStartPower(rayIndex);
    }

    @Override
    protected boolean onBlockHit(BlockPos pos, BlockState state, float rayPower) {
        return super.onBlockHit(pos, state, rayPower);
    }

    @Override
    protected void onBlockMarkedForDestruction(BlockPos pos) {
        super.onBlockMarkedForDestruction(pos);
    }

    @Override
    protected void onFillInterior(LongOpenHashSet destroySet) {
        super.onFillInterior(destroySet);
    }

    @Override
    protected boolean onEntityHit(Entity entity, double seenPct, Vec3 impulse) {
        return super.onEntityHit(entity, seenPct, impulse);
    }

    @Override
    protected void onExplodeEnd(LongOpenHashSet destroySet) {
        super.onExplodeEnd(destroySet);
    }

    @Override
    protected void onBlocksFinalized(List<BlockPos> blown) {
        super.onBlocksFinalized(blown);
    }

    @Override
    protected void onFirePlaced(BlockPos pos) {
        super.onFirePlaced(pos);
    }




    @Override
    public void explode(){
        super.explode();
    }

    @Override
    public void finalizeExplosion(boolean spawnParticles, boolean playSound, boolean spawnDrops){
        super.finalizeExplosion(spawnParticles, playSound, spawnDrops);
    }

}