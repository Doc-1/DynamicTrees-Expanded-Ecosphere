package net.docvin.dt_expandedecosphere.client.model.generator;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.docvin.dt_expandedecosphere.client.event.BakedModelEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.LinkedHashMap;
import java.util.Map;

public class RootLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {

    private final Map<String, String> textures = new LinkedHashMap<>();

    public RootLoaderBuilder(ResourceLocation loaderId, BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
        super(loaderId, parent, existingFileHelper);
    }

    public static RootLoaderBuilder buttressRoot(BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
        return new RootLoaderBuilder(BakedModelEvent.BUTTRESS_ROOT, parent, existingFileHelper);
    }

    public RootLoaderBuilder texture(String key, ResourceLocation location) {
        this.textures.put(key, location.toString());
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        json = super.toJson(json);

        final JsonObject textures = new JsonObject();
        this.textures.forEach((key, location) ->
                textures.add(key, new JsonPrimitive(location)));
        json.add("textures", textures);

        return json;
    }

}
