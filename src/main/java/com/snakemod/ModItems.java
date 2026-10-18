package com.snakemod;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    // La "lista de pedidos": todos los ítems del mod se anotan aquí
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SnakeMod.MODID);

    // Un pedido: ítem simple con id "snakemod:snake_scale"
    public static final DeferredItem<Item> SNAKE_SCALE = ITEMS.registerSimpleItem("snake_scale");

    // Entrega la lista a Minecraft (se llama desde el constructor del mod)
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}