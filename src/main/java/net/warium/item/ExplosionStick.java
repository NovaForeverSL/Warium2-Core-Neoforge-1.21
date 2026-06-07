package net.warium.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.warium.explosions.ScaledExplosion;

public class ExplosionStick extends Item {

    private static final float MIN_RADIUS = 2.0f;
    private static final float MAX_RADIUS = 45.0f;

    public ExplosionStick(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        Vec3 eyePos  = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();

        Vec3 farPos = eyePos.add(lookVec.scale(1024.0));

        BlockHitResult hit = level.clip(new ClipContext(
                eyePos, farPos,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        ));

        if (hit.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        Vec3 pos = hit.getLocation();

        float radius = 18; //MIN_RADIUS + level.random.nextFloat() * (MAX_RADIUS - MIN_RADIUS);

        ScaledExplosion explosion = new ScaledExplosion(
                level, player,
                pos.x, pos.y, pos.z,
                radius,
                1.25f, 0.75f, 1.25f,
                true,
                Explosion.BlockInteraction.DESTROY
        );

        if (!level.isClientSide()) {
            explosion.explode();
        }

        explosion.finalizeExplosion(true, true, false);

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}