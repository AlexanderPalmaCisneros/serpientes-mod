package com.snakemod.effect;

import com.snakemod.ModEffects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * Antidote: while active, it cures snake venom and vanilla Poison every tick.
 * The immunity (blocking new venom/poison) is in ModEvents.
 */
public class AntidoteEffect extends MobEffect {
    public AntidoteEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xE8E8C8);
    }

    // Runs on every tick the effect is active
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        entity.removeEffect(ModEffects.SNAKE_VENOM);
        entity.removeEffect(MobEffects.POISON);
        return true;
    }
}
