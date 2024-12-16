package net.outmoded.modelengine;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.outmoded.modelengine.ModelManager.getAllActiveModelsUuids;
import static net.outmoded.modelengine.ModelManager.getAllLoadedModelsKeys;

public class CommandsTabCompleat implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("sprocket_engine"))
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
                return Arrays.asList(ModelManager.getActiveModel(args[1]).getAnimations());
            }
        }

        return new ArrayList<>();
    }
}
