package com.snakemod.event;

import com.snakemod.ModEffects;
import com.snakemod.ModItems;
import com.snakemod.SnakeMod;

import com.snakemod.item.SnakeScaleArmorItem;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * Set bonus of the snake scale armor (chestplate + leggings + boots, the helmet does not count):
 * below 3.5 hearts the player gets the reptilian blessing. It lasts while the health stays low,
 * and 3 more seconds after going back above. When the health first drops below, a poison burst
 * envenoms the nearby mobs (cooldown 1 minute).
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

    // Poison burst
    public static final int BURST_COOLDOWN_TICKS = 1200;  // 1 minute (decided)
    public static final double BURST_RADIUS = 6.0;         // [A AJUSTAR] blocks
    public static final int BURST_VENOM_TICKS = 100;       // [A AJUSTAR] 5 seconds of venom
    public static final int BURST_VENOM_AMPLIFIER = 0;     // [A AJUSTAR] venom level I
    public static final float BURST_KNOCKBACK = 0.6F;      // [A AJUSTAR] push strength
    public static final int BURST_COLOR = 0x8F9A2C;        // olive green, like the scales
    // Decided: players are not affected for now. Switching this to true would also hit other players
    // (to do it well, it should then respect the server PvP setting and teams)
    public static final boolean AFFECTS_PLAYERS = false;

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

    // Poison burst: the moment the health drops below 3.5 hearts, nearby mobs get the snake venom
    @SubscribeEvent
    public static void onPlayerDamaged(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof Player player) || !(player.level() instanceof ServerLevel level)) {
            return;
        }
        float healthAfter = player.getHealth();
        float healthBefore = healthAfter + event.getNewDamage(); // getNewDamage = health lost by this hit
        boolean crossedThreshold = healthBefore >= LOW_HEALTH && healthAfter < LOW_HEALTH && healthAfter > 0;
        // The cooldown is stored on the chestplate item, like an ender pearl: the icon greys out while it recharges
        if (!crossedThreshold || !hasFullSet(player) || player.getCooldowns().isOnCooldown(ModItems.SNAKE_SCALE_CHESTPLATE.get())) {
            return;
        }
        // Both chestplates share the cooldown, so swapping them does not skip the wait
        player.getCooldowns().addCooldown(ModItems.SNAKE_SCALE_CHESTPLATE.get(), BURST_COOLDOWN_TICKS);
        player.getCooldowns().addCooldown(ModItems.ANTIDOTE_SNAKE_SCALE_CHESTPLATE.get(), BURST_COOLDOWN_TICKS);
        poisonBurst(level, player);
    }

    private static void poisonBurst(ServerLevel level, Player player) {
        // Only a visual "explosion": green cloud, a soft boom and a push. It does not break blocks or deal damage
        level.sendParticles(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, BURST_COLOR),
                player.getX(), player.getY() + 1.0, player.getZ(), 140, BURST_RADIUS / 2, 0.6, BURST_RADIUS / 2, 0.0);
        level.sendParticles(ParticleTypes.EXPLOSION, player.getX(), player.getY() + 1.0, player.getZ(), 1, 0, 0, 0, 0);
        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 0.6F, 1.6F);

        AABB area = player.getBoundingBox().inflate(BURST_RADIUS);
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area, e -> isBurstTarget(e, player))) {
            target.addEffect(new MobEffectInstance(ModEffects.SNAKE_VENOM, BURST_VENOM_TICKS, BURST_VENOM_AMPLIFIER), player);
            // Push away from the player (knockback takes the direction FROM the target TO the attacker)
            target.knockback(BURST_KNOCKBACK, player.getX() - target.getX(), player.getZ() - target.getZ());
        }
    }

    // Every mob in the sphere except players (decided: not for now, see AFFECTS_PLAYERS), the wearer's own pets and armor stands
    private static boolean isBurstTarget(LivingEntity entity, Player player) {
        if (entity == player || !entity.isAlive() || entity instanceof ArmorStand) return false;
        if (entity instanceof Player && !AFFECTS_PLAYERS) return false;
        if (entity instanceof OwnableEntity pet && player.getUUID().equals(pet.getOwnerUUID())) return false;
        return entity.distanceToSqr(player) <= BURST_RADIUS * BURST_RADIUS;
    }

    // Upgraded set (the 3 pieces improved with antidote): the same protection as the antidote potion.
    // Snake venom and vanilla poison cannot be applied...
    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (hasUpgradedSet(event.getEntity()) && isAntidoteBlocked(event.getEffectInstance().getEffect())) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    // ...and if the player already had them when putting the last piece on, they are cured
    @SubscribeEvent
    public static void onUpgradedTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide() && hasUpgradedSet(player)) {
            player.removeEffect(ModEffects.SNAKE_VENOM);
            player.removeEffect(MobEffects.POISON);
        }
    }

    private static boolean isAntidoteBlocked(Holder<MobEffect> effect) {
        return effect.is(ModEffects.SNAKE_VENOM.getKey()) || effect.is(MobEffects.POISON);
    }

    /** True when the entity wears the snake scale chestplate, leggings and boots (normal or upgraded). */
    public static boolean hasFullSet(LivingEntity entity) {
        return piece(entity, EquipmentSlot.CHEST) != null && piece(entity, EquipmentSlot.LEGS) != null
                && piece(entity, EquipmentSlot.FEET) != null;
    }

    /** True when the 3 pieces are worn and all of them are upgraded with antidote. */
    public static boolean hasUpgradedSet(LivingEntity entity) {
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            SnakeScaleArmorItem piece = piece(entity, slot);
            if (piece == null || !piece.isUpgraded()) return false;
        }
        return true;
    }

    // The snake scale piece worn in that slot, or null if there is none (or it belongs to another slot)
    private static SnakeScaleArmorItem piece(LivingEntity entity, EquipmentSlot slot) {
        return entity.getItemBySlot(slot).getItem() instanceof SnakeScaleArmorItem armor
                && armor.getEquipmentSlot() == slot ? armor : null;
    }
}
