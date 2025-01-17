package net.docvin.dt_expandedecosphere.cellkits;

import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import net.minecraft.core.BlockPos;

public class DTExpandedEcoshpereLeafClusters {

    public static final SimpleVoxmap SPARSE = new SimpleVoxmap(3, 2, 3, new byte[]{
            0, 1, 0,
            1, 0, 1,
            0, 1, 0,

            0, 0, 0,
            0, 1, 0,
            0, 0, 0
    }).setCenter(new BlockPos(1, 0, 1));

    public static final SimpleVoxmap POPLAR = new SimpleVoxmap(3, 4, 3, new byte[]{
            0, 0, 0,
            0, 1, 0,
            0, 0, 0,

            0, 2, 0,
            2, 0, 2,
            0, 2, 0,

            0, 1, 0,
            1, 2, 1,
            0, 1, 0,

            0, 0, 0,
            0, 1, 0,
            0, 0, 0
    }).setCenter(new BlockPos(1, 1, 1));

    public static final SimpleVoxmap POPLAR_TOP = new SimpleVoxmap(3, 3, 3, new byte[]{
            0, 1, 0,
            1, 0, 1,
            0, 1, 0,

            0, 0, 0,
            0, 2, 0,
            0, 0, 0,

            0, 0, 0,
            0, 1, 0,
            0, 0, 0
    }).setCenter(new BlockPos(1, 0, 1));

}
