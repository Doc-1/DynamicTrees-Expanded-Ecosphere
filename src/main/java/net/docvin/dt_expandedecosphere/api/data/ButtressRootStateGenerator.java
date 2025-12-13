package net.docvin.dt_expandedecosphere.api.data;

import com.ferreusveritas.dynamictrees.api.data.Generator;
import com.ferreusveritas.dynamictrees.data.provider.DTBlockStateProvider;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import net.docvin.dt_expandedecosphere.client.model.generator.RootLoaderBuilder;
import net.docvin.dt_expandedecosphere.common.block.ButtressRootBlock;
import net.docvin.dt_expandedecosphere.tree.family.suppliers.ButtressRootBlockSupplier;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ButtressRootStateGenerator implements Generator<DTBlockStateProvider, Family> {

    public static final DependencyKey<ButtressRootBlock> BUTTRESS_ROOT = new DependencyKey<>("buttress_root");
    public static final DependencyKey<Block> PRIMITIVE_LOG = new DependencyKey<>("primitive_log");

    @Override
    public void generate(DTBlockStateProvider provider, Family input, Dependencies dependencies) {
        final ButtressRootBlock buttressRoot = dependencies.get(BUTTRESS_ROOT);
        provider.simpleBlock(buttressRoot,
                provider.models().getBuilder(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(buttressRoot)).getPath())
                        .customLoader(RootLoaderBuilder::buttressRoot)
                        .texture("bark", input.getTexturePath(Family.BRANCH)
                                .orElse(provider.block(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(dependencies.get(PRIMITIVE_LOG))))
                                )).end()
        );
    }

    @Override
    public @NotNull Dependencies gatherDependencies(@NotNull Family input) {
        return new Dependencies()
                .append(BUTTRESS_ROOT, ButtressRootBlockSupplier.getButtressRoot(input))
                .append(PRIMITIVE_LOG, input.getPrimitiveLog());
    }

}
