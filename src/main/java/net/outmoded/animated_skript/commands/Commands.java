package net.outmoded.animated_skript.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.outmoded.animated_skript.Config;
import net.outmoded.animated_skript.models.ModelClass;
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
                    sender.sendMessage(ChatColor.RED + "You Cannon Use This Command");
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
                    Config.loadLang();
                    ModelManager.getInstance().loadModelConfigs();
                    ModelManager.getInstance().reloadAllActiveModels();


                    //ModelPersistence.saveModels();
                    String message = Config.getLang("prefix")+Config.getLang("reload_command");
                    sender.sendMessage(MiniMessage.miniMessage().deserialize(message));

                    return true;

                } else {
                    sender.sendMessage(ChatColor.RED + "Type /animated-skript help for list of commands");
                    return true;
                }

            } else if (args.length == 2) {

                if (Objects.equals(args[0], "spawn")) {



                    if (!ModelManager.getInstance().loadedModelExists(args[1])) {
                        String message = Config.getLang("prefix")+Config.getLang("model_invalid_command");
                        sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
                        return true;
                    }
                    Location location = ((Player) sender).getLocation();
                    location.setYaw(0);
                    location.setPitch(0);

                    ModelClass modelClass = ModelManager.getInstance().spawnNewModel(args[1], location);

                    if (modelClass == null){
                        String message = Config.getLang("prefix")+Config.getLang("model_invalid_command");
                        sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
                        return true;
                    }


                    String modelType = modelClass.modelType;
                    String message = Config.getLang("prefix")+Config.getLang("spawn_model_command");
                    message = message.replace("{model_uuid}", modelType);
                    message = message.replace("{model_type}", args[1]);
                    sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
                    return true;
                }

                if (Objects.equals(args[0], "remove")) {



                    if (!ModelManager.getInstance().activeModelExists(UUID.fromString(args[1]))) {
                        String message = Config.getLang("prefix")+Config.getLang("model_invalid_command");
                        sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
                        return true;
                    }
                    String modelType = ModelManager.getInstance().getActiveModel(UUID.fromString(args[1])).modelType;
                    ModelManager.getInstance().removeActiveModel(UUID.fromString(args[1]));

                    String message = Config.getLang("prefix")+Config.getLang("removed_model_command");
                    message = message.replace("{model_uuid}", args[1]);
                    message = message.replace("{model_type}", modelType);
                    sender.sendMessage(MiniMessage.miniMessage().deserialize(message));

                    return true;
                }
                if (Objects.equals(args[0], "stop_animation")) {



                    if (!ModelManager.getInstance().activeModelExists(UUID.fromString(args[1]))) {
                        String message = Config.getLang("prefix")+Config.getLang("model_invalid_command");
                        sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
                        return true;
                    }


                    ModelManager.getInstance().getActiveModel(UUID.fromString(args[1])).resetAnimation();

                    String message = Config.getLang("prefix")+Config.getLang("stopped_animation_command");
                    message = message.replace("{model_uuid}", args[1]);
                    message = message.replace("{model_type}", ModelManager.getInstance().getActiveModel(UUID.fromString(args[1])).modelType);
                    sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
                    return true;
                }

            } else if (args.length == 3) {


                if (Objects.equals(args[0], "play_animation")) {


                    if (ModelManager.getInstance().activeModelExists(UUID.fromString(args[1]))) {
                        if (ModelManager.getInstance().getActiveModel(UUID.fromString(args[1])) == null) {

                            String message = Config.getLang("prefix")+Config.getLang("model_invalid_command");
                            sender.sendMessage(MiniMessage.miniMessage().deserialize(message));
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
