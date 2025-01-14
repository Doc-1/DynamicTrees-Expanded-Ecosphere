package net.docvin.dt_expandedecosphere;

import com.ferreusveritas.dynamictrees.api.GatherDataHelper;
import com.ferreusveritas.dynamictrees.api.registry.RegistryEvent;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.rooty.SoilProperties;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import com.mojang.logging.LogUtils;
import net.docvin.dt_expandedecosphere.growthlogic.GrowthLogicKits;
import net.docvin.dt_expandedecosphere.systems.genfeature.DTExpandedEcosphereGenFeatures;
import net.docvin.dt_expandedecosphere.worldgen.cancllers.PlacedTreeCanceller;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


@Mod(DynamicTreesExpandedEcosphere.MOD_ID)
public class DynamicTreesExpandedEcosphere {

    public static final String MOD_ID = "dt_expandedecosphere";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public DynamicTreesExpandedEcosphere() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::gatherData);


        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);
    }

    public static ResourceLocation location(final String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    private void clientSetup(final FMLClientSetupEvent event) {

    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void gatherData(final GatherDataEvent event) {
        GatherDataHelper.gatherAllData(MOD_ID, event,
                SoilProperties.REGISTRY,
                Family.REGISTRY,
                Species.REGISTRY,
                Fruit.REGISTRY,
                LeavesProperties.REGISTRY,
                CapProperties.REGISTRY);
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class DTExpandedEcosphereRegistries {
        public static final FeatureCanceller TREE_CANCELLER = new PlacedTreeCanceller(new ResourceLocation(MOD_ID + ":expandedecosphere"));

        @SubscribeEvent
        public static void onFeatureCancellerRegistry(final RegistryEvent<FeatureCanceller> event) {
            event.getRegistry().registerAll(TREE_CANCELLER);
        }

        @SubscribeEvent
        public static void onGrowthLogicKitRegistry(final RegistryEvent<GrowthLogicKit> event) {
            GrowthLogicKits.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerGenFeature(final RegistryEvent<GenFeature> event) {
            DTExpandedEcosphereGenFeatures.registerGenFeatures(event);
        }
    }
}
