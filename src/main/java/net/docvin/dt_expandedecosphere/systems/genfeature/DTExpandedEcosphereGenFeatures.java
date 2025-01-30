package net.docvin.dt_expandedecosphere.systems.genfeature;

import com.ferreusveritas.dynamictrees.api.registry.RegistryEvent;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import net.docvin.dt_expandedecosphere.DynamicTreesExpandedEcosphere;

public class DTExpandedEcosphereGenFeatures {

    public static final GenFeature COBWEB = new SpiderWebGenFeature(DynamicTreesExpandedEcosphere.location("cob_web"));
    public static final GenFeature MOSS = new MossGrowthGenFeature(DynamicTreesExpandedEcosphere.location("moss"));
    public static final GenFeature REPLACE_GRASS = new ReplaceGrassWithBlockGenFeature(DynamicTreesExpandedEcosphere.location("replace_grass"));

    public static final GenFeature FILL_VOLUME = new FillVolumeGenFeature(DynamicTreesExpandedEcosphere.location("fill_volume"));

    public DTExpandedEcosphereGenFeatures() {
    }

    public static void registerGenFeatures(RegistryEvent<GenFeature> event) {
        event.getRegistry().registerAll(COBWEB, REPLACE_GRASS, MOSS, FILL_VOLUME);
    }
}
