package battlereports;

import battlereports.items.Items;
import com.google.gson.JsonObject;

/**
 * Created by Kihz on 2021-11-07
 */
public class PlayerInventory {
    private JsonObject[] inventoryContents = new JsonObject[9];
    // 0 = main hand
    // 1 = off hand
    // 2 = helmet
    // 3 = chestplate
    // 4 = leggings
    // 5 = boots
    // 6 = accessory 1
    // 7 = accessory 2
    // 8 = accessory 3

    public static PlayerInventory generateRandomInventory() {
        PlayerInventory playerInventory = new PlayerInventory();
        playerInventory.inventoryContents[0] = Items.getRandomWeapon();
        playerInventory.inventoryContents[1] = Items.getRandomWeapon();
        playerInventory.inventoryContents[2] = Items.getRandomHelmet();
        playerInventory.inventoryContents[3] = Items.getRandomChestplate();
        playerInventory.inventoryContents[4] = Items.getRandomLeggings();
        playerInventory.inventoryContents[5] = Items.getRandomBoots();

        return playerInventory;
    }

    public JsonObject getHelmet() {
        return inventoryContents[2];
    }

    public JsonObject getChestplate() {
        return inventoryContents[3];
    }

    public JsonObject getLeggings() {
        return inventoryContents[4];
    }

    public JsonObject getBoots() {
        return inventoryContents[5];
    }

    public JsonObject getItemInMainHand() {
        return inventoryContents[0];
    }

    public JsonObject getItemInOffHand() {
        return inventoryContents[1];
    }

    public JsonObject getItem(int slot) {
        return inventoryContents[slot];
    }
}
