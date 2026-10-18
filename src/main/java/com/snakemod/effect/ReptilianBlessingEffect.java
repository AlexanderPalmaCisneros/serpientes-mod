package com.snakemod.effect;

import com.snakemod.SnakeMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * Reptilian blessing: gives speed and strength.
 * It has no code that runs each tick: it only adds attribute modifiers, and Minecraft applies and removes them.
 */
public class ReptilianBlessingEffect extends MobEffect {
    // [A AJUSTAR] provisional values: speed is a bit stronger than Speed III, strength is Strength I + 50%
    public static final double SPEED_BONUS = 0.7;      // +70% movement speed (vanilla Speed I = 0.2, III = 0.6)
    public static final double STRENGTH_BONUS = 4.5;   // +4.5 attack damage (vanilla Strength I = 3, II = 6)

    public ReptilianBlessingEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xC8A82A);
        // The id identifies each modifier so Minecraft can remove exactly this one when the effect ends
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath(SnakeMod.MODID, "effect.reptilian_blessing_speed"),
                SPEED_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE,
                ResourceLocation.fromNamespaceAndPath(SnakeMod.MODID, "effect.reptilian_blessing_strength"),
                STRENGTH_BONUS, AttributeModifier.Operation.ADD_VALUE);
    }
}
