package net.outmoded.animated_skript.commands;


import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.ModelManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class CommandsTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1){
            if (getServer().getPluginManager().getPlugin("outmodedlib") == null){

                return Arrays.asList("list_loaded_models", "list_active_models", "help", "reload", "spawn", "remove", "play_animation", "stop_animation");
            }

            return Arrays.asList("list_loaded_models", "list_active_models", "help", "reload", "reload-resource-pack", "spawn", "remove", "play_animation", "stop_animation");
        }
        if (args.length == 2){

            if (args[0].equals("spawn")){
                return Arrays.asList(ModelManager.getInstance().getAllLoadedModelsKeys());
            }
            else if(args[0].equals("play_animation")){
                return Arrays.asList(ModelManager.getInstance().getAllActiveModelsUuidsAsString());
            }
            else if(args[0].equals("stop_animation")){
                return Arrays.asList(ModelManager.getInstance().getAllActiveModelsUuidsAsString());
            }
            else if(args[0].equals("remove")){
                return Arrays.asList(ModelManager.getInstance().getAllActiveModelsUuidsAsString());
            }
            else {
                return new ArrayList<>();
            }
        }
        if (args.length == 3){


            if (args[0].equals("play_animation")){
                UUID uuid;

                if (args[1] == null)
                    return new ArrayList<>();

                uuid = UUID.fromString(args[1]);

                if (!ModelManager.getInstance().activeModelExists(uuid)){

                    return new ArrayList<>();
                }

                ModelClass model = ModelManager.getInstance().getActiveModel(uuid);

                if (model == null) {
                    return new ArrayList<>();
                }

                if (model.getAnimations().length == 0) {
                    return new ArrayList<>();
                }
                return Arrays.asList(model.getAnimationNames());
            }

            return new ArrayList<>();

        }


        return new ArrayList<>();
    }
}
