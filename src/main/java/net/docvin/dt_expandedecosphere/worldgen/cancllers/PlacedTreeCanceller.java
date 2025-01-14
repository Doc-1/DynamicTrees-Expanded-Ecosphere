package net.docvin.dt_expandedecosphere.worldgen.cancllers;

import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import net.minecraft.world.level.levelgen.feature.RandomSelectorFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;

public class PlacedTreeCanceller extends FeatureCanceller {
    public PlacedTreeCanceller(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public boolean shouldCancel(ConfiguredFeature<?, ?> configuredFeature, BiomePropertySelectors.NormalFeatureCancellation normalFeatureCancellation) {

        FeatureConfiguration featureConfig = configuredFeature.config();
        System.out.println(featureConfig);

        if (featureConfig instanceof RandomPatchConfiguration || featureConfig instanceof RandomFeatureConfiguration || featureConfig instanceof RandomSelectorFeature) {
            return true;
        }

        if (featureConfig instanceof TreeConfiguration || featureConfig instanceof HugeFungusConfiguration || featureConfig instanceof HugeMushroomFeatureConfiguration)
            return true;
        return true;
    }


}

