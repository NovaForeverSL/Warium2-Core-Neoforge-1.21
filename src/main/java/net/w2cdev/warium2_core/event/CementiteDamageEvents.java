package net.w2cdev.warium2_core.event;

import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.w2cdev.warium2_core.Warium2_Core;
import net.w2cdev.warium2_core.block.custom.DamageableCementiteBlock;

@EventBusSubscriber(modid = Warium2_Core.MODID)
public final class CementiteDamageEvents {
    private static final int ARROW_DAMAGE = 1;
    private static final int EXPLOSION_DAMAGE = 2;

    private CementiteDamageEvents() {
    }

    @SubscribeEvent
    public static void onProjectileImpact(final ProjectileImpactEvent event) {
        if (!(event.getProjectile().level() instanceof ServerLevel level)) {
            return;
        }
        if (!(event.getRayTraceResult() instanceof BlockHitResult hitResult)) {
            return;
        }
        if (!(event.getProjectile() instanceof AbstractArrow)) {
            return;
        }

        damageBlock(level, hitResult.getBlockPos(), ARROW_DAMAGE);
    }

    @SubscribeEvent
    public static void onExplosionDetonate(final ExplosionEvent.Detonate event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        final Iterator<BlockPos> iterator = event.getAffectedBlocks().iterator();
        while (iterator.hasNext()) {
            final BlockPos pos = iterator.next();
            if (!damageBlock(level, pos, EXPLOSION_DAMAGE)) {
                continue;
            }
            iterator.remove();
        }
    }

    private static boolean damageBlock(final ServerLevel level, final BlockPos pos, final int amount) {
        final BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof DamageableCementiteBlock damageableBlock)) {
            return false;
        }

        damageableBlock.applyDamage(level, pos, state, amount);
        return true;
    }
}
