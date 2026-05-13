package net.warium2_core.lib.projectile.vpl.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.warium2_core.lib.projectile.vpl.base.SimpleProjectile;

public class SimpleBullet extends SimpleProjectile {
    public SimpleBullet(EntityType<? extends SimpleBullet> type, Level level) { super(type, level); }

    @Override
    protected void onHitEntity(Entity target) {
        target.hurt(damageSources().mobProjectile(this, null), 2.0f);
    }
}
