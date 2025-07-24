package net.docvin.dt_expandedecosphere.resources;


import com.ferreusveritas.dynamictrees.api.applier.ApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.deserialisation.PropertyAppliers;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.google.gson.JsonElement;
import net.docvin.dt_expandedecosphere.DynamicTreesExpandedEcosphere;
import net.docvin.dt_expandedecosphere.tree.species.SubmersibleSpecies;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = DynamicTreesExpandedEcosphere.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterJSONAppliers {

    private static final HashMap<String, Integer> maxGrowHeightTreeMap = new HashMap<>();

    @SubscribeEvent
    public static void registerAppliersSpecies(final ApplierRegistryEvent.Reload<Species, JsonElement> event) {
        registerSpeciesAppliers(event.getAppliers());
    }

    public static void registerSpeciesAppliers(PropertyAppliers<Species, JsonElement> appliers) {
        appliers.register("submerged_species", SubmersibleSpecies.class, Species.class,
                SubmersibleSpecies::setSubmergedSpecies);

        appliers.register("max_grow_height", Integer.class, RegisterJSONAppliers::setMaxGrowthHeight);
    }

    @SubscribeEvent
    public static void registerAppliersSpecies(final ApplierRegistryEvent.GatherData<Species, JsonElement> event) {
        registerSpeciesAppliers(event.getAppliers());
    }

    public static void setMaxGrowthHeight(Species species, int maxGrowthHeight) {
        maxGrowHeightTreeMap.put(species.getRegistryName().getPath(), maxGrowthHeight);
    }

    public static int getMaxGrowthHeight(Species species) {
        String path = species.getRegistryName().getPath();
        return maxGrowHeightTreeMap.containsKey(path) ? maxGrowHeightTreeMap.get(species.getRegistryName().getPath()) : 999;

    }
}
