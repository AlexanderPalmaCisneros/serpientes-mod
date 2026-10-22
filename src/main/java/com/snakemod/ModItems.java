package com.snakemod;

import com.snakemod.item.MythicItem;
import com.snakemod.item.SnakeScaleArmorItem;
import com.snakemod.item.SnakeSpearItem;
import com.snakemod.item.SnakeUpgradeTemplate;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;
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
    // Recompensa mítica de la arena: nombre en dorado
    public static final DeferredItem<MythicItem> GOLDEN_SNAKE_EYE = ITEMS.registerItem("golden_snake_eye", MythicItem::new);
    // Provisional egg: the pet hatching logic comes in a later phase
    public static final DeferredItem<Item> SNAKE_EGG = ITEMS.registerSimpleItem("snake_egg");

    // Arma de colmillos: puño americano que envenena al golpear
    public static final DeferredItem<FangClawsItem> FANG_CLAWS = ITEMS.registerItem("fang_claws",
            properties -> new FangClawsItem(ModTiers.FANG, properties));

    // Lanza arrojadiza con punta de colmillo
    public static final DeferredItem<SnakeSpearItem> SNAKE_SPEAR = ITEMS.registerItem("snake_spear",
            SnakeSpearItem::new, SnakeSpearItem.createProperties());

    // Plantilla de mejora de serpiente: botín de la arena, se usa en la mesa de herrería
    public static final DeferredItem<SmithingTemplateItem> SNAKE_UPGRADE_TEMPLATE = ITEMS.register(
            "snake_upgrade_template", SnakeUpgradeTemplate::create);

    // Armadura de escamas: plantilla + pieza de netherite + escama en la mesa de herrería
    public static final DeferredItem<SnakeScaleArmorItem> SNAKE_SCALE_CHESTPLATE = registerArmor("snake_scale_chestplate", ArmorItem.Type.CHESTPLATE, false);
    public static final DeferredItem<SnakeScaleArmorItem> SNAKE_SCALE_LEGGINGS = registerArmor("snake_scale_leggings", ArmorItem.Type.LEGGINGS, false);
    public static final DeferredItem<SnakeScaleArmorItem> SNAKE_SCALE_BOOTS = registerArmor("snake_scale_boots", ArmorItem.Type.BOOTS, false);

    // Las mismas piezas mejoradas con antídoto: pieza + poción de antídoto en la mesa de herrería
    public static final DeferredItem<SnakeScaleArmorItem> ANTIDOTE_SNAKE_SCALE_CHESTPLATE = registerArmor("antidote_snake_scale_chestplate", ArmorItem.Type.CHESTPLATE, true);
    public static final DeferredItem<SnakeScaleArmorItem> ANTIDOTE_SNAKE_SCALE_LEGGINGS = registerArmor("antidote_snake_scale_leggings", ArmorItem.Type.LEGGINGS, true);
    public static final DeferredItem<SnakeScaleArmorItem> ANTIDOTE_SNAKE_SCALE_BOOTS = registerArmor("antidote_snake_scale_boots", ArmorItem.Type.BOOTS, true);

    private static DeferredItem<SnakeScaleArmorItem> registerArmor(String name, ArmorItem.Type type, boolean upgraded) {
        return ITEMS.register(name, () -> new SnakeScaleArmorItem(type, upgraded, new Item.Properties()
                .durability(type.getDurability(ModArmorMaterials.DURABILITY_FACTOR))
                .fireResistant())); // like netherite, it does not burn in lava
    }

    // Entrega la lista a Minecraft (se llama desde el constructor del mod)
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}