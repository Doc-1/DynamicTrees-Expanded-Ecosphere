package net.docvin.dt_expandedecosphere.growthlogic;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import net.docvin.dt_expandedecosphere.DynamicTreesExpandedEcosphere;

public class GrowthLogicKits {

    public static final GrowthLogicKit BEECH = new BeechGrowthLogic(DynamicTreesExpandedEcosphere.location("common_beech"));
    public static final GrowthLogicKit VariateHeight = new VariateHeightLogic(DynamicTreesExpandedEcosphere.location("variate_height"));
    public static final GrowthLogicKit POPLAR = new PoplarLogic(DynamicTreesExpandedEcosphere.location("poplar"));

    public static void register(final Registry<GrowthLogicKit> registry) {
        registry.registerAll(BEECH, VariateHeight, POPLAR);
    }
}
