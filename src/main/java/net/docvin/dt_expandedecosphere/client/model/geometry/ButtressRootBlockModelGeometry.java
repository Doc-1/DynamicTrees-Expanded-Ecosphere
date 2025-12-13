package net.docvin.dt_expandedecosphere.client.model.geometry;

import com.ferreusveritas.dynamictrees.models.geometry.BranchBlockModelGeometry;
import net.docvin.dt_expandedecosphere.client.model.baked.ButtressRootBlockBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ButtressRootBlockModelGeometry extends BranchBlockModelGeometry {
    public ButtressRootBlockModelGeometry(final ResourceLocation barkResLoc) {
        super(barkResLoc, null, null, false);
    }

    @Override
    public @NotNull BakedModel bake(@NotNull IGeometryBakingContext owner, @NotNull ModelBaker baker, @NotNull Function<Material, TextureAtlasSprite> spriteGetter, @NotNull ModelState modelTransform,
                                    @NotNull ItemOverrides overrides, @NotNull ResourceLocation modelLocation) {
        return new ButtressRootBlockBakedModel(modelLocation, this.barkTextureLocation, spriteGetter);
    }
}