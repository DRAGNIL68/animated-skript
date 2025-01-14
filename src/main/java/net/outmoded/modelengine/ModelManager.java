package net.outmoded.modelengine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.outmoded.modelengine.events.OnModelRemovedEvent;
import net.outmoded.modelengine.events.OnModelSpawnedEvent;
import net.outmoded.modelengine.events.OnReloadEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.outmoded.modelengine.Config.debugMode;
import static org.bukkit.Bukkit.getServer;

public class ModelManager {
    private final static Map<String, JsonNode> loadedModels = new HashMap<>(); // stores all model types I.e. there configs: key = file name
    private final static Map<String, ModelClass> activeModels = new HashMap<>(); // stores all models that are active on the server
    //private final static ModelManager modelManager = new ModelManager();


    private ModelManager(){

    }


    public static void spawnNewModel(String modelType, Location location){
        try{
            if (loadedModelExists(modelType)) {
                String uuid = String.valueOf(UUID.randomUUID());

                OnModelSpawnedEvent event = new OnModelSpawnedEvent(uuid, modelType);
                Bukkit.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {

                    location.setPitch(0);
                    location.setYaw(0);


                    ModelClass newModel = new ModelClass(location, modelType, uuid);
                    activeModels.put(uuid, newModel);


                    if (Config.debugMode())
                        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "New Model " + ChatColor.WHITE + modelType + ChatColor.GREEN + " With Uuid " + ChatColor.WHITE + uuid);

                }
            }





        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static void addModelConfig(String name, JsonNode modelFile){
        if (!loadedModels.containsKey(name)){
            loadedModels.put(name, modelFile);
        }
    } // TODO: not sure if ths even needs to exist

    public static void tickAllAnimations() {
        for (ModelClass model : activeModels.values()) {
            model.tickAnimation();
        }

    } // loops animations of all active models

    public static String[] getAllActiveModelsUuids() {
        String[] output = new String[activeModels.size()];
        Integer count = 0;
        for (String key : activeModels.keySet()) {
            output[count] = key;
            count++;

        }
        return output;
    } // get uuids of active models

    public static String[] getAllLoadedModelsKeys() {
        String[] output = new String[loadedModels.size()];
        Integer count = 0;
        for (String key : loadedModels.keySet()) {
            output[count] = key;
            count++;

        }
        return output;
    } // get all names of loaded models

    public static ModelClass getActiveModel(String uuid) {
        if (activeModels.containsKey(uuid)) {
            return activeModels.get(uuid);
        }
        return null;
    } // returns a reference of the requested ModelClass

    public static boolean  activeModelExists(String uuid) {
        if (activeModels.containsKey(uuid)) {
            return true;
        }
        return false;
    }

    public static boolean loadedModelExists(String uuid) {
        if (loadedModels.containsKey(uuid)) {
            return true;
        }
        return false;
    }

    public static JsonNode getLoadedModel(String name) {
        return loadedModels.get(name);
    } // returns a reference of the requested JsonNode

    public static void reloadAllActiveModels() {



        for (ModelClass model : activeModels.values()) {
            model.loadConfig();
            model.loadAnimations();
        }

    }


    public static void deleteActiveModel(String uuid) {
        if (!activeModels.containsKey(uuid)){
            return;
        }
        ModelClass model = activeModels.get(uuid);
        OnModelRemovedEvent event = new OnModelRemovedEvent(uuid, model.modelType);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            model.deleteModelNodes();
            activeModels.remove(uuid);

        }


    } // deletes an active model TODO: need way to remove loaded model

    public static void loadModelConfigs() { // loads json configs for models into memory
        OnReloadEvent event = new OnReloadEvent();

        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            loadedModels.clear();

            ObjectMapper objectMapper = new ObjectMapper();
            File dataFolder = ModelEngine.getInstance().getDataFolder();


            File contentsFolder = new File(ModelEngine.getInstance().getDataFolder(), "contents");


            try {
                File[] listedFiles = contentsFolder.listFiles();
                int errors = 0;
                if (listedFiles != null) {
                    for (File modelfile : listedFiles) {
                        JsonNode modelFileAsJsonNode = objectMapper.readTree(modelfile);
                        if (!loadedModels.containsKey(modelfile.getName())) {
                            int lastJsonIndex = modelfile.getName().lastIndexOf(".json");
                            loadedModels.put(modelfile.getName().substring(0, lastJsonIndex), modelFileAsJsonNode);
                            if (debugMode())
                                getServer().getConsoleSender().sendMessage(ChatColor.RED + "(Debug) File Name: " + modelfile.getName() + " Name: " + modelfile.getName().substring(0, lastJsonIndex));

                        } else {
                            errors++;
                            int lastJsonIndex = modelfile.getName().lastIndexOf(".json");
                            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Model Already Has The Name " + modelfile.getName().substring(0, lastJsonIndex)); // this can never happen

                        }

                    }
                    getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Finished Loading Models with " + ChatColor.RED + errors + ChatColor.GREEN + " Error(s)");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
