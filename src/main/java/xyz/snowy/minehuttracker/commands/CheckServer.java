package xyz.snowy.minehuttracker.commands;

import xyz.snowy.minehuttracker.MinehutTracker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CheckServer implements CommandExecutor {
    public MinehutTracker plugin;
    public CheckServer(MinehutTracker plugin) {
        this.plugin = plugin;
    }
    private HttpURLConnection connection;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = Color(plugin.getConfig().getString("prefix"));
        String usage = ChatColor.translateAlternateColorCodes('&', prefix + " &8Usage: &7/checkserver <Server Name>");
        String error = ChatColor.translateAlternateColorCodes('&', prefix + " &cThere was a problem!");
        String consoleerror =  ChatColor.translateAlternateColorCodes('&',prefix + " &cConsole cannot use this commad");
        String notfound = ChatColor.translateAlternateColorCodes('&', prefix + " &cThat server couldn't be found");
        if (sender instanceof Player) {

            Player p = (Player) sender;
            if (args.length == 0) { p.sendMessage(usage); return true; } else {
                try {
                    URL url = new URL("https://api.minehut.com/server/" + args[0] + "?byName=true");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    int response_code = connection.getResponseCode();
                    if (response_code != 200) {
                        p.sendMessage(notfound);
                        return true;
                    } else {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        JSONObject Data = new JSONObject(response.toString());
                        JSONObject serverData = Data.getJSONObject("server");
                        Bukkit.getLogger().info("[Minehut Tracker Debug] " + serverData);
                        p.sendMessage(Color(prefix + " &fServer Name: &l" + args[0]));
                        p.sendMessage(Color("&fOnline: &7" + serverData.getBoolean("online")));
                        p.sendMessage(Color("&fPlayer Count: &7" + serverData.getInt("playerCount")));
                        p.sendMessage(Color("&fMOTD: &7" + serverData.getString("motd")));
                        p.sendMessage(Color("&fServer Version Type: &7" + serverData.getString("server_version_type")));
                        p.sendMessage(Color("&fVisibility: &7" + serverData.getBoolean("visibility")));
                        p.sendMessage(Color("&fServer Plan: &7" + serverData.getString("server_plan")));
                        if (serverData.has("using_cosmetics")) {
                            p.sendMessage(Color("&fUsing Cosmetics: &7" + serverData.getBoolean("using_cosmetics")));
                        }
                        Date a = new Date(serverData.getLong("creation"));
                        SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd yyyy hh:mm aa");
                        p.sendMessage(Color("&fCreation Date: &7" + dateFormat.format(a)));
                        p.sendMessage(Color("&fCreation Timestamp: &7" + Data.getJSONObject("server").getLong("creation")));
                        p.sendMessage(Color("&dMinehut+ &7- Developed by &dSnowyJS &7& &dMia.it"));
                        return true;
                    }
                } catch (Exception e) {
                    p.sendMessage(error);
                    Bukkit.getLogger().warning("[MH+] Oh no! You ran into an error, prolly like join play.hardcore.rip or something instead.: " + e);
                    return true;
                } finally {
                    connection.disconnect();
                }
            }
        } else {
            sender.sendMessage(consoleerror);
            return true;
        }
    }
    public String Color(String s) {
        s = ChatColor.translateAlternateColorCodes('&', s);
        return s;
    }
}
