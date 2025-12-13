package net.docvin.dt_expandedecosphere.common.block;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.block.branch.TrunkShellBlock;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.RootConnections;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class ButtressRootBlock extends Block implements SimpleWaterloggedBlock {

    public static final int MAX_SIZE = 16;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final IntegerProperty SIZE = IntegerProperty.create("max_size", 1, MAX_SIZE);

    private final Family family;

    public ButtressRootBlock(Family family) {
        this(MapColor.WOOD, family);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    public ButtressRootBlock(MapColor mapColor, Family family) {
        super(Properties.of()
                .mapColor(mapColor)
                .strength(2.5f, 1.0F)
                .sound(SoundType.WOOD));

        this.family = family;
    }

    public Family getFamily() {
        return family;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return this.family.getBranchItem().map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    // BLOCK STATES //

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SIZE, WATERLOGGED);
    }

    public int getSize(BlockState blockState) {
        return blockState.getBlock() == this ? blockState.getValue(SIZE) : 0;
    }

    public void placeRootAtSize(LevelAccessor level, BlockPos pos, int size, int flags) {
        boolean replacingWater = level.getBlockState(pos).getFluidState() == Fluids.WATER.getSource(false);
        level.setBlock(pos, this.getStateForSize(size).setValue(WATERLOGGED, replacingWater), flags);
    }

    public BlockState getStateForSize(int size) {
        return this.defaultBlockState().setValue(SIZE, Mth.clamp(size, 0, getMaxSize()));
    }

    public int getMaxSize() {
        return MAX_SIZE;
    }

    //  WATER LOGGING //

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            level.getFluidTicks().schedule(new ScheduledTick<>(Fluids.WATER, currentPos, Fluids.WATER.getTickDelay(level), 1));
        }
        return super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    // RENDERING //

    public RootConnections getConnectionData(final BlockAndTintGetter level, final BlockPos pos) {
        final RootConnections connections = new RootConnections();

        for (Direction dir : CoordUtils.HORIZONTALS) {
            final RootConnection connection = this.getSideConnectionSize(level, pos, dir);

            if (connection == null) {
                continue;
            }

            connections.setRadius(dir, connection.size);
            connections.setConnectionLevel(dir, connection.level);
        }

        return connections;
    }

    // PHYSICAL BOUNDS
    @Nonnull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        boolean connectionMade = false;
        final int thisSize = getSize(state);

        VoxelShape shape = Shapes.empty();

        for (Direction dir : CoordUtils.HORIZONTALS) {
            final RootConnection conn = this.getSideConnectionSize(level, pos, dir);

            if (conn == null) {
                continue;
            }

            connectionMade = true;
            final int r = Mth.clamp(conn.size, 1, thisSize);
            final double size = r / 16.0;
            final double gap = 0.5 - size;

            AABB aabb = new AABB(-size / 3, 0, -size / 3, size / 3, size, size / 3);
            aabb = aabb.expandTowards(dir.getStepX() * gap, 0, dir.getStepZ() * gap).move(0.5, 0.0, 0.5);
            shape = Shapes.joinUnoptimized(shape, Shapes.create(aabb), BooleanOp.OR);
        }

        if (!connectionMade) {
            double size = thisSize / 16.0;
            AABB aabb = new AABB(0.5 - size, 0, 0.5 - size, 0.5 + size, size, 0.5 + size);
            shape = Shapes.joinUnoptimized(shape, Shapes.create(aabb), BooleanOp.OR);
        }

        return shape;
    }

    private boolean isAirOrWater(BlockState state) {
        return state.getBlock() == Blocks.AIR || state.getBlock() == Blocks.WATER;
    }

    @Nullable
    protected RootConnection getSideConnectionSize(BlockGetter level, BlockPos pos, Direction side) {
        if (!side.getAxis().isHorizontal()) {
            return null;
        }

        BlockPos dPos = pos.relative(side);
        BlockState state = CoordUtils.getStateSafe(level, dPos);
        final BlockState upState = CoordUtils.getStateSafe(level, pos.above());

        final RootConnections.ConnectionLevel connectionLevel = (upState != null && isAirOrWater(upState) && state != null && state.isRedstoneConductor(level, dPos)) ?
                RootConnections.ConnectionLevel.HIGH : (state != null && isAirOrWater(state) ? RootConnections.ConnectionLevel.LOW : RootConnections.ConnectionLevel.MID);

        if (connectionLevel != RootConnections.ConnectionLevel.MID) {
            dPos = dPos.above(connectionLevel.getYOffset());
            state = CoordUtils.getStateSafe(level, dPos);
        }

        if (state != null && state.getBlock() instanceof ButtressRootBlock) {
            return new RootConnection(connectionLevel, ((ButtressRootBlock) state.getBlock()).getSize(state));
        } else if (connectionLevel == RootConnections.ConnectionLevel.MID && TreeHelper.isBranch(state)) {
            return new RootConnection(RootConnections.ConnectionLevel.MID, Math.min(TreeHelper.getTreePart(state).getRadius(state), 8));
        }

        return null;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        final BlockState upstate = level.getBlockState(pos.above());

        if (upstate.getBlock() instanceof TrunkShellBlock) {
            level.setBlockAndUpdate(pos, upstate);
        }

        for (Direction dir : CoordUtils.HORIZONTALS) {
            final BlockPos dPos = pos.relative(dir).below();
            level.getBlockState(dPos).neighborChanged(level, dPos, this, pos, false);
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        if (!canBlockStay(level, pos, state)) {
            level.removeBlock(pos, false);
        }
    }

    protected boolean canBlockStay(Level level, BlockPos pos, BlockState state) {
        final BlockPos below = pos.below();
        final BlockState belowState = level.getBlockState(below);

        final int size = getSize(state);

        if (belowState.isRedstoneConductor(level, below)) { // If a root is sitting on a solid block.
            for (Direction dir : CoordUtils.HORIZONTALS) {
                final RootConnection conn = this.getSideConnectionSize(level, pos, dir);

                if (conn != null && conn.size > size) {
                    return true;
                }
            }
        } else { // If the root has no solid block under it.
            boolean connections = false;

            for (Direction dir : CoordUtils.HORIZONTALS) {
                final RootConnection conn = this.getSideConnectionSize(level, pos, dir);

                if (conn == null) {
                    continue;
                }

                if (conn.level == RootConnections.ConnectionLevel.MID) {
                    return false;
                }

                if (conn.size > size) {
                    connections = true;
                }
            }

            return connections;
        }

        return false;
    }


    public static class RootConnection {
        public RootConnections.ConnectionLevel level;
        public int size;

        public RootConnection(RootConnections.ConnectionLevel level, int size) {
            this.level = level;
            this.size = size;
        }

        @Override
        public String toString() {
            return super.toString() + " Level: " + this.level.toString() + " Size: " + this.size;
        }
    }


}
