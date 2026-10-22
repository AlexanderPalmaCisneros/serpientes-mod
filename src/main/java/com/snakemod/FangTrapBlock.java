package com.snakemod;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Fang trap: fangs sticking out of a snake scale block. Walking into it hurts, envenoms and pushes
 * every living thing (players included). It never wears out.
 * It can only be placed on the ends of a standing scale column: on top (fangs up) or under it (fangs down).
 */
public class FangTrapBlock extends Block {
    // [A AJUSTAR] provisional values
    public static final float DAMAGE = 2.0F;              // 1 heart per hit (the game lets a hit land every half second)
    public static final int VENOM_DURATION_TICKS = 100;   // 5 seconds of snake venom
    public static final int VENOM_AMPLIFIER = 0;
    public static final float KNOCKBACK = 0.5F;           // push strength, like a hit from a weapon without Knockback

    // Where the fangs point: UP = standing on top of the column, DOWN = hanging under it
    public static final DirectionProperty FACING = BlockStateProperties.VERTICAL_DIRECTION;

    // The box you see when aiming at it. There is no collision box: you walk "into" the fangs, like a cobweb
    private static final VoxelShape SHAPE_UP = Block.box(1, 0, 1, 15, 13, 15);
    private static final VoxelShape SHAPE_DOWN = Block.box(1, 3, 1, 15, 16, 15);

    public FangTrapBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING) == Direction.UP ? SHAPE_UP : SHAPE_DOWN;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    // Clicking the top face puts it standing up, the bottom face hanging down. The side faces give null = not placed
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction face = context.getClickedFace();
        if (face.getAxis() != Direction.Axis.Y) {
            return null;
        }
        BlockState state = this.defaultBlockState().setValue(FACING, face);
        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
    }

    // The block it hangs from (or stands on) must be a scale column standing up: then that face is one of its ends
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState support = level.getBlockState(supportPos(state, pos));
        return support.is(ModBlocks.SNAKE_SCALE_BLOCK.get())
                && support.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y;
    }

    private static BlockPos supportPos(BlockState state, BlockPos pos) {
        return pos.relative(state.getValue(FACING).getOpposite());
    }

    // If the scale block it is attached to disappears (or turns), the trap breaks and drops itself
    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level,
                                     BlockPos pos, BlockPos neighborPos) {
        if (direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    // Called every tick while an entity is inside the block space
    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide() && entity instanceof LivingEntity living) {
            // hurt() already ignores repeated hits during the short invulnerability after each one
            if (living.hurt(level.damageSources().cactus(), DAMAGE)) {
                living.addEffect(new MobEffectInstance(ModEffects.SNAKE_VENOM, VENOM_DURATION_TICKS, VENOM_AMPLIFIER));
                // Push it away from the center of the trap (knockback takes the direction from the target to the source)
                living.knockback(KNOCKBACK, pos.getX() + 0.5 - living.getX(), pos.getZ() + 0.5 - living.getZ());
            }
        }
    }
}
