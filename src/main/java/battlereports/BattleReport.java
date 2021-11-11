package battlereports;

import battlereports.items.Items;
import com.google.gson.JsonObject;
import jsontools.Jsonable;
import jsontools.containers.JsonList;
import jsontools.containers.JsonMap;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Represents a report that contains information about a PvP battle.
 * Created by Kihz on 2021-06-08
 */
@Getter
public class BattleReport implements Jsonable {
    private final UUID battleId = UUID.randomUUID();
    private String location;
    private int durationSeconds;
    private JsonMap<BattlePlayer> participants = new JsonMap<>();

    public BattleReport(String location, int durationSeconds) {
        this.location = location;
        this.durationSeconds = durationSeconds;
    }
    /**
     * A class that represents a player that takes place in a battle
     */
    @Getter
    public static class BattlePlayer implements Jsonable {
        private final String name;
        private final String guild;
        private final int radiance;
        private final JsonMap<JsonObject> gear = new JsonMap<>();
        private final JsonMap<Integer> damageDealt = new JsonMap<>();
        private final JsonMap<Integer> damageTaken = new JsonMap<>();
        private final JsonList<KillReport> playersKilled = new JsonList<>(); // A list of players we killed

        public BattlePlayer(String name, String guild, int radiance, PlayerInventory inventory, String target, int damage) {
            this.name = name;
            this.guild = guild;
            this.radiance = radiance;
            this.damageDealt.put(target, damage);
            parseInventory(inventory);
        }

        public BattlePlayer(String name, String guild, int radiance, PlayerInventory inventory) {
            this.name = name;
            this.guild = guild;
            this.radiance = radiance;
            parseInventory(inventory);
        }

        /**
         * Add damage to this battle player
         * @param damage The damage to add
         */
        public void addDamage(String target, int damage) {
            int damageDealt = this.damageDealt.containsKey(target) ? this.damageDealt.get(target) : 0;
            if(damageDealt == 0) {
                this.damageDealt.put(target, damage);
            } else {
                this.damageDealt.put(target, damageDealt + damage);
            }
        }

        /**
         * Add damage taken to this battle player
         * @param damage The damage to add
         */
        public void addDamageTaken(String source, int damage) {
            int damageDealt = this.damageTaken.containsKey(source) ? this.damageTaken.get(source) : 0;
            if(damageDealt == 0) {
                this.damageTaken.put(source, damage);
            } else {
                this.damageTaken.put(source, damageDealt + damage);
            }
        }

        /**
         * Parse a player inventory to populate our gear map
         * @param inventory The inventory to parse
         */
        private void parseInventory(PlayerInventory inventory) {
            gear.put(BattleGearSlot.HELMET.name(), inventory.getHelmet());
            gear.put(BattleGearSlot.CHESTPLATE.name(), inventory.getChestplate());
            gear.put(BattleGearSlot.LEGGINGS.name(), inventory.getLeggings());
            gear.put(BattleGearSlot.BOOTS.name(), inventory.getBoots());
            gear.put(BattleGearSlot.HAND.name(), inventory.getItemInMainHand());
            gear.put(BattleGearSlot.OFFHAND.name(), inventory.getItemInOffHand());
            gear.put(BattleGearSlot.ACCESSORY_1.name(), inventory.getItem(6));
            gear.put(BattleGearSlot.ACCESSORY_2.name(), inventory.getItem(7));
            gear.put(BattleGearSlot.ACCESSORY_3.name(), inventory.getItem(8));
        }
    }

    /**
     * Represents information about a player kill
     */
    @AllArgsConstructor
    public static class KillReport implements Jsonable {
        private final String killer;
        private final String whoDied;
        private final long timeStamp;

        public KillReport(String killer, String whoDied) {
            this.killer = killer;
            this.whoDied = whoDied;
            this.timeStamp = System.currentTimeMillis();
        }
    }

    private enum BattleGearSlot {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS,
        HAND,
        OFFHAND,
        ACCESSORY_1,
        ACCESSORY_2,
        ACCESSORY_3,
    }
}
