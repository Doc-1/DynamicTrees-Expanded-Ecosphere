package net.docvin.dt_expandedecosphere.tree.family.suppliers;

import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.util.Optionals;
import net.docvin.dt_expandedecosphere.common.block.ButtressRootBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ButtressRootBlockSupplier {
    private static final HashMap<String, Supplier<ButtressRootBlock>> buttressRootProviderTreeMap = new HashMap<>();
    private static final List<String> list = new ArrayList<>();

    public static void addButtressRootProvider(Family family, Supplier<ButtressRootBlock> supplier) {
        if (supplier != null)
            buttressRootProviderTreeMap.put(family.getRegistryName().getPath(), supplier);
        else
            buttressRootProviderTreeMap.remove(family.getRegistryName().getPath());
    }

    public static void familyHasRootGen(Family family, boolean value) {
        if (value)
            list.add(family.getRegistryName().getPath());
    }

    public static Optional<ButtressRootBlock> getButtressRoot(Family family) {
        String path = family.getRegistryName().getPath();
        return Optionals.ofBlock(buttressRootProviderTreeMap.getOrDefault(path, null));
    }


    public static boolean hasButtressRoot(Family family) {
        return list.contains(family.getRegistryName().getPath());
    }
}
