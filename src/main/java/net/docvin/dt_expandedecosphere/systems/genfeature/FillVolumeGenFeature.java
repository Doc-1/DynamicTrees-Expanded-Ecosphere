package net.docvin.dt_expandedecosphere.systems.genfeature;

import com.ferreusveritas.dynamictrees.systems.genfeature.ClearVolumeGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PreGenerationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class FillVolumeGenFeature extends ClearVolumeGenFeature {

    public FillVolumeGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected @NotNull BlockPos preGenerate(GenFeatureConfiguration configuration, PreGenerationContext context) {
        final BlockPos rootPos = context.pos();

        // Erase a volume of blocks that could potentially get in the way.
        for (BlockPos pos : BlockPos.betweenClosed(
                rootPos.offset(new Vec3i(-1, 1, -1)),
                rootPos.offset(new Vec3i(1, configuration.get(HEIGHT), 1))
        )) {
            if (context.level().getBlockState(pos).getFluidState().isEmpty())
                context.level().removeBlock(pos, false);
            else
                context.level().setBlock(pos, Blocks.WATER.defaultBlockState(), 2);
        }

        return rootPos;
    }
}
