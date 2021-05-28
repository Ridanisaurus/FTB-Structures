package dev.ftb.mods.ftbstructures.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CrateBlock extends Block {
	public static final VoxelShape SHAPE = Shapes.or(
			box(0.5, 0.25, 0.5, 15.5, 15.75, 15.5),
			box(0, 0, 0, 5, 16, 5),
			box(0, 0, 11, 5, 16, 16),
			box(11, 0, 11, 16, 16, 16),
			box(5, 13, 0, 11, 16, 5),
			box(0, 13, 5, 5, 16, 11),
			box(5, 13, 11, 11, 16, 16),
			box(11, 13, 5, 16, 16, 11),
			box(5, 0, 0, 11, 3, 5),
			box(0, 0, 5, 5, 3, 11),
			box(5, 0, 11, 11, 3, 16),
			box(11, 0, 5, 16, 3, 11),
			box(11, 0, 0, 16, 16, 5)
	);

	public CrateBlock() {
		super(Properties.of(Material.WOOD).strength(5F, 6F).sound(SoundType.WOOD).noDrops().noOcclusion());
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
		arg.add(BlockStateProperties.WATERLOGGED);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	@Deprecated
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext arg) {
		FluidState fluidState = arg.getLevel().getFluidState(arg.getClickedPos());
		return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	@Nullable
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			level.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, facing, facingState, level, pos, facingPos);
	}

	@Override
	@Deprecated
	public VoxelShape getBlockSupportShape(BlockState arg, BlockGetter arg2, BlockPos arg3) {
		return Shapes.block();
	}
}