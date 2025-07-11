package net.docvin.dt_expandedecosphere.worldgen.cancllers;

import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;

public class ExpandedEcosphereTreeCanceller<T extends FeatureConfiguration> extends FeatureCanceller {

    private final Class<T> treeFeatureConfigClass;
    List<TreeConfiguration> treeConfigurationList = new ArrayList<>();

    public ExpandedEcosphereTreeCanceller(final ResourceLocation registryName, Class<T> treeFeatureConfigClass) {
        super(registryName);
        this.treeFeatureConfigClass = treeFeatureConfigClass;
    }

    @Override
    public boolean shouldCancel(ConfiguredFeature<?, ?> configuredFeature, BiomePropertySelectors.NormalFeatureCancellation featureCancellations) {
        final FeatureConfiguration featureConfig = configuredFeature.config();

        /*  The following code removes vanilla trees from the biome's generator.
            There may be some problems as MultipleRandomFeatures can store other features too,
            so these are currently removed from world gen too. The list is immutable so they can't be removed individually,
            but one (unclean) solution may be to add the non-tree features back to the generator. */
        MinecraftServer level = ServerLifecycleHooks.getCurrentServer();
        RegistryAccess registryAccess = level.registryAccess();
        Registry<ConfiguredFeature<?, ?>> configuredFeatureRegistry =
                registryAccess.registryOrThrow(Registries.CONFIGURED_FEATURE);
        Registry<PlacedFeature> featureRegistry =
                registryAccess.registryOrThrow(Registries.PLACED_FEATURE);
        if (treeConfigurationList.isEmpty()) {
            for (var entry : configuredFeatureRegistry.entrySet()) {
                var config = entry.getValue().config();
                ResourceLocation name = entry.getKey().location();
                if (config instanceof TreeConfiguration treeConfiguration) {
                    if (name.toString().equals("wythers:vegetation/configured_trees_dry_savanna")) {
                        treeConfigurationList.add(treeConfiguration);
                    }
                }
            }
        }

        if (skip(featureConfig))
            return false;

        if (featureConfig instanceof RandomFeatureConfiguration) {
            // Removes configuredFeature if it contains trees.
            return this.doesContainTrees((RandomFeatureConfiguration) featureConfig, featureCancellations);
        } else if (this.treeFeatureConfigClass.isInstance(featureConfig)) {
            String nameSpace = "";
            final ConfiguredFeature<?, ?> nextConfiguredFeature = configuredFeature.getFeatures().findFirst().get();
            final FeatureConfiguration nextFeatureConfig = nextConfiguredFeature.config();
            final ResourceLocation featureRegistryName = ForgeRegistries.FEATURES.getKey(nextConfiguredFeature.feature());
            if (featureRegistryName != null) {
                nameSpace = featureRegistryName.getNamespace();
            }
            if (this.treeFeatureConfigClass.isInstance(nextFeatureConfig) && !nameSpace.equals("") &&
                    featureCancellations.shouldCancelNamespace(nameSpace)) {
                return true; // Removes any individual trees.
            } else if (nextFeatureConfig instanceof RandomFeatureConfiguration) {
                // Removes configuredFeature if it contains trees.
                return this.doesContainTrees((RandomFeatureConfiguration) nextFeatureConfig, featureCancellations);
            }
        }

        return false;
    }


    private boolean doesContainTrees(RandomFeatureConfiguration featureConfig, BiomePropertySelectors.NormalFeatureCancellation featureCancellations) {
        for (WeightedPlacedFeature feature : featureConfig.features) {
            final PlacedFeature currentConfiguredFeature = feature.feature.value();
            final ResourceLocation featureRegistryName = ForgeRegistries.FEATURES.getKey(currentConfiguredFeature.getFeatures().findFirst().get().feature());

            if (this.treeFeatureConfigClass.isInstance(currentConfiguredFeature.placement()) && featureRegistryName != null &&
                    featureCancellations.shouldCancelNamespace(featureRegistryName.getNamespace())) {

                return true;
            }
        }
        return false;
    }

    private boolean skip(FeatureConfiguration feature) {
        if (feature instanceof TreeConfiguration treeConfiguration)
            return treeConfigurationList.stream().anyMatch(treeConfiguration::equals);
        return false;
    }

}

