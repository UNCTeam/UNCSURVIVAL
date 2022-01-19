package teamunc.uncsurvival.utils.serializerAdapter;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;

import java.lang.reflect.Type;

public class GameConfigurationSerializer implements JsonSerializer<GameConfiguration> {
    @Override
    public JsonElement serialize(GameConfiguration gameConfiguration, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonGameConfiguration = new JsonObject();
        jsonGameConfiguration.addProperty("datePhase2", gameConfiguration.getDatePhase2().toString());
        jsonGameConfiguration.addProperty("datePhase3", gameConfiguration.getDatePhase3().toString());
        jsonGameConfiguration.addProperty("dateFin", gameConfiguration.getDateFin().toString());
        JsonArray goalItems = new JsonArray();
        for(Material item : gameConfiguration.getGoalItems()) {
            goalItems.add(item.toString());
        }
        jsonGameConfiguration.add("goalItems", goalItems);

        Bukkit.getConsoleSender().sendMessage("LALALA SERIALIZE");

        JsonArray priceItems = new JsonArray();
        for(Integer price : gameConfiguration.getGoalItemsPrices()) {
            priceItems.add(price);
        }
        jsonGameConfiguration.add("goalItemsPrices", goalItems);
        return jsonGameConfiguration;
    }
}
