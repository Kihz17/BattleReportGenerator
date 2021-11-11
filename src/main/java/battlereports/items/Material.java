package battlereports.items;

import utilities.Utils;

/**
 * Created by Kihz on 2021-11-07
 */
public enum Material {

    TRIDENT,

    WOODEN_SWORD,
    WOODEN_AXE,
    WOODEN_HOE,

    STONE_SWORD,
    STONE_AXE,
    STONE_HOE,

    IRON_SWORD,
    IRON_AXE,
    IRON_HOE,

    DIAMOND_SWORD,
    DIAMOND_AXE,
    DIAMOND_HOE,

    NETHERITE_SWORD,
    NETHERITE_AXE,
    NETHERITE_HOE,

    LEATHER_HELMET,
    LEATHER_CHESTPLATE,
    LEATHER_LEGGINGS,
    LEATHER_BOOTS,

    CHAIN_HELMET,
    CHAIN_CHESTPLATE,
    CHAIN_LEGGINGS,
    CHAIN_BOOTS,

    IRON_HELMET,
    IRON_CHESTPLATE,
    IRON_LEGGINGS,
    IRON_BOOTS,

    DIAMOND_HELMET,
    DIAMONDCHESTPLATE,
    DIAMONDLEGGINGS,
    DIAMOND_BOOTS,

    NETHERITE_HELMET,
    NETHERITE_CHESTPLATE,
    NETHERITE_LEGGINGS,
    NETHERITE_BOOTS;

    public static Material getRandomWeapon() {
        return values()[Utils.randInt(0, 15)];
    }

    public static Material getRandomArmor() {
        return values()[Utils.randInt(15, values().length - 1)];
    }
}
