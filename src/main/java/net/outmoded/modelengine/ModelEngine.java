package net.outmoded.modelengine;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.outmoded.modelengine.commands.Commands;
import net.outmoded.modelengine.commands.CommandsTabComplete;
import net.outmoded.modelengine.listeners.OnEntityDismountEvent;
import net.outmoded.modelengine.models.ModelManager;
import net.outmoded.modelengine.models.ModelPersistence;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

import static net.outmoded.modelengine.models.ModelPersistence.loadLastConfig;


public final class ModelEngine extends JavaPlugin {


    SkriptAddon addon;


    @Override
    public void onEnable() {


        getServer().getPluginManager().registerEvents(new OnEntityDismountEvent(), this);
        getServer().getPluginManager().registerEvents(new ModelPersistence(), this); // model saving and loading

        getCommand("animated-skript").setExecutor(new Commands());
        getCommand("animated-skript").setTabCompleter(new CommandsTabComplete());

        ModelPersistence.loadLastConfig(); // loads last config into memory

        final Component logo = MiniMessage.miniMessage().deserialize(
                "<color:#1235ff>[</color><color:#3daeff>animated-skript</color><color:#1235ff>]</color> "
        );

        final Component component = MiniMessage.miniMessage().deserialize(
                "<color:#0dff1d>Loaded | Version 1.0-alpha | Made by DRAGNIL68</color>"
        );

        final Component warning = MiniMessage.miniMessage().deserialize(
                "<red>WARNING: This Plugin Is Still In Early Alpha And My Do Damage ONLY use on test servers"
        );
        getServer().getConsoleSender().sendMessage(logo.append(component));
        getServer().getConsoleSender().sendMessage(logo.append(warning));


        // Skript stuff
        addon = Skript.registerAddon(this);
        try {
            //This will register all our syntax for us. Explained below
            addon.loadClasses("net.outmoded.modelengine", "skript");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Component skript = MiniMessage.miniMessage().deserialize(
                "<color:#0dff1d>Skript Syntax Loaded!"
        );
        getServer().getConsoleSender().sendMessage(logo.append(skript));
        // End of Skript Stuff

        Config.load();
        ModelManager.loadModelConfigs();
        ModelPersistence.loadLastConfig();


        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
            public void run(){
                ModelManager.tickAllAnimations();

            }
        }, 20, 1L);


        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
            public void run(){
                ModelPersistence.saveAllActiveModelsToCurrentConfig();
                ModelPersistence.writeToDisk();

            }
        }, 3000, (long) Config.getAutoSaveTimer() * 20 * 60); // 5m * 60 = 300m, 300m * 20 = 6000T


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ModelPersistence.saveAllActiveModelsToCurrentConfig();
        ModelPersistence.writeToDisk();

    }


    public static ModelEngine getInstance() {
        return getPlugin(ModelEngine.class);
    }

    public SkriptAddon getAddonInstance() {
        return addon;
    }
}


