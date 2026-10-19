package com.snakemod;

import com.snakemod.effect.AntidoteEffect;
import com.snakemod.effect.ReptilianBlessingEffect;
import com.snakemod.effect.SnakeVenomEffect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    // Lista de pedidos de efectos de estado
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, SnakeMod.MODID);

    public static final DeferredHolder<MobEffect, SnakeVenomEffect> SNAKE_VENOM =
            EFFECTS.register("snake_venom", SnakeVenomEffect::new);

    public static final DeferredHolder<MobEffect, ReptilianBlessingEffect> REPTILIAN_BLESSING =
            EFFECTS.register("reptilian_blessing", ReptilianBlessingEffect::new);

    public static final DeferredHolder<MobEffect, AntidoteEffect> ANTIDOTE =
            EFFECTS.register("antidote", AntidoteEffect::new);

    public static void register(IEventBus modEventBus) {
        EFFECTS.register(modEventBus);
    }
}
