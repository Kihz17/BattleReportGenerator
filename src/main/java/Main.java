import battlereports.BattleReport;
import battlereports.FakeBattleReportGenerator;
import com.google.gson.JsonElement;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by Kihz on 2021-11-07
 */
public class Main {

    private static final String POST_ADDRESS = "https://minecraft-game.dedicateddevelopers.us/api/battles";

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Invalid arguments!");
            System.out.println("Missing number of reports to generate!");
            System.out.println("Format: \"java -jar <jarFileName> <numberOfReports>\"");
            return;
        }

        try {
            int reportsToGenerate = Integer.parseInt(args[0]);
            for(int i = 0; i < reportsToGenerate; i++) {
                BattleReport battleReport = FakeBattleReportGenerator.generateReport();
                sendReport(battleReport);
            }

            System.out.println("Sent " + reportsToGenerate + " reports.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean sendReport(BattleReport report) {
        try {
            HttpsURLConnection httpsConnection = (HttpsURLConnection) new URL(POST_ADDRESS).openConnection();
            if(httpsConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("Website rejected our battle report, this report was lost. (Error Code: " + httpsConnection.getResponseCode() + ")");
                return false;
            }

            JsonElement reportJson = report.save();
            byte[] rawData = reportJson.toString().getBytes(StandardCharsets.UTF_8);
            httpsConnection.setDoOutput(true);
            httpsConnection.setInstanceFollowRedirects(false);
            httpsConnection.setRequestMethod("POST");
            httpsConnection.setRequestProperty("Content-Type", "application/json");
            httpsConnection.setRequestProperty( "charset", "utf-8");
            httpsConnection.setRequestProperty("Content-Length", Integer.toString(rawData.length));
            httpsConnection.getOutputStream().write(rawData);
            return true;
        } catch (Exception e) {
            System.out.println("Error while trying to post a BattleReport.");
            e.printStackTrace();
            return false;
        }
    }
}
