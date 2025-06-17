package net.outmoded.animated_skript.commands;

import net.outmoded.animated_skript.Config;
import net.outmoded.animated_skript.models.ModelManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

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

                    sender.sendMessage(ChatColor.GREEN + "Models Loaded:" + Arrays.toString(ModelManager.getInstance().getAllLoadedModelsKeys()));
                    return true;

                } else if (Objects.equals(args[0], "list_active_models")) {

                    sender.sendMessage(ChatColor.GREEN + "Models Active:" + Arrays.toString(ModelManager.getInstance().getAllActiveModelsUuids()));
                    return true;

                } else if (Objects.equals(args[0], "help")) {


                    sender.sendMessage("/animated-skript list_active_models|list_loaded_models|reload ||2 args|| spawn filename_with_no.json ||3 args|| play_animation activeModelUuid(in console) animationName");
                    return true;

                } else if (Objects.equals(args[0], "reload")) {

                    Config.load();
                    ModelManager.getInstance().loadModelConfigs();
                    ModelManager.getInstance().reloadAllActiveModels();


                    //ModelPersistence.saveModels();

                    sender.sendMessage(ChatColor.GREEN + "Reloaded All Models");
                    return true;

                } else {
                    sender.sendMessage(ChatColor.RED + "Type /animated-skript help for list of commands");
                    return true;
                }

            } else if (args.length == 2) {

                if (Objects.equals(args[0], "spawn")) {



                    if (!ModelManager.getInstance().loadedModelExists(args[1])) {
                        sender.sendMessage(ChatColor.RED + "Model Does Not Exist");
                        return true;
                    }
                    Location location = ((Player) sender).getLocation();
                    location.setYaw(0);
                    location.setPitch(0);

                    ModelManager.getInstance().spawnNewModel(args[1], location);
                    sender.sendMessage("Spawned Model: " + args[1]);
                    return true;
                }

                if (Objects.equals(args[0], "remove")) {



                    if (!ModelManager.getInstance().activeModelExists(UUID.fromString(args[1]))) {
                        sender.sendMessage(ChatColor.RED + "Model Does Not Exist");
                        return true;
                    }

                    ModelManager.getInstance().removeActiveModel(UUID.fromString(args[1]));
                    sender.sendMessage("Removed Model: " + args[1]);
                    return true;
                }
                if (Objects.equals(args[0], "stop_animation")) {



                    if (!ModelManager.getInstance().activeModelExists(UUID.fromString(args[1]))) {
                        sender.sendMessage(ChatColor.RED + "Model Does Not Exist");
                        return true;
                    }


                    ModelManager.getInstance().getActiveModel(UUID.fromString(args[1])).resetAnimation();
                    sender.sendMessage("Stopped Animation Of Model: " + args[1]);
                    return true;
                }

            } else if (args.length == 3) {


                if (Objects.equals(args[0], "play_animation")) {


                    if (ModelManager.getInstance().activeModelExists(UUID.fromString(args[1]))) {
                        if (ModelManager.getInstance().getActiveModel(UUID.fromString(args[1])) == null) {
                            sender.sendMessage(ChatColor.RED + "Model Does Not Exist");
                            return true;
                        }

                        if (ModelManager.getInstance().getActiveModel(UUID.fromString(args[1])) != null)
                            ModelManager.getInstance().getActiveModel(UUID.fromString(args[1])).playAnimation(args[2]);

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
