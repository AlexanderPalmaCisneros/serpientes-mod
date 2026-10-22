package com.snakemod.item;

import java.util.List;

import com.snakemod.SnakeMod;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

/**
 * Builds the snake upgrade template, the same way vanilla builds the netherite upgrade template.
 * The texts are the tooltip lines and the hints shown in the smithing table slots.
 */
public final class SnakeUpgradeTemplate {
    // Grey outlines drawn in the empty base slot (vanilla sprites), cycling between the 3 pieces
    private static final List<ResourceLocation> BASE_SLOT_ICONS = List.of(
            ResourceLocation.withDefaultNamespace("item/empty_armor_slot_chestplate"),
            ResourceLocation.withDefaultNamespace("item/empty_armor_slot_leggings"),
            ResourceLocation.withDefaultNamespace("item/empty_armor_slot_boots"));

    private SnakeUpgradeTemplate() {
    }

    public static SmithingTemplateItem create() {
        return new SmithingTemplateItem(
                text("applies_to").withStyle(ChatFormatting.BLUE),
                text("ingredients").withStyle(ChatFormatting.BLUE),
                Component.translatable("upgrade." + SnakeMod.MODID + ".snake_upgrade").withStyle(ChatFormatting.GRAY),
                text("base_slot_description"),
                text("additions_slot_description"),
                BASE_SLOT_ICONS,
                List.of()); // no vanilla outline fits a scale block, so the slot stays empty
    }

    private static MutableComponent text(String key) {
        return Component.translatable("item." + SnakeMod.MODID + ".smithing_template.snake_upgrade." + key);
    }
}
