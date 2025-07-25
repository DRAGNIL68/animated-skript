package net.outmoded.animated_skript;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.google.errorprone.annotations.Keep;
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
import org.checkerframework.common.returnsreceiver.qual.This;

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

        if (version.equals("1.21.4") || version.equals("1.21.6") || version.equals("1.21.7")){
        }
        else {
            AnimatedSkript.getInstance().getLogger().warning("you are running a unsupported version: supported version = 1.21.4/1.21.6/1.21.7");
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
        Config.loadLang();
        ModelManager.getInstance().loadModelConfigs();
        ModelPersistence.loadLastConfig();
        // ###########################
        final Component component = MiniMessage.miniMessage().deserialize(
                Config.getLang("prefix")+"<color:#0dff1d>Loaded | Version "+AnimatedSkript.getInstance().getPluginMeta().getVersion()+" | Made by DRAGNIL68</color>"
        );

        getServer().getConsoleSender().sendMessage(component);
        getServer().getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize(Config.getLang("prefix")+Config.getLang("alpha_warning")));


        // ###########################
        // Skript stuff

        if (Bukkit.getServer().getPluginManager().getPlugin("Skript") != null){
            addon = Skript.registerAddon(this);
            try {
                //This will register all our syntax for us. Explained below
                addon.loadClasses("net.outmoded.animated_skript", "skript");
            } catch (IOException e) {
                e.printStackTrace();
            }
            getServer().getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize(Config.getLang("prefix")+Config.getLang("skript_syntax_loaded")));
        }
        else {

            getServer().getConsoleSender().sendMessage( MiniMessage.miniMessage().deserialize(Config.getLang("prefix")+Config.getLang("skript_syntax_disabled")));

        }

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


