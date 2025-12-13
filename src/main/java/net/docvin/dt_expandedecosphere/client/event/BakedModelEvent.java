package net.docvin.dt_expandedecosphere.client.event;

import net.docvin.dt_expandedecosphere.DynamicTreesExpandedEcosphere;
import net.docvin.dt_expandedecosphere.client.model.loader.ButtressRootBlockModelLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DynamicTreesExpandedEcosphere.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BakedModelEvent {

    //These locs are accessed by the model data generators
    public static final ResourceLocation BUTTRESS_ROOT = DynamicTreesExpandedEcosphere.location("buttress_root");

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelEvent.RegisterGeometryLoaders event) {
        // Register model loaders for baked models.
        event.register(BUTTRESS_ROOT.getPath(), new ButtressRootBlockModelLoader());
    }
}
