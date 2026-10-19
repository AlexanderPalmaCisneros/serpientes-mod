package com.snakemod;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPotions {
    // [A AJUSTAR] provisional durations, in ticks (20 ticks = 1 second)
    public static final int VENOM_TICKS = 900;          // 45 seconds of snake venom
    public static final int ANTIDOTE_TICKS = 3600;      // 3 minutes of cure + immunity
    public static final int BLESSING_TICKS = 1800;      // 90 seconds of speed and strength

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, SnakeMod.MODID);

    // A Potion is just a list of effects. The potion item (bottle) is built by Minecraft
    public static final DeferredHolder<Potion, Potion> SNAKE_VENOM = POTIONS.register("snake_venom",
            () -> new Potion(new MobEffectInstance(ModEffects.SNAKE_VENOM, VENOM_TICKS)));

    public static final DeferredHolder<Potion, Potion> ANTIDOTE = POTIONS.register("antidote",
            () -> new Potion(new MobEffectInstance(ModEffects.ANTIDOTE, ANTIDOTE_TICKS)));

    public static final DeferredHolder<Potion, Potion> REPTILIAN_BLESSING = POTIONS.register("reptilian_blessing",
            () -> new Potion(new MobEffectInstance(ModEffects.REPTILIAN_BLESSING, BLESSING_TICKS)));

    public static void register(IEventBus modEventBus) {
        POTIONS.register(modEventBus);
    }
}
