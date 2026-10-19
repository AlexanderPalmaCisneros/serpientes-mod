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
    // [A AJUSTAR] provisional values: speed is far above Speed V, strength is a bit above Strength II
    public static final double SPEED_BONUS = 1.5;      // +150% movement speed (vanilla Speed I = 0.2, III = 0.6, V = 1.0)
    public static final double STRENGTH_BONUS = 7.5;   // +7.5 attack damage (vanilla Strength I = 3, II = 6)

    // [A AJUSTAR] In the air vanilla loses 9% of the horizontal speed every tick (factor 0.91) and the
    // speed effect does not help. Multiplying by this each tick cancels most of that loss while jumping.
    // 1.0 = vanilla behavior, 1.0989 (= 1/0.91) = keeps the speed completely
    public static final double AIR_MOMENTUM_FACTOR = 1.08;

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
