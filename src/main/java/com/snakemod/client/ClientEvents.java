package com.snakemod.client;

import com.snakemod.ModEntities;
import com.snakemod.SnakeMod;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

// Client-only registrations (this class is never loaded on a dedicated server)
@EventBusSubscriber(modid = SnakeMod.MODID, value = Dist.CLIENT)
public class ClientEvents {

    // Tells the game how to draw our entities
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SNAKE_SPEAR.get(), SnakeSpearRenderer::new);
    }
}
