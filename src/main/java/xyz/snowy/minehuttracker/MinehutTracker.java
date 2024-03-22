package xyz.snowy.minehuttracker;

import xyz.snowy.minehuttracker.commands.CheckServer;
import xyz.snowy.minehuttracker.commands.MHTracker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinehutTracker extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Bukkit.getLogger().info("[Minehut Tracker] Plugin has started");
        // Commands
        getCommand("mhtracker").setExecutor(new MHTracker(this));
        getCommand("checkserver").setExecutor(new CheckServer(this));
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[Minehut Tracker] bye bye!");
    }
}
