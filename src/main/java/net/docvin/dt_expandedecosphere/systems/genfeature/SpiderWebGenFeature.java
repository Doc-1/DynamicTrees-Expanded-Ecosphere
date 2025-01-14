package net.docvin.dt_expandedecosphere.systems.genfeature;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.systems.nodemapper.FindEndsNode;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpiderWebGenFeature extends GenFeature {

    public static final ConfigurationProperty<Block> WEB_BLOCK = ConfigurationProperty.block("cob_web");


    public SpiderWebGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(WEB_BLOCK, PLACE_CHANCE, QUANTITY);
    }

    @Override
    public @NotNull GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration().with(WEB_BLOCK, Blocks.COBWEB).with(PLACE_CHANCE, 0.8F).with(QUANTITY, 4);
    }


    @Override
    protected boolean postGenerate(@NotNull GenFeatureConfiguration configuration, PostGenerationContext context) {
        if (!context.endPoints().isEmpty()) {
            int qty = configuration.get(QUANTITY);
            final LevelAccessor level = context.level();
            for (int i = 0; i < qty; i++) {
                final BlockPos endPoint = context.endPoints().get(context.random().nextInt(context.endPoints().size()));
                this.placeBeeNestInValidPlace(configuration, level, endPoint, true, context.random());
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean postGrow(@NotNull GenFeatureConfiguration configuration, PostGrowContext context) {
        final LevelAccessor level = context.level();

        if (level.getRandom().nextFloat() < 0.05) {
            final BlockPos rootPos = context.pos();
            final FindEndsNode endFinder = new FindEndsNode();
            TreeHelper.startAnalysisFromRoot(level, rootPos, new MapSignal(endFinder));
            final List<BlockPos> endPoints = endFinder.getEnds();
            Block nestBlock = configuration.get(WEB_BLOCK);
            int qty = configuration.get(QUANTITY);
            if (!endPoints.isEmpty()) {
                for (int i = 0; i < qty; i++) {
                    final BlockPos endPoint = endPoints.get(level.getRandom().nextInt(endPoints.size()));
                    if (!nestAlreadyPresent(level, nestBlock, rootPos))
                        this.placeBeeNestInValidPlace(configuration, context.level(), endPoint, false, context.random());
                }
            }
            return true;
        } else
            return false;
    }

    private boolean nestAlreadyPresent(LevelAccessor world, Block webBlock, BlockPos rootPos) {
        for (int y = 2; y < 64; y++) {
            BlockPos trunkPos = rootPos.above(y);
            for (Direction dir : CoordUtils.HORIZONTALS)
                return world.getBlockState(trunkPos.relative(dir)).getBlock() == webBlock;


        }
        return false;
    }

    private void placeBeeNestInValidPlace(GenFeatureConfiguration configuration, LevelAccessor world, BlockPos rootPos, boolean worldGen, RandomSource random) {
        Block nestBlock = configuration.get(WEB_BLOCK);
        placeBeeNestWithBees(world, nestBlock, rootPos);
    }

    private void placeBeeNestWithBees(LevelAccessor world, Block nestBlock, BlockPos pos) {
        BlockState nestState = nestBlock.defaultBlockState();
        world.setBlock(pos, nestState, 3);
    }


}
