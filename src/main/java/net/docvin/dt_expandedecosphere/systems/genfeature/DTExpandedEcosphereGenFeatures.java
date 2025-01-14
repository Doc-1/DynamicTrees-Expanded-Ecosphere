package net.docvin.dt_expandedecosphere.systems.genfeature;

import com.ferreusveritas.dynamictrees.api.registry.RegistryEvent;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import net.docvin.dt_expandedecosphere.DynamicTreesExpandedEcosphere;

public class DTExpandedEcosphereGenFeatures {

    public static final GenFeature COB_WEB = new SpiderWebGenFeature(DynamicTreesExpandedEcosphere.location("cob_web"));

    public DTExpandedEcosphereGenFeatures() {
    }

    public static void registerGenFeatures(RegistryEvent<GenFeature> event) {
        event.getRegistry().registerAll(COB_WEB);
    }
}
