package teamunc.uncsurvival.utils.serializerAdapter;

import com.google.gson.*;
import org.bukkit.Material;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class GameConfigurationDeserializer implements JsonDeserializer<GameConfiguration> {
    @Override
    public GameConfiguration deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject gameConfirurationJson = jsonElement.getAsJsonObject();
        LocalDateTime datePhase2 = LocalDateTime.parse(gameConfirurationJson.get("datePhase2").getAsString());
        LocalDateTime datePhase3 = LocalDateTime.parse(gameConfirurationJson.get("datePhase3").getAsString());
        LocalDateTime dateFin = LocalDateTime.parse(gameConfirurationJson.get("dateFin").getAsString());

        ArrayList<Material> materials = new ArrayList<>();
        ArrayList<Integer> prices = new ArrayList<>();

        JsonArray goalItems = gameConfirurationJson.getAsJsonArray("goalItems");
        for(int i = 0; i<goalItems.size();i++) {
            materials.add(Material.valueOf(goalItems.get(i).getAsString()));
        }

        JsonArray priceItems = gameConfirurationJson.getAsJsonArray("goalItemsPrices");
        for(int i = 0; i<goalItems.size();i++) {
            prices.add(priceItems.get(0).getAsInt());
        }
        GameConfiguration gameConfiguration = new GameConfiguration(datePhase2, datePhase3, dateFin, materials, prices);
        return gameConfiguration;
    }
}
