package net.docvin.dt_expandedecosphere.worldgen.cancllers;

import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class PlacedTreeCanceller extends FeatureCanceller {
    public PlacedTreeCanceller(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public boolean shouldCancel(ConfiguredFeature<?, ?> configuredFeature, BiomePropertySelectors.NormalFeatureCancellation normalFeatureCancellation) {

        FeatureConfiguration featureConfig = configuredFeature.config();
        System.out.println(featureConfig);


        return featureConfig instanceof TreeConfiguration;
    }


}

