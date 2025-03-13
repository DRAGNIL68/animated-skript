package net.outmoded.modelengine.commands;

import net.outmoded.modelengine.Config;
import net.outmoded.modelengine.models.ModelManager;
import net.outmoded.modelengine.models.ModelPersistence;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static net.outmoded.modelengine.models.ModelManager.*;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {

            if (args.length == 1) {

                if (!sender.hasPermission("animated-skript")) {
                    sender.sendMessage(ChatColor.RED + "Type /animation help for list of commands");
                    return true;
                }

                if (Objects.equals(args[0], "list_loaded_models")) {

                    sender.sendMessage(ChatColor.GREEN + "Models Loaded:" + Arrays.toString(ModelManager.getAllLoadedModelsKeys()));
                    return true;

                } else if (Objects.equals(args[0], "list_active_models")) {

                    sender.sendMessage(ChatColor.GREEN + "Models Active:" + Arrays.toString(ModelManager.getAllActiveModelsUuids()));
                    return true;

                } else if (Objects.equals(args[0], "help")) {


                    sender.sendMessage("/animated-skript list_active_models|list_loaded_models|reload ||2 args|| spawn filename_with_no.json ||3 args|| play_animation activeModelUuid(in console) animationName");
                    return true;

                } else if (Objects.equals(args[0], "reload")) {

                    Config.load();
                    ModelManager.loadModelConfigs();
                    ModelManager.reloadAllActiveModels();


                    //ModelPersistence.saveModels();

                    sender.sendMessage(ChatColor.GREEN + "Reloaded All Models");
                    return true;

                } else {
                    sender.sendMessage(ChatColor.RED + "Type /animated-skript help for list of commands");
                    return true;
                }

            } else if (args.length == 2) {

                if (Objects.equals(args[0], "spawn")) {



                    if (!loadedModelExists(args[1])) {
                        sender.sendMessage(ChatColor.RED + "Model Dose Not Exist");
                        return true;
                    }
                    Location location = ((Player) sender).getLocation();

                    ModelManager.spawnNewModel(args[1], location);
                    sender.sendMessage("Spawned Model: " + args[1]);
                    return true;
                }

                if (Objects.equals(args[0], "remove")) {



                    if (!loadedModelExists(args[1])) {
                        sender.sendMessage(ChatColor.RED + "Model Dose Not Exist");
                        return true;
                    }
                    Location location = ((Player) sender).getLocation();

                    ModelManager.getActiveModel(UUID.fromString(args[1]));
                    sender.sendMessage("Removed Model: " + args[1]);
                    return true;
                }

            } else if (args.length == 3) {


                if (Objects.equals(args[0], "play_animation")) {


                    if (activeModelExists(UUID.fromString(args[1]))) {
                        if (getActiveModel(UUID.fromString(args[1])) == null) {
                            sender.sendMessage(ChatColor.RED + "Model Dose Not Exist");
                            return true;
                        }

                        getActiveModel(UUID.fromString(args[1])).playAnimation(args[2]);
                        return true;
                    }
                }

                else {
                    sender.sendMessage(ChatColor.RED + "Type /animation help for list of commands");
                    return true;
                }


            } else {
                sender.sendMessage(ChatColor.RED + "Type /animation help for list of commands");
                return true;
            }
        }


        return false;
    }


}
