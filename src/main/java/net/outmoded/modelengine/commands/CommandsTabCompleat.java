package net.outmoded.modelengine.commands;


import net.outmoded.modelengine.ModelClass;
import net.outmoded.modelengine.ModelManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

import static net.outmoded.modelengine.ModelManager.*;

public class CommandsTabCompleat implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("model_engine"))
            return null;

        if (args.length == 1){
            return Arrays.asList("list_loaded_models", "list_active_models", "help", "reload", "spawn", "play_animation");
        }
        if (args.length == 2){

            if (args[0].equals("spawn")){
                return Arrays.asList(getAllLoadedModelsKeys());
            }
            else if(args[0].equals("play_animation")){
                return Arrays.asList(getAllActiveModelsUuids());
            }
            else if(args[0].equals("remove")){
                return Arrays.asList(getAllActiveModelsUuids());
            }
        }
        if (args.length == 3){

            if (args[0].equals("play_animation")){
                ModelClass model = getActiveModel(args[1]);
                if (model == null)
                    return Collections.emptyList();


                return Arrays.asList(model.getAnimations());

            }
        }

        return new ArrayList<>();
    }
}
