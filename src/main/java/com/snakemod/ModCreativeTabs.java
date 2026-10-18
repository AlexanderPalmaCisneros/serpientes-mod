package com.snakemod;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    // Otra lista de pedidos, pero para pestañas
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SnakeMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SNAKEMOD_TAB =
            CREATIVE_TABS.register("snakemod_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.snakemod")) // clave del texto en el archivo de idioma
                    .icon(() -> ModItems.SNAKE_SCALE.get().getDefaultInstance()) // dibujo de la pestaña
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.SNAKE_SCALE.get()); // qué ítems contiene
                        output.accept(ModItems.SNAKE_FANG.get());
                        output.accept(ModItems.SNAKE_EYE.get());
                        output.accept(ModItems.GOLDEN_SNAKE_EYE.get());
                        output.accept(ModItems.SNAKE_EGG.get());
                        output.accept(ModBlocks.SNAKE_SCALE_BLOCK_ITEM.get());
                        output.accept(ModBlocks.SNAKE_SCALE_CARPET_ITEM.get());
                        output.accept(ModBlocks.SNAKE_SCALE_STAIRS_ITEM.get());
                    })
                    .build());

    public static void register(IEventBus modEventBus) {
        CREATIVE_TABS.register(modEventBus);
    }
}
