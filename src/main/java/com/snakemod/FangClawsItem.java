package com.snakemod;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;

/**
 * Brass-knuckle weapon with two fangs. Poisons whatever it hits.
 * Extends TieredItem (not SwordItem) so it is a pure damage weapon: no cobweb cutting, no sweeping.
 */
public class FangClawsItem extends TieredItem {
    // [A AJUSTAR] provisional balance values, change them here only
    public static final float ATTACK_DAMAGE = 4.0F;          // base damage (a sword is 3 + material bonus)
    public static final float ATTACK_SPEED = -2.0F;          // added to the default 4.0 (sword is -2.4, so this is slightly faster)
    public static final int VENOM_DURATION_TICKS = 100;      // 20 ticks = 1 second, so 5 seconds
    public static final int VENOM_AMPLIFIER = 0;             // 0 = level I

    public FangClawsItem(Tier tier, Properties properties) {
        // Reuses the same helper swords use to build the damage/speed attributes
        super(tier, properties.attributes(
                SwordItem.createAttributes(tier, ATTACK_DAMAGE, ATTACK_SPEED)));
    }

    // Called when the weapon hits a living entity
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Own venom (not vanilla Poison) so it also works on undead mobs
        target.addEffect(new MobEffectInstance(ModEffects.SNAKE_VENOM, VENOM_DURATION_TICKS, VENOM_AMPLIFIER), attacker);
        return true;
    }

    // Wears the weapon down by 1 use per hit
    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }
}
