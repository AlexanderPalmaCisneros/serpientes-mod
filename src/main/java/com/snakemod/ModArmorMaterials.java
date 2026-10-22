package com.snakemod;

import java.util.EnumMap;
import java.util.List;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModArmorMaterials {
    // [A AJUSTAR] provisional values: same as netherite, because the armor is made from netherite pieces
    public static final int BOOTS_DEFENSE = 3;
    public static final int LEGGINGS_DEFENSE = 6;
    public static final int CHESTPLATE_DEFENSE = 8;
    public static final int HELMET_DEFENSE = 3;
    public static final int ENCHANTABILITY = 15;
    public static final float TOUGHNESS = 3.0F;
    public static final float KNOCKBACK_RESISTANCE = 0.1F;
    public static final int DURABILITY_FACTOR = 37; // vanilla multiplies this by a base value per piece

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, SnakeMod.MODID);

    // The material is the "recipe card" shared by all pieces: defense, sound, repair item and the texture worn on the body
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SNAKE_SCALE = ARMOR_MATERIALS.register("snake_scale",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, BOOTS_DEFENSE);
                        map.put(ArmorItem.Type.LEGGINGS, LEGGINGS_DEFENSE);
                        map.put(ArmorItem.Type.CHESTPLATE, CHESTPLATE_DEFENSE);
                        map.put(ArmorItem.Type.HELMET, HELMET_DEFENSE);
                        map.put(ArmorItem.Type.BODY, CHESTPLATE_DEFENSE);
                    }),
                    ENCHANTABILITY,
                    SoundEvents.ARMOR_EQUIP_NETHERITE,
                    () -> Ingredient.of(ModItems.SNAKE_SCALE.get()), // repaired in the anvil with snake scales
                    // Texture worn on the player: textures/models/armor/snake_scale_layer_1.png and _layer_2.png
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(SnakeMod.MODID, "snake_scale"))),
                    TOUGHNESS,
                    KNOCKBACK_RESISTANCE));

    public static void register(IEventBus modEventBus) {
        ARMOR_MATERIALS.register(modEventBus);
    }
}
