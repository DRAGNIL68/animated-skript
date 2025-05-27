package net.outmoded.animated_skript;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.outmoded.animated_skript.commands.Commands;
import net.outmoded.animated_skript.commands.CommandsTabComplete;
import net.outmoded.animated_skript.listeners.OnEntityDismountEvent;
import net.outmoded.animated_skript.models.ModelManager;
import net.outmoded.animated_skript.models.ModelPersistence;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;


public final class AnimatedSkript extends JavaPlugin {


    SkriptAddon addon;


    @Override
    public void onEnable() {

        // ###########################
        // bstats
        int pluginId = 25094;
        Metrics metrics = new Metrics(this, pluginId);
        // ###########################
        // checks version
        String version = Bukkit.getMinecraftVersion();

        if (!version.equals("1.21.4")){
            getServer().getConsoleSender().sendMessage(version);
            AnimatedSkript.getInstance().getLogger().warning("you are running a unsupported version: supported version = 1.21.4");
        }


        // ###########################
        // registering event listeners
        getServer().getPluginManager().registerEvents(new OnEntityDismountEvent(), this); // stops models from braking when they are teleported
        getServer().getPluginManager().registerEvents(new ModelPersistence(), this); // model saving and loading
        // ###########################
        // commands
        getCommand("animated-skript").setExecutor(new Commands());
        getCommand("animated-skript").setTabCompleter(new CommandsTabComplete());
        // ###########################
        // loads plugin config, model config and save data
        Config.load();
        ModelManager.getInstance().loadModelConfigs();
        ModelPersistence.loadLastConfig();
        // ###########################
        // terrible component code TODO: replace this with something sane
        final Component logo = MiniMessage.miniMessage().deserialize(
                "<color:#1235ff>[</color><color:#3daeff>animated-skript</color><color:#1235ff>]</color> "
        );

        final Component component = MiniMessage.miniMessage().deserialize(
                "<color:#0dff1d>Loaded | Version 1.3.2-alpha | Made by DRAGNIL68</color>"
        );

        final Component warning = MiniMessage.miniMessage().deserialize(
                "<red>WARNING: This Plugin Is Still In Early Alpha And My Do Damage ONLY use on test servers"
        );
        getServer().getConsoleSender().sendMessage(logo.append(component));
        getServer().getConsoleSender().sendMessage(logo.append(warning));

        // ###########################
        // Skript stuff
        addon = Skript.registerAddon(this);
        try {
            //This will register all our syntax for us. Explained below
            addon.loadClasses("net.outmoded.animated_skript", "skript");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Component skript = MiniMessage.miniMessage().deserialize(
                "<color:#0dff1d>Skript Syntax Loaded!"
        );
        getServer().getConsoleSender().sendMessage(logo.append(skript));
        // ###########################


        // ###########################
        // ticks models
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
            public void run(){
                ModelManager.getInstance().tickAllAnimations();

            }
        }, 20, 1L);
        // ###########################
        // updates save data
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
            public void run(){
                ModelPersistence.saveAllActiveModelsToCurrentConfig();
                ModelPersistence.writeToDisk();

            }
        }, 3000, (long) Config.getAutoSaveTimer() * 20 * 60); // 5m * 60 = 300m, 300m * 20 = 6000T
        // ###########################

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ModelPersistence.saveAllActiveModelsToCurrentConfig();
        ModelPersistence.writeToDisk();

    }


    public static AnimatedSkript getInstance() {
        return getPlugin(AnimatedSkript.class);
    }

    public SkriptAddon getAddonInstance() {
        return addon;
    }
}


