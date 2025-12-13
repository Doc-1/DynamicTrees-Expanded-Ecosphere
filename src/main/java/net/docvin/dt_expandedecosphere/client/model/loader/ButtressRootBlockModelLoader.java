package net.docvin.dt_expandedecosphere.client.model.loader;

import com.ferreusveritas.dynamictrees.models.geometry.BranchBlockModelGeometry;
import com.ferreusveritas.dynamictrees.models.loader.BranchBlockModelLoader;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.docvin.dt_expandedecosphere.client.model.geometry.ButtressRootBlockModelGeometry;
import org.jetbrains.annotations.NotNull;

public class ButtressRootBlockModelLoader extends BranchBlockModelLoader {

    @Override
    public @NotNull BranchBlockModelGeometry read(@NotNull JsonObject modelObject, @NotNull JsonDeserializationContext deserializationContext) {
        final JsonObject textures = this.getTexturesObject(modelObject);
        return new ButtressRootBlockModelGeometry(this.getBarkTextureLocation(textures));
    }

    @Override
    protected @NotNull String getModelTypeName() {
        return "Buttress Root";
    }
}
