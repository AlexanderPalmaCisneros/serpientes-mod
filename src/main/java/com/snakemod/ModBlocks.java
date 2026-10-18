package com.snakemod;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    // Lista de pedidos de bloques
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SnakeMod.MODID);

    // El bloque: "snakemod:snake_scale_block"
    // TODO: strength values are provisional, not in the design doc yet
    // RotatedPillarBlock = bloque de columna: guarda su orientación (eje x, y o z) como los troncos
    public static final DeferredBlock<RotatedPillarBlock> SNAKE_SCALE_BLOCK = BLOCKS.registerBlock(
            "snake_scale_block",
            RotatedPillarBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN) // color en el mapa
                    .strength(2.0F, 6.0F)           // dureza al romper, resistencia a explosiones
                    .sound(SoundType.STONE));       // sonido al pisar/romper

    // El ítem del bloque (lo que llevas en la mano), apuntando al bloque de arriba
    public static final DeferredItem<BlockItem> SNAKE_SCALE_BLOCK_ITEM =
            ModItems.ITEMS.registerSimpleBlockItem("snake_scale_block", SNAKE_SCALE_BLOCK);

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
