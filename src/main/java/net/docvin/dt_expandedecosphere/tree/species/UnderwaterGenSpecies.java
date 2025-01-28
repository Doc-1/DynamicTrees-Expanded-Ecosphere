package net.docvin.dt_expandedecosphere.tree.species;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.worldgen.GenerationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class UnderwaterGenSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(UnderwaterGenSpecies::new);

    public UnderwaterGenSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
        this.setAllowedWaterHeightForWorldgen(5);
    }


    @Override
    public boolean isAcceptableSoilForWorldgen(LevelAccessor level, BlockPos pos, BlockState soilBlockState) {

        // If the block is water, check the block below it is valid soil (and not water).
        if (isWater(soilBlockState)) {
            int maxH = getAllowedWaterHeightForWorldgen();
            int waterBelow = countWaterBlocksBelow(level, pos, maxH + 2);
            return waterBelow <= maxH && isAcceptableSoilForWorldgen(level.getBlockState(pos.below(waterBelow)));
        }
        return false;
    }

    @Override
    public boolean generate(GenerationContext context) {
        BlockPos.MutableBlockPos pos = context.rootPos();
        LevelAccessor level = context.level();
        if (isWater(level.getBlockState(pos.below()))) {
            int maxH = getAllowedWaterHeightForWorldgen();
            int waterBelow = countWaterBlocksBelow(level, pos, maxH + 2);
            if (waterBelow <= maxH && isAcceptableSoilForWorldgen(level.getBlockState(pos.below(waterBelow)))) {
                context.rootPos().move(0, -waterBelow, 0);
                return super.generate(context);
            }
        }
        return false;
    }
}
