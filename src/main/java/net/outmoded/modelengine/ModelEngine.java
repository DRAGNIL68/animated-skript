package net.outmoded.modelengine;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public final class ModelEngine extends JavaPlugin {

    @Override
    public void onEnable() {

        Config.load();
        ModelManager.loadModelConfigs();

        getCommand("sprocket_engine").setExecutor(new Commands());
        getCommand("sprocket_engine").setTabCompleter(new CommandsTabCompleat());

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Sprocket Engine Loaded | Version 1.0-alpha | Made by DRAGNIL68");
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "WARNING: Sprocket Engine Is In Early Alpha And My Do Damage ONLY use on test servers");



        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
            public void run(){
                ModelManager.tickAllAnimations();

            }
        }, 20, 1L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static ModelEngine getInstance() {
        return getPlugin(ModelEngine.class);
    }
}


