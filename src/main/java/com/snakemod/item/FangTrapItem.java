package com.snakemod.item;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The fang trap in your hand. When the trap cannot go where you clicked (it is not an end of a standing
 * snake scale column), it tells you why on the screen instead of silently doing nothing.
 */
public class FangTrapItem extends BlockItem {
    public FangTrapItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = super.getPlacementState(context);
        // The block returns null only when the place is wrong; then we warn the player
        Player player = context.getPlayer();
        if (state == null && player != null && context.getLevel().isClientSide()
                && this.getBlock().getStateForPlacement(context) == null) {
            // true = shown in the middle of the screen, above the hotbar (not in the chat)
            player.displayClientMessage(Component.translatable("message.snakemod.fang_trap_wrong_place")
                    .withStyle(ChatFormatting.RED), true);
        }
        return state;
    }
}
