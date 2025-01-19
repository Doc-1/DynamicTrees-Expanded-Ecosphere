package net.docvin.dt_expandedecosphere.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

public class AngledGrowthLogic extends GrowthLogicKit {


    public AngledGrowthLogic(ResourceLocation registryName) {
        super(registryName);
    }

    public static int getDirVariation(LevelAccessor world, BlockPos rootPos, int offset, int dirVariation) {

        return 2 + (CoordUtils.coordHashCode(rootPos.above(offset), 2) % dirVariation);//Vary the direction by a psuedorandom hash function
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final int[] probMap = context.probMap();
        final GrowSignal signal = context.signal();
        Direction originDir = signal.dir;
        BlockPos rootPos = signal.rootPos;

        probMap[0] = 0; //Never go down.
        probMap[1] = 2;
        if (signal.isInTrunk()) {
            if (signal.numSteps % 3 == 0) {
                int dist = rootPos.getY() - signal.numSteps;
                int i = getDirVariation(context.level(), rootPos, dist, 4);
                probMap[1] = 1;
                probMap[i] = 1;
            }
        } else if (signal.numTurns == 1) {
            boolean flag = (signal.numSteps % 3) == 2;
            probMap[originDir.get3DDataValue()] = flag ? 0 : 1;
            probMap[1] = flag ? 1 : 0;
        } else if (signal.numTurns >= 2) {
            int dist = rootPos.getY() - ((context.pos().getY() - rootPos.getY()) - 1);
            int i = getDirVariation(context.level(), rootPos, dist, 4);
            System.out.println(dist);
            probMap[1] = 0;
            probMap[i] = 1;
        }
        return probMap;
    }

}
