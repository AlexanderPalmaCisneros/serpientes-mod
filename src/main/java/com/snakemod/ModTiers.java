package com.snakemod;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

/**
 * Tool materials of the mod. A "tier" bundles durability, enchantability and the repair item.
 * All values are provisional [A AJUSTAR] in the design doc.
 */
public class ModTiers {
    public static final Tier FANG = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL, // which blocks it cannot mine (irrelevant for a weapon)
            250,                               // durability (iron = 250)
            6.0F,                              // mining speed (irrelevant for a weapon)
            0.0F,                              // extra attack damage added by the material
            14,                                // enchantability (iron = 14)
            () -> Ingredient.of(ModItems.SNAKE_FANG.get())); // repaired with snake fangs (lazy: item does not exist yet here)
}
