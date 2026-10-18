package com.snakemod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

/**
 * Snake venom: damage over time that works on EVERY living entity.
 * Vanilla Poison is blocked by undead mobs (zombies, skeletons...), this one is not.
 */
public class SnakeVenomEffect extends MobEffect {
    // [A AJUSTAR] provisional balance values, change them here only
    public static final float DAMAGE = 1.0F;            // damage dealt on each pulse
    public static final int BASE_INTERVAL_TICKS = 25;   // ticks between pulses at level I (20 ticks = 1 second)
    public static final boolean CAN_KILL = false;       // false = like vanilla Poison, it never takes the last half heart

    public SnakeVenomEffect() {
        super(MobEffectCategory.HARMFUL, 0x6CA832); // color of the particles and the potion
    }

    // Called on every tick the effect is active, but only does something when the check below says so
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int interval = BASE_INTERVAL_TICKS >> amplifier; // higher level = faster pulses
        return interval > 0 ? duration % interval == 0 : true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (CAN_KILL || entity.getHealth() > DAMAGE) {
            entity.hurt(entity.damageSources().magic(), DAMAGE);
        }
        return true; // true = keep the effect running
    }
}
