package com.snakemod;

import java.util.Map;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * Carpet that remembers which sides have another snake scale carpet next to it.
 * The blockstate JSON uses these flags to hide the border on the connected sides,
 * so a group of carpets looks like one single shape.
 */
public class SnakeScaleCarpetBlock extends CarpetBlock {
    public static final MapCodec<SnakeScaleCarpetBlock> CODEC = simpleCodec(SnakeScaleCarpetBlock::new);

    // true = there is another snake scale carpet on that side
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = Map.of(
            Direction.NORTH, NORTH,
            Direction.EAST, EAST,
            Direction.SOUTH, SOUTH,
            Direction.WEST, WEST);

    public SnakeScaleCarpetBlock(BlockBehaviour.Properties properties) {
        super(properties);
        // Start with no neighbors on any side
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false));
    }

    @Override
    public MapCodec<SnakeScaleCarpetBlock> codec() {
        return CODEC;
    }

    // Declares which properties this block has (like declaring the fields of the block)
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST);
    }

    // When placed: look at the 4 sides and set the flags
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = this.defaultBlockState();
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            state = state.setValue(PROPERTY_BY_DIRECTION.get(direction),
                    level.getBlockState(pos.relative(direction)).is(this));
        }
        return state;
    }

    // When a neighbor changes (placed or broken): update the flag of that side
    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        BlockState result = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        if (result.is(this) && direction.getAxis().isHorizontal()) {
            result = result.setValue(PROPERTY_BY_DIRECTION.get(direction), neighborState.is(this));
        }
        return result;
    }
}
