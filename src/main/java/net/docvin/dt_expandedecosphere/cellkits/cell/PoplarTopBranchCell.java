package net.docvin.dt_expandedecosphere.cellkits.cell;

import com.ferreusveritas.dynamictrees.api.cell.Cell;
import net.minecraft.core.Direction;

public class PoplarTopBranchCell implements Cell {

    static final int[] map = {3, 4, 3, 3, 3, 3};

    @Override
    public int getValue() {
        return 5;
    }

    @Override
    public int getValueFromSide(Direction side) {
        return map[side.ordinal()];
    }
}
