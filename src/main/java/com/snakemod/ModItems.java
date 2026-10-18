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
    public static final DeferredItem<Item> SNAKE_FANG = ITEMS.registerSimpleItem("snake_fang");
    public static final DeferredItem<Item> SNAKE_EYE = ITEMS.registerSimpleItem("snake_eye");
    public static final DeferredItem<Item> GOLDEN_SNAKE_EYE = ITEMS.registerSimpleItem("golden_snake_eye");
    // Provisional egg: the pet hatching logic comes in a later phase
    public static final DeferredItem<Item> SNAKE_EGG = ITEMS.registerSimpleItem("snake_egg");

    // Arma de colmillos: puño americano que envenena al golpear
    public static final DeferredItem<FangClawsItem> FANG_CLAWS = ITEMS.registerItem("fang_claws",
            properties -> new FangClawsItem(ModTiers.FANG, properties));

    // Entrega la lista a Minecraft (se llama desde el constructor del mod)
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}