package net.docvin.dt_expandedecosphere.resources;


import com.ferreusveritas.dynamictrees.api.applier.ApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.deserialisation.PropertyAppliers;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.google.gson.JsonElement;
import net.docvin.dt_expandedecosphere.DynamicTreesExpandedEcosphere;
import net.docvin.dt_expandedecosphere.tree.species.SubmersibleSpecies;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DynamicTreesExpandedEcosphere.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterJSONAppliers {

    @SubscribeEvent
    public static void registerAppliersSpecies(final ApplierRegistryEvent.Reload<Species, JsonElement> event) {
        registerSpeciesAppliers(event.getAppliers());
    }

    public static void registerSpeciesAppliers(PropertyAppliers<Species, JsonElement> appliers) {
        appliers.register("submerged_species", SubmersibleSpecies.class, Species.class,
                SubmersibleSpecies::setSubmergedSpecies);
    }

    @SubscribeEvent
    public static void registerAppliersSpecies(final ApplierRegistryEvent.GatherData<Species, JsonElement> event) {
        registerSpeciesAppliers(event.getAppliers());
    }

}
