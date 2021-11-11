package battlereports.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jsontools.JsonSerializer;
import jsontools.Jsonable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import utilities.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kihz on 2021-11-07
 */

public class Items implements Jsonable {
    public static final List<JsonObject> HELMETS = new ArrayList<>();
    public static final List<JsonObject> CHESTPLATES = new ArrayList<>();
    public static final List<JsonObject> LEGGINGS = new ArrayList<>();
    public static final List<JsonObject> BOOTS = new ArrayList<>();
    public static final List<JsonObject> WEAPONS = new ArrayList<>();

    private static final String ITEM_DIR = "items/";

    public static JsonObject getRandomHelmet() {
        return HELMETS.get(Utils.randInt(0, HELMETS.size() - 1)).deepCopy();
    }

    public static JsonObject getRandomChestplate() {
        return CHESTPLATES.get(Utils.randInt(0, CHESTPLATES.size() - 1)).deepCopy();
    }

    public static JsonObject getRandomLeggings() {
        return LEGGINGS.get(Utils.randInt(0, LEGGINGS.size() - 1)).deepCopy();
    }

    public static JsonObject getRandomBoots() {
        return BOOTS.get(Utils.randInt(0, BOOTS.size() - 1)).deepCopy();
    }

    public static JsonObject getRandomWeapon() {
        return WEAPONS.get(Utils.randInt(0, WEAPONS.size() - 1)).deepCopy();
    }

    static {
        JsonArray helmets = Utils.readJsonFile(new File(ITEM_DIR + "helmets.json")).getAsJsonObject().get("items").getAsJsonArray();
        for(JsonElement element : helmets)
            HELMETS.add(element.getAsJsonObject());

        JsonArray chestplates = Utils.readJsonFile(new File(ITEM_DIR + "chestplates.json")).getAsJsonObject().get("items").getAsJsonArray();
        for(JsonElement element : chestplates)
            CHESTPLATES.add(element.getAsJsonObject());

        JsonArray leggings = Utils.readJsonFile(new File(ITEM_DIR + "leggings.json")).getAsJsonObject().get("items").getAsJsonArray();
        for(JsonElement element : leggings)
            LEGGINGS.add(element.getAsJsonObject());

        JsonArray boots = Utils.readJsonFile(new File(ITEM_DIR + "boots.json")).getAsJsonObject().get("items").getAsJsonArray();
        for(JsonElement element : boots)
            BOOTS.add(element.getAsJsonObject());

        JsonArray weapons = Utils.readJsonFile(new File(ITEM_DIR + "weapons.json")).getAsJsonObject().get("items").getAsJsonArray();
        for(JsonElement element : weapons)
            WEAPONS.add(element.getAsJsonObject());
    }
}
