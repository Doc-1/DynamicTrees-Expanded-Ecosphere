package net.docvin.dt_expandedecosphere.cellkits.cell;

import com.ferreusveritas.dynamictrees.api.cell.Cell;
import net.minecraft.core.Direction;

public class SparseBranchCell implements Cell {

    static final int[] map = {0, 2, 2, 2, 2, 2};

    @Override
    public int getValue() {
        return 2;
    }

    @Override
    public int getValueFromSide(Direction side) {
        return map[side.ordinal()];
    }
}
