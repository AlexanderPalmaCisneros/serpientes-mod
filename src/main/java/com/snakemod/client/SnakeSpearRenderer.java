package com.snakemod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.snakemod.entity.SnakeSpearEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;

/**
 * Draws the flying spear using the same model as the item, turned to point where it flies.
 */
public class SnakeSpearRenderer extends EntityRenderer<SnakeSpearEntity> {
    private final ItemRenderer itemRenderer;

    public SnakeSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SnakeSpearEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        // Same turns the vanilla arrow uses: after them, the +X axis points where the entity flies
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        // -90: the item model points its tip up (+Y), and we want it along +X
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) - 90.0F));
        poseStack.scale(1.6F, 1.6F, 1.6F); // the model is about 2 blocks long (32 units), so the spear in flight is about 3 blocks
        this.itemRenderer.renderStatic(entity.getPickupItemStackOrigin(), ItemDisplayContext.NONE, packedLight,
                OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SnakeSpearEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
