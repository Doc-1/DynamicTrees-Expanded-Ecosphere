package net.docvin.dt_expandedecosphere.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class BeechGrowthLogic extends GrowthLogicKit {

    public BeechGrowthLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final int[] probMap = context.probMap();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            probMap[dir.get3DDataValue()] *= 5;
        }
        probMap[Direction.UP.get3DDataValue()] *= 2;
        return probMap;
    }
}
