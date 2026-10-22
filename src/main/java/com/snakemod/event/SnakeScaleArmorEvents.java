package com.snakemod.event;

import com.snakemod.ModEffects;
import com.snakemod.ModItems;
import com.snakemod.SnakeMod;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * Set bonus of the snake scale armor (chestplate + leggings + boots, the helmet does not count):
 * below 3.5 hearts the player gets the reptilian blessing. It lasts while the health stays low,
 * and 3 more seconds after going back above.
 */
@EventBusSubscriber(modid = SnakeMod.MODID)
public class SnakeScaleArmorEvents {
    // Fixed threshold (decided): 3.5 hearts = 7 health points, whatever the max health is.
    // Only real health counts, not the golden absorption hearts.
    public static final float LOW_HEALTH = 7.0F;
    // [A AJUSTAR] how long the blessing lasts after the health goes back above the threshold
    public static final int GRACE_TICKS = 60; // 3 seconds
    // [A AJUSTAR] blessing level: 0 = level I, the same as the potion
    public static final int BLESSING_AMPLIFIER = 0;
    // The effect is refreshed when it has less than this left, instead of every tick (fewer packets to the client)
    private static final int REFRESH_BELOW = GRACE_TICKS - 10;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        // The server decides the effects; the client only receives them
        if (player.level().isClientSide() || !hasFullSet(player) || player.getHealth() >= LOW_HEALTH) {
            return;
        }
        // Like a battery that is recharged while you are in danger: while the health is low the effect is
        // topped up to 3 seconds; once you heal, nobody recharges it and it runs out by itself 3 seconds later
        MobEffectInstance current = player.getEffect(ModEffects.REPTILIAN_BLESSING);
        if (current == null || current.getDuration() < REFRESH_BELOW) {
            // ambient = true and visible = false: no swirling particles, but the icon is shown.
            // If a potion already gave a longer blessing, addEffect keeps the longer one.
            player.addEffect(new MobEffectInstance(ModEffects.REPTILIAN_BLESSING, GRACE_TICKS, BLESSING_AMPLIFIER,
                    true, false, true));
        }
    }

    /** True when the entity wears the snake scale chestplate, leggings and boots. */
    public static boolean hasFullSet(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.SNAKE_SCALE_CHESTPLATE.get())
                && entity.getItemBySlot(EquipmentSlot.LEGS).is(ModItems.SNAKE_SCALE_LEGGINGS.get())
                && entity.getItemBySlot(EquipmentSlot.FEET).is(ModItems.SNAKE_SCALE_BOOTS.get());
    }
}
