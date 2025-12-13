package net.docvin.dt_expandedecosphere.mixins;

import com.ferreusveritas.dynamictrees.api.data.Generator;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.data.provider.DTBlockStateProvider;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.util.MutableLazyValue;
import net.docvin.dt_expandedecosphere.api.data.ButtressRootStateGenerator;
import net.docvin.dt_expandedecosphere.common.block.ButtressRootBlock;
import net.docvin.dt_expandedecosphere.tree.family.suppliers.ButtressRootBlockSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

import static com.ferreusveritas.dynamictrees.util.ResourceLocationUtils.suffix;

@Mixin(Family.class)
public class FamilyMixin {


    @Unique
    protected MutableLazyValue<Generator<DTBlockStateProvider, Family>> dTExpandedEcosphere$buttressRootStateGenerator =
            MutableLazyValue.supplied(ButtressRootStateGenerator::new);


    @Inject(method = "generateStateData", at = @At("HEAD"), remap = false)
    public void generateStateData(DTBlockStateProvider provider, CallbackInfo ci) {
        Family family = dTExpandedEcosphere$getThis();
        this.dTExpandedEcosphere$buttressRootStateGenerator.get().generate(provider, family);
    }

    @Inject(method = "setupBlocks", at = @At("TAIL"), remap = false)
    public void setupBlocks(CallbackInfo ci) {
        Family family = dTExpandedEcosphere$getThis();
        if (ButtressRootBlockSupplier.hasButtressRoot(family)) {
            Supplier<ButtressRootBlock> supplier = RegistryHandler.addBlock(suffix(family.getRegistryName(), "_buttress_root"), () -> new ButtressRootBlock(family));
            ButtressRootBlockSupplier.addButtressRootProvider(family, supplier);
        }
    }

    @Unique
    public Family dTExpandedEcosphere$getThis() {
        return (Family) (Object) this;
    }


}
