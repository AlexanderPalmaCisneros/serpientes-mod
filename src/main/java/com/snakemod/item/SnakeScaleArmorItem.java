package com.snakemod.item;

import java.util.List;

import com.snakemod.ModArmorMaterials;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/**
 * A piece of the snake scale armor. The antidote-upgraded pieces are separate items with upgraded = true:
 * that is the "mark" of the upgrade, and it also stops the same piece from being upgraded twice
 * (the smithing recipe only accepts the normal piece).
 */
public class SnakeScaleArmorItem extends ArmorItem {
    private final boolean upgraded;

    public SnakeScaleArmorItem(Type type, boolean upgraded, Properties properties) {
        super(ModArmorMaterials.SNAKE_SCALE, type, properties);
        this.upgraded = upgraded;
    }

    public boolean isUpgraded() {
        return upgraded;
    }

    // Mythic item: the name is shown in gold
    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(ChatFormatting.GOLD);
    }

    // Upgraded pieces get an eye-catching line in the tooltip
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (upgraded) {
            tooltip.add(Component.translatable("tooltip.snakemod.antidote_upgrade")
                    .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
