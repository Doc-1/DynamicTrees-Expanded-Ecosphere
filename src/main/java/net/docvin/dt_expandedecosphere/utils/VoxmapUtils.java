package net.docvin.dt_expandedecosphere.utils;

import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class VoxmapUtils {

    private static final Map<Integer, SimpleVoxmap> cachedVoxmaps = new HashMap<>();

    private static SimpleVoxmap createMap(int radius) {
        int r2 = radius * 2;
        int dx = 0;
        int dz = 0;
        final byte[] map = new byte[(int) Math.pow(r2 + 1, 2)];

        int i = 0;
        for (int z = 0; z <= r2; z++) {
            for (int x = 0; x <= r2; x++) {
                dx = Math.abs(x - radius);
                dz = Math.abs(z - radius);
                if (dx + dz <= radius) {
                    map[i] = 1;
                } else if (dx > radius) {
                    map[i] = 0;
                } else if (dz > radius) {
                    map[i] = 0;
                } else if (Math.sqrt(dx) + Math.sqrt(dz) <= Math.sqrt(radius)) {
                    map[i] = 1;
                } else {
                    map[i] = 0;
                }
                i++;
            }
        }
        final SimpleVoxmap cachedMap = new SimpleVoxmap(r2 + 1, 1, r2 + 1, map).setCenter(new BlockPos(radius, 1, radius));
        return cachedMap;
    }

    public static SimpleVoxmap getCircleVoxmap(int radius) {
        if (cachedVoxmaps.containsKey(radius))
            return cachedVoxmaps.get(radius);
        else {
            final SimpleVoxmap voxmap = createMap(radius);
            cachedVoxmaps.put(radius, voxmap);
            return voxmap;
        }
    }
}
