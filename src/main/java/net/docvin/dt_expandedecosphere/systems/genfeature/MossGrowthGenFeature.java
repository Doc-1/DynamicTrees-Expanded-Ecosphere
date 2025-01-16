package net.docvin.dt_expandedecosphere.systems.genfeature;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import net.docvin.dt_expandedecosphere.utils.VoxmapUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MossGrowthGenFeature extends GenFeature {

    public static final ConfigurationProperty<Block> BLOCK = ConfigurationProperty.block("block");
    public static final ConfigurationProperty<Integer> RADIUS = ConfigurationProperty.integer("radius");
    public static final ConfigurationProperty<Integer> GEN_PLACEMENT = ConfigurationProperty.integer("gen_placement");
    public static final ConfigurationProperty<Float> GEB_PLACE_CHANCE = ConfigurationProperty.floatProperty("gen_place_chance");

    public MossGrowthGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(BLOCK, PLACE_CHANCE, RADIUS, GEN_PLACEMENT, GEB_PLACE_CHANCE);
    }

    @Override
    public @NotNull GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration().with(BLOCK, Blocks.MOSS_CARPET).with(PLACE_CHANCE, 0.1F).with(RADIUS, 6).with(GEN_PLACEMENT, 20).with(GEB_PLACE_CHANCE, 0.8F);
    }

    @Override
    protected boolean postGenerate(@NotNull GenFeatureConfiguration configuration, PostGenerationContext context) {
        BlockState blockState = configuration.get(BLOCK).defaultBlockState();
        int max = configuration.get(GEN_PLACEMENT);
        float chance = configuration.get(GEB_PLACE_CHANCE);
        LevelAccessor level = context.level();
        SimpleVoxmap voxmap = VoxmapUtils.getCircleVoxmap(configuration.get(RADIUS));
        BlockPos rootPos = context.pos().below(2);
        int total = 0;
        for (SimpleVoxmap.Cell cell : voxmap.getAllNonZeroCells())
            if (total <= max && level.getRandom().nextFloat() >= chance && placeMoss(level, rootPos.offset(cell.getPos()), blockState))
                total++;
        return true;
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        BlockState blockState = configuration.get(BLOCK).defaultBlockState();
        int max = configuration.get(GEN_PLACEMENT);
        float chance = configuration.get(PLACE_CHANCE);
        LevelAccessor level = context.level();
        SimpleVoxmap voxmap = VoxmapUtils.getCircleVoxmap(configuration.get(RADIUS));
        BlockPos rootPos = context.pos().below(2);

        for (SimpleVoxmap.Cell cell : voxmap.getAllNonZeroCells())
            if (level.getRandom().nextFloat() >= chance && placeMoss(level, rootPos.offset(cell.getPos()), blockState))
                break;
        return true;
    }

    private boolean placeMoss(LevelAccessor level, BlockPos pos, BlockState blockState) {
        for (int y = 0; y <= 6; y++) {
            BlockState above = level.getBlockState(pos.above());
            BlockState below = level.getBlockState(pos);
            if (!below.canBeReplaced() && !below.equals(blockState) && !TreeHelper.isTreePart(below) && below.getFluidState().isEmpty() && above.isAir() && above.getFluidState().isEmpty()) {
                level.setBlock(pos.above(), blockState, 3);
                return true;
            }
            pos = pos.above();
        }
        return false;
    }
}
