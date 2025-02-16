package net.outmoded.modelengine;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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



        getCommand("animated-skript").setExecutor(new Commands());
        getCommand("animated-skript").setTabCompleter(new CommandsTabComplete());
        final Component logo = MiniMessage.miniMessage().deserialize(
                "<color:#1235ff>[</color><color:#3daeff>animated-skript</color><color:#1235ff>]</color> "
        );

        final Component component = MiniMessage.miniMessage().deserialize(
                "<color:#0dff1d>Loaded | Version 1.0-alpha | Made by DRAGNIL68</color>"
        );

        final Component warning = MiniMessage.miniMessage().deserialize(
                "<dark_red>WARNING: This Plugin Is Still In Early Alpha And My Do Damage ONLY use on test servers</dark_red>"
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


        Config.load();
        ModelManager.loadModelConfigs();
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

    public SkriptAddon getAddonInstance() {
        return addon;
    }
}


