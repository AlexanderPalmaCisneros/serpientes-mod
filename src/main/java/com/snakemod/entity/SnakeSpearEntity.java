package com.snakemod.entity;

import com.snakemod.ModEffects;
import com.snakemod.ModEntities;
import com.snakemod.ModItems;
import com.snakemod.item.SnakeSpearItem;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * The spear while it is flying. Based on the vanilla trident (ThrownTrident) but without Loyalty:
 * it hits once, poisons with the snake venom and stays stuck so any player can pick it up.
 */
public class SnakeSpearEntity extends AbstractArrow {
    private boolean dealtDamage;

    // Used by Minecraft to create the entity (when loading the world or on the client)
    public SnakeSpearEntity(EntityType<? extends SnakeSpearEntity> type, Level level) {
        super(type, level);
    }

    // Used by the item when a player throws the spear
    public SnakeSpearEntity(Level level, LivingEntity shooter, ItemStack spearStack) {
        super(ModEntities.SNAKE_SPEAR.get(), shooter, level, spearStack, null);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        super.tick();
    }

    // After hitting something, it cannot hit again
    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        Entity owner = this.getOwner();
        DamageSource damageSource = this.damageSources().trident(this, owner == null ? this : owner);
        float damage = SnakeSpearItem.THROWN_DAMAGE;
        if (this.level() instanceof ServerLevel serverLevel) {
            damage = EnchantmentHelper.modifyDamage(serverLevel, this.getWeaponItem(), target, damageSource, damage);
        }

        this.dealtDamage = true;
        if (target.hurt(damageSource, damage)) {
            if (this.level() instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, target, damageSource, this.getWeaponItem());
            }
            if (target instanceof LivingEntity living) {
                this.doKnockback(living, damageSource);
                this.doPostHurtEffects(living); // applies the venom, see below
            }
        }

        // Bounce back a little, like the trident
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
    }

    // Called after the spear hurts a living entity: poison it with our own venom
    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        target.addEffect(new MobEffectInstance(ModEffects.SNAKE_VENOM,
                SnakeSpearItem.VENOM_DURATION_TICKS, SnakeSpearItem.VENOM_AMPLIFIER), this.getOwner());
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    // What you get back when you pick it up (if the spear was lost without a stack)
    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.SNAKE_SPEAR.get());
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}
