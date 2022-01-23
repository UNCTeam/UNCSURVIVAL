package teamunc.uncsurvival.utils.serializerAdapter;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class GameConfigurationSerializer implements JsonSerializer<GameConfiguration>, JsonDeserializer<GameConfiguration> {
    @Override
    public JsonElement serialize(GameConfiguration gameConfiguration, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonGameConfiguration = new JsonObject();
        jsonGameConfiguration.addProperty("datePhase2", gameConfiguration.getDatePhase2().toString());
        jsonGameConfiguration.addProperty("datePhase3", gameConfiguration.getDatePhase3().toString());
        jsonGameConfiguration.addProperty("dateFin", gameConfiguration.getDateFin().toString());

        JsonArray priceItems = new JsonArray();
        for(Integer price : gameConfiguration.getGoalItemsPrices()) {
            priceItems.add(price);
        }

        return jsonGameConfiguration;
    }

    @Override
    public GameConfiguration deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject gameConfirurationJson = jsonElement.getAsJsonObject();
        LocalDateTime datePhase2 = LocalDateTime.parse(gameConfirurationJson.get("datePhase2").getAsString());
        LocalDateTime datePhase3 = LocalDateTime.parse(gameConfirurationJson.get("datePhase3").getAsString());
        LocalDateTime dateFin = LocalDateTime.parse(gameConfirurationJson.get("dateFin").getAsString());

        ArrayList<Integer> prices = new ArrayList<>();

        JsonArray priceItems = gameConfirurationJson.getAsJsonArray("goalItemsPrices");
        for(int i = 0; i<priceItems.size();i++) {
            prices.add(priceItems.get(i).getAsInt());
        }
        GameConfiguration gameConfiguration = new GameConfiguration(datePhase2, datePhase3, dateFin, prices);
        return gameConfiguration;
    }
}
