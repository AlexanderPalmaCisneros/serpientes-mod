package com.snakemod;

import com.snakemod.entity.SnakeSpearEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    // Lista de pedidos de entidades
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, SnakeMod.MODID);

    // The flying spear. Same size and tracking values as the vanilla trident
    public static final DeferredHolder<EntityType<?>, EntityType<SnakeSpearEntity>> SNAKE_SPEAR = ENTITIES.register("snake_spear",
            () -> EntityType.Builder.<SnakeSpearEntity>of(SnakeSpearEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("snake_spear"));

    public static void register(IEventBus modEventBus) {
        ENTITIES.register(modEventBus);
    }
}
