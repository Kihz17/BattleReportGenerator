package battlereports;

import jsontools.containers.JsonList;
import jsontools.containers.JsonMap;
import utilities.Utils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kihz on 2021-11-07
 */
public class FakeBattleReportGenerator {

    public static String[] GUILD_TEST_NAMES = new String[] {"NightFall", "ISMN", "VK", "Valor", "AK47", "Rune"};
    public static String[] USERNAMES = new String[] {
            "Rockouthard", "xgirble1", "senoxe", "Zercon", "RevoSend",
            "Elie", "Neoka", "Kihz", "TTSWumbo", "Serioum", "BillySanchezz",
            "Darren", "Pirro", "Fifty50", "STR8", "Liam", "NOTTHEYADDAS",
            "murtaa", "Snake", "Block", "MC45w", "Take", "Table", "Strafed",
            "simplenm", "AnsonWang", "kuhuru", "Disregard", "moose_breeder",
            "GuildUpdate", "Pray", "Goompy", "SilentFeedz", "Flex0", "Violet"};

    public static BattleReport generateReport() {
        int battleDurationSeconds = Utils.randInt(120, 300);
        BattleReport battleReport = new BattleReport("Test Location", battleDurationSeconds);

        // Setup some guilds
        final int numberOfGuilds = Utils.randInt(2, GUILD_TEST_NAMES.length);
        final String[] guilds = new String[numberOfGuilds];
        for(int i = 0; i < numberOfGuilds; i++)
            guilds[i] = GUILD_TEST_NAMES[i];

        // Setup some players
        Map<String, List<BattleReport.BattlePlayer>> battleMap = new HashMap<>();
        final int numberOfParticipants = Utils.randInt(2, USERNAMES.length);
        for(int i = 0; i < numberOfParticipants; i++) {
            String guild = null;
            if(i <= guilds.length - 1) {
                guild = guilds[i]; // Ensure we have at least one entry in each guild
            } else {
                guild = guilds[Utils.randInt(0, guilds.length - 1)];
            }

            String name = USERNAMES[i];
            int radiance = Utils.randInt(20, 250);
            PlayerInventory playerInventory = PlayerInventory.generateRandomInventory();

            battleMap.computeIfAbsent(guild, k -> new ArrayList<>()).add(new BattleReport.BattlePlayer(name, guild, radiance, playerInventory));
        }

        // Make the players damage each other
        for(List<BattleReport.BattlePlayer> battlePlayers : battleMap.values()) {
            for(BattleReport.BattlePlayer battlePlayer : battlePlayers) {

                // Choose a guild to attack
                List<String> guildsToAttack = new ArrayList<>(battleMap.keySet());
                guildsToAttack.remove(battlePlayer.getGuild());
                String guildToAttack = guildsToAttack.get(Utils.randInt(0, guildsToAttack.size() - 1));

                // Get a random player to "attack" from the guild
                List<BattleReport.BattlePlayer> playersToAttack = battleMap.get(guildToAttack);
                if(playersToAttack == null) {
                    System.out.println("fa");
                }
                BattleReport.BattlePlayer playerToAttack = playersToAttack.get(Utils.randInt(0, playersToAttack.size() - 1));

                int damage = Utils.randInt(10, 5000);
                battlePlayer.addDamage(playerToAttack.getName(), damage);
                playerToAttack.addDamageTaken(battlePlayer.getName(), damage);

                boolean didPlayerDie = Utils.randChance(0.1D); // 10% chance the player died
                if(didPlayerDie) { // Player died, add it to the kill report
                    battlePlayer.getPlayersKilled().add(new BattleReport.KillReport(battlePlayer.getName(),
                            playerToAttack.getName(),
                            System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(Utils.randInt(0, battleDurationSeconds))));
                }
            }
        }

        // Add the generated players to the report
        battleMap.values().forEach(playerList -> playerList.forEach(player -> battleReport.getParticipants().put(UUID.randomUUID().toString(), player)));

        return battleReport;
    }
}
