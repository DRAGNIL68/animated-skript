package net.outmoded.animated_skript.commands;


import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.ModelManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class CommandsTabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("model_engine"))
            return null;

        if (args.length == 1){
            return Arrays.asList("list_loaded_models", "list_active_models", "help", "reload", "spawn", "remove", "play_animation", "stop_animation");
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
                    return Collections.emptyList();

                uuid = UUID.fromString(args[1]);

                sender.sendMessage(args[1]);

                if (!ModelManager.getInstance().activeModelExists(uuid)){

                    return Collections.emptyList();
                }

                ModelClass model = ModelManager.getInstance().getActiveModel(uuid);

                if (model == null) {
                    return Collections.emptyList();
                }

                if (model.getAnimations().length == 0) {
                    return Collections.emptyList();
                }
                return Arrays.asList(model.getAnimationNames());
            }


        }


        return new ArrayList<>();
    }
}
