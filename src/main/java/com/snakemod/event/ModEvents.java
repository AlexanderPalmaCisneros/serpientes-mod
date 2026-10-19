package com.snakemod.event;

import com.snakemod.ModEffects;
import com.snakemod.ModItems;
import com.snakemod.ModPotions;
import com.snakemod.SnakeMod;
import com.snakemod.effect.ReptilianBlessingEffect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

// Events of the mod that run on the main game bus
@EventBusSubscriber(modid = SnakeMod.MODID)
public class ModEvents {

    // Brewing stand recipes. The chain: awkward + fang = venom, venom + eye = antidote (like a real antivenom)
    @SubscribeEvent
    public static void onRegisterBrewing(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addMix(Potions.AWKWARD, ModItems.SNAKE_FANG.get(), ModPotions.SNAKE_VENOM);
        event.getBuilder().addMix(ModPotions.SNAKE_VENOM, ModItems.SNAKE_EYE.get(), ModPotions.ANTIDOTE);
        event.getBuilder().addMix(Potions.AWKWARD, ModItems.SNAKE_EYE.get(), ModPotions.REPTILIAN_BLESSING);
    }

    // Reptilian blessing: keep the horizontal speed while jumping (only for the local player, on the client).
    // The client moves the player and the server accepts it, so changing the server copy would cause rubber-banding.
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!player.isLocalPlayer() || !player.hasEffect(ModEffects.REPTILIAN_BLESSING)) {
            return;
        }
        boolean airborne = !player.onGround() && !player.isInWater() && !player.isInLava()
                && !player.getAbilities().flying && !player.isFallFlying()
                && !player.onClimbable() && !player.isPassenger();
        if (airborne) {
            Vec3 motion = player.getDeltaMovement();
            player.setDeltaMovement(motion.x * ReptilianBlessingEffect.AIR_MOMENTUM_FACTOR, motion.y,
                    motion.z * ReptilianBlessingEffect.AIR_MOMENTUM_FACTOR);
        }
    }

    // Antidote immunity: while the entity has the antidote effect, venom and poison cannot be applied
    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (!event.getEntity().hasEffect(ModEffects.ANTIDOTE)) {
            return;
        }
        var incoming = event.getEffectInstance().getEffect();
        if (incoming.is(ModEffects.SNAKE_VENOM.getId()) || incoming.is(ResourceLocation.withDefaultNamespace("poison"))) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }
}
