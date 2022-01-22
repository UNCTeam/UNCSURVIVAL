package teamunc.uncsurvival.utils.serializerAdapter;

import com.google.gson.*;
import org.bukkit.Location;

import java.lang.reflect.Type;

public class LocationSerializer implements JsonSerializer<Location>, JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }
}
