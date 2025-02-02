package net.docvin.dt_expandedecosphere.tree.species;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.worldgen.GenerationContext;
import com.ferreusveritas.dynamictrees.worldgen.JoCode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class SubmersibleSpecies extends Species {
    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(SubmersibleSpecies::new);

    //Use "allowed_water_height_for_world_gen": int in trees/modid/species/species.json to set max depth of tree
    private Species submerged = Species.NULL_SPECIES;

    public SubmersibleSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);

    }

    public void setSubmergedSpecies(Species submerged) {
        if (submerged != this)
            this.submerged = submerged;
    }

    @Override
    public boolean isAcceptableSoilForWorldgen(LevelAccessor level, BlockPos pos, BlockState soilBlockState) {
        // If the block is water, check the blocks below it is valid soil (and not water).
        if (isWater(soilBlockState)) {
            int maxH = submerged.getAllowedWaterHeightForWorldgen();
            int waterBelow = countWaterBlocksBelow(level, pos, maxH + 2);
            return waterBelow <= maxH && isAcceptableSoilForWorldgen(level.getBlockState(pos.below(waterBelow)));
        }
        return isAcceptableSoilForWorldgen(soilBlockState);
    }

    @Override
    public BlockPos preGeneration(LevelAccessor level, BlockPos.MutableBlockPos rootPos, int radius, Direction facing, SafeChunkBounds safeBounds, JoCode joCode) {
        if (isWater(level.getBlockState(rootPos))) {
            int maxH = submerged.getAllowedWaterHeightForWorldgen();
            int waterBelow = countWaterBlocksBelow(level, rootPos, maxH + 2);
            if (waterBelow <= maxH && isAcceptableSoilForWorldgen(level.getBlockState(rootPos.below(waterBelow)))) {
                rootPos.move(0, -waterBelow, 0);
                return submerged.preGeneration(level, rootPos, radius, facing, safeBounds, joCode);
            }
        }
        return super.preGeneration(level, rootPos, radius, facing, safeBounds, joCode);
    }

    @Override
    public boolean generate(GenerationContext context) {
        BlockPos.MutableBlockPos pos = context.rootPos();
        LevelAccessor level = context.level();
        //System.out.println("gen " + pos + " " + level.getBlockState(pos));
        if (isSubmerged(level, pos)) {
            System.out.println(pos.above() + " dasdaf");
            return submerged.generate(context);
        }
        return super.generate(context);
    }

    private boolean isSubmerged(LevelAccessor level, BlockPos pos) {
        int min = 8;
        int x = 0;
        for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 0, 1))) {
            FluidState state = level.getFluidState(blockPos);
            x = !state.isEmpty() ? x + 1 : 0;
            if (x >= min)
                return true;
        }
        return false;
    }

    @Override
    protected boolean transitionToTree(Level level, BlockPos pos, Family family) {
        if (isSubmerged(level, pos)) {
            return submerged.transitionToTree(level, pos);
        }

        return super.transitionToTree(level, pos, family);
    }

}
