package net.outmoded.modelengine;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import net.outmoded.modelengine.commands.Commands;
import net.outmoded.modelengine.commands.CommandsTabComplete;
import net.outmoded.modelengine.models.ModelManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;


public final class ModelEngine extends JavaPlugin {


    SkriptAddon addon;


    @Override
    public void onEnable() {

        Config.load();
        ModelManager.loadModelConfigs();

        getCommand("model_engine").setExecutor(new Commands());
        getCommand("model_engine").setTabCompleter(new CommandsTabComplete());

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Model Engine Loaded | Version 1.0-alpha | Made by DRAGNIL68");
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "WARNING: Model Engine Is In Early Alpha And My Do Damage ONLY use on test servers");


        test.runPack();

        // Skript stuff
        /*


        addon = Skript.registerAddon(this);
        try {
            //This will register all our syntax for us. Explained below
            addon.loadClasses("me.limeglass.addon", "elements");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getLogger().info("[ExampleAddon] has been enabled!");
        */
        // End of Skript Stuff
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

    //public SkriptAddon getAddonInstance() {
        //return addon;
    //}
}


