package net.warium2_core.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warium2_core.lib.projectile.vpl.entity.Entitys;
import net.warium2_core.lib.projectile.vpl.entity.SimpleBullet;

public class TestGun extends Item {

    public TestGun(Properties properties) {
        super(properties.stacksTo(1).durability(500));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        Vec3 lookVec  = player.getLookAngle();
        Vec3 velocity = lookVec.scale(2.5);
        Vec3 eyePos   = player.getEyePosition(1.0f);
        Vec3 spawnPos = eyePos.add(lookVec);

        if (!level.isClientSide) {
            SimpleBullet bullet = new SimpleBullet(Entitys.BULLET.get(), level);
            bullet.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
            bullet.setDeltaMovement(velocity);

            level.addFreshEntity(bullet);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.5f, 1.5f);

            player.getCooldowns().addCooldown(this, 10);
            itemStack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(itemStack));
        }

        return InteractionResultHolder.success(itemStack);
    }
}