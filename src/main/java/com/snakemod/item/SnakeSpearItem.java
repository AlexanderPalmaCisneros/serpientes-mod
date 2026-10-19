package com.snakemod.item;

import com.snakemod.ModEffects;
import com.snakemod.entity.SnakeSpearEntity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

/**
 * Throwable spear with a fang tip. Hold right click to charge it, release to throw it.
 * It poisons with the snake venom, both thrown and in melee.
 */
public class SnakeSpearItem extends Item {
    // [A AJUSTAR] provisional balance values, change them here only
    public static final float THROWN_DAMAGE = 6.0F;        // damage of the thrown spear (trident = 8)
    public static final float MELEE_DAMAGE = 5.0F;         // damage when used as a melee weapon
    public static final float ATTACK_SPEED = -2.9F;        // same as the trident: slow
    public static final float SHOOT_POWER = 2.5F;          // speed of the throw (trident = 2.5)
    public static final int MIN_CHARGE_TICKS = 10;         // you must hold at least this long to throw
    public static final int DURABILITY = 150;
    public static final int VENOM_DURATION_TICKS = 140;    // 7 seconds
    public static final int VENOM_AMPLIFIER = 0;

    public SnakeSpearItem(Properties properties) {
        super(properties);
    }

    // All the properties of the item in one place (the registry uses this)
    public static Properties createProperties() {
        return new Properties()
                .durability(DURABILITY)
                .attributes(createAttributes())
                .component(DataComponents.TOOL, TridentItem.createToolProperties());
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, MELEE_DAMAGE, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    // The raised-arm pose while charging (same as the trident)
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000; // very long: you can hold as long as you want
    }

    // Right click: start charging
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isTooDamagedToUse(stack)) {
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    // You release right click: throw the spear if you charged it enough
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) {
            return;
        }
        int chargedTicks = this.getUseDuration(stack, entity) - timeLeft;
        if (chargedTicks < MIN_CHARGE_TICKS || isTooDamagedToUse(stack)) {
            return;
        }
        if (!level.isClientSide) {
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(entity.getUsedItemHand()));
            SnakeSpearEntity spear = new SnakeSpearEntity(level, player, stack);
            spear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, SHOOT_POWER, 1.0F);
            if (player.hasInfiniteMaterials()) {
                spear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY; // in creative it is not lost, nor picked up
            }
            level.addFreshEntity(spear);
            level.playSound(null, spear, SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.hasInfiniteMaterials()) {
                player.getInventory().removeItem(stack); // the spear leaves your inventory
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    private static boolean isTooDamagedToUse(ItemStack stack) {
        return stack.getDamageValue() >= stack.getMaxDamage() - 1;
    }

    // Melee hit: poison too
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addEffect(new MobEffectInstance(ModEffects.SNAKE_VENOM, VENOM_DURATION_TICKS, VENOM_AMPLIFIER), attacker);
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}
