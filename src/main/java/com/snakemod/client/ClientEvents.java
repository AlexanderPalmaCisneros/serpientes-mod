package com.snakemod.client;

import com.snakemod.ModEntities;
import com.snakemod.ModItems;
import com.snakemod.SnakeMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

// Client-only registrations (this class is never loaded on a dedicated server)
@EventBusSubscriber(modid = SnakeMod.MODID, value = Dist.CLIENT)
public class ClientEvents {

    // Tells the game how to draw our entities
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SNAKE_SPEAR.get(), SnakeSpearRenderer::new);
    }

    // Registers the "mold" of the chestplate model (with the shoulder fangs)
    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SnakeScaleArmorModel.LAYER, SnakeScaleArmorModel::createLayer);
    }

    // The chestplate swaps the normal armor model for ours; leggings and boots keep the vanilla shape
    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            private HumanoidModel<LivingEntity> model;

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot,
                                                          HumanoidModel<?> original) {
                if (model == null) { // built the first time it is needed, when the models are already loaded
                    model = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(SnakeScaleArmorModel.LAYER));
                }
                return model;
            }
        }, ModItems.SNAKE_SCALE_CHESTPLATE.get());
    }
}
