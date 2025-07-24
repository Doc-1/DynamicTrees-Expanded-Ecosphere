package net.docvin.dt_expandedecosphere.mixins;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import net.docvin.dt_expandedecosphere.resources.RegisterJSONAppliers;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrowthLogicKit.class)
public abstract class GrowthLogicKitMixin {

//    @Final
//    @Mutable
//    @Shadow(remap = false)
//    public static GrowthLogicKit DEFAULT = new net.docvin.dt_expandedecosphere.growthlogic.GrowthLogicKit(DTTrees.NULL) {
//        @Override
//        public @NotNull GrowthLogicKitConfiguration getDefaultConfiguration() {
//            return this.defaultConfiguration;
//        }
//    };

    @Inject(method = "populateDirectionProbabilityMap", at = @At("HEAD"), remap = false)
    public void populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context, CallbackInfoReturnable<int[]> cir) {
        final GrowSignal signal = context.signal();
        BlockPos rootPos = signal.rootPos;
        int dist = (context.pos().getY() - rootPos.getY());
        final int[] probMap = context.probMap();

        if (dist >= RegisterJSONAppliers.getMaxGrowthHeight(context.species()) && probMap[1] > 0 && context.signal().numTurns == 0)
            signal.energy = 0;
    }

}
