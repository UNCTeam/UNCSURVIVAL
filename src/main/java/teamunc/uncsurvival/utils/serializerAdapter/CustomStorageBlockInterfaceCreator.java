package teamunc.uncsurvival.utils.serializerAdapter;

import com.google.gson.*;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;
import teamunc.uncsurvival.logic.customBlock.customStorageBlock.CustomStorageBlock;
import teamunc.uncsurvival.logic.customBlock.customStorageBlock.MincerBlock;

import java.lang.reflect.Type;

public class CustomStorageBlockInterfaceCreator implements JsonDeserializer<CustomStorageBlock> {

    @Override
    public CustomStorageBlock deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        CustomBlockType types = CustomBlockType.valueOf(jsonObj.get("customBlockType").getAsString());
        switch (types) {
            case MINCER_BLOCK:
                return jsonDeserializationContext.deserialize(jsonElement, MincerBlock.class);
            case COOK_BLOCk:
                return null;
            case GROWTH_BLOCK:
                return null;
            case PROECTION_BLOCK:
                return null;
        }
        return null;
    }
}
