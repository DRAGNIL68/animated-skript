package net.outmoded.modelengine.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.outmoded.modelengine.Config;
import net.outmoded.modelengine.ModelEngine;
import net.outmoded.modelengine.events.OnModelRemovedEvent;
import net.outmoded.modelengine.events.OnModelSpawnedEvent;
import net.outmoded.modelengine.events.OnReloadEvent;
import net.outmoded.modelengine.pack.Namespace;
import net.outmoded.modelengine.pack.ResourcePack;
import net.outmoded.modelengine.pack.jsonObjects.McMeta;
import net.outmoded.modelengine.pack.jsonObjects.Model;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import static net.outmoded.modelengine.Config.debugMode;
import static org.bukkit.Bukkit.getServer;

public class ModelManager {
    private final static Map<String, JsonNode> loadedModels = new HashMap<>(); // stores all model types I.e. there configs: key = file name
    private final static Map<UUID, ModelClass> activeModels = new HashMap<>(); // stores all models that are active on the server
    private static ResourcePack resourcePack;
    private static Namespace animatedSkript;

    private ModelManager(){

    }



    public static void spawnNewModel(String modelType, Location location){
        try{
            if (loadedModelExists(modelType)) {
                UUID uuid = UUID.randomUUID();

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
    public static void spawnNewModel(String modelType, Location location, UUID uuid){ // this one lets you set the uuid of the model
        try{
            if (loadedModelExists(modelType)) {


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

    public static UUID[] getAllActiveModelsUuids() {
        UUID[] output = new UUID[activeModels.size()];
        Integer count = 0;
        for (UUID key : activeModels.keySet()) {
            output[count] = key;
            count++;

        }
        return output;
    } // get uuids of active models

    public static String[] getAllActiveModelsUuidsAsString() {
        String[] output = new String[activeModels.size()];
        Integer count = 0;
        for (UUID key : activeModels.keySet()) {
            output[count] = String.valueOf(key);
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

    public static ModelClass getActiveModel(UUID uuid) {
        if (activeModels.containsKey(uuid)) {
            return activeModels.get(uuid);
        }
        return null;
    } // returns a reference of the requested ModelClass

    public static boolean  activeModelExists(UUID uuid) {
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


    public static void deleteActiveModel(UUID uuid) {
        if (!activeModels.containsKey(uuid)){
            return;
        }
        ModelClass model = activeModels.get(uuid);
        OnModelRemovedEvent event = new OnModelRemovedEvent(uuid, model.getModelType());
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            model.deleteModelNodes();
            activeModels.remove(uuid);

        }


    } // deletes an active model TODO: need way to remove loaded model

    public static void loadModelTexture (JsonNode model, String modelName){
        if (model.get("textures") == null){
            return;
        }

        JsonNode textures = model.get("textures");
        textures.forEach(texture -> {
            String textureName = texture.get("name").asText();
            String textureSrc = texture.get("src").asText().replace("data:image/png;base64,", "");

            resourcePack.base64ToTexture(textureName, animatedSkript.getNamespacePathAsString() + "textures/item/" + modelName + "/", textureSrc);
            //resourcePack.base64ToTexture("frog.png", "assets/", textureSrc);

        });


    }

    public static void loadModelData (JsonNode model, String modelName){ // terrible name, loads 3d model from json file
        if (model.get("textures") == null){
            return;
        }

        JsonNode variants = model.get("variants");
        variants.forEach(variant -> {

            JsonNode models = variant.get("models");
            models.forEach(modelUuid -> {
                JsonNode textures = modelUuid.get("model").get("textures");

                Map<String, String> update = new HashMap<>();

                Iterator<String> itr = textures.fieldNames();
                while (itr.hasNext()) {
                    String key_field = itr.next();
                    String value = textures.get(key_field).asText();
                    String newVal = animatedSkript.getNamespaceAsString() + ":item/" + modelName + "/" + value.substring(value.lastIndexOf("/") + 1);
                    update.put(key_field, newVal);
                }

                update.forEach((key, newValue) -> {
                    ((ObjectNode) textures).put(key, newValue);
                });





            });

            Iterator<String> itr = models.fieldNames();
            while (itr.hasNext()) {
                String key_field = itr.next();
                String value = models.get(key_field).asText();
                JsonNode valueAsJsonNode = models.get(key_field);

                animatedSkript.createGenericFile(key_field + ".json","models/" + modelName + "/" + variant.get("name").asText() + "/", valueAsJsonNode.get("model").toString());
                animatedSkript.writeJsonObject(new Model("animated-skript:" + modelName + "/" + variant.get("name").asText() + "/" + key_field), "items/" + modelName + "/" + variant.get("name").asText());
            }
        });


    }


    public static void loadModelConfigs() { // loads json configs for models into memory
        OnReloadEvent event = new OnReloadEvent();

        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            resourcePack = new ResourcePack("animated-skript");

            animatedSkript = new Namespace("animated-skript", resourcePack); // <- creates new namespace

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

                            loadModelTexture(modelFileAsJsonNode, modelfile.getName().substring(0, lastJsonIndex));
                            loadModelData(modelFileAsJsonNode, modelfile.getName().substring(0, lastJsonIndex));
                            resourcePack.writeJsonObject(new McMeta("§3Animated-Skript", 42), "");
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
                resourcePack.build(ModelEngine.getInstance().getDataFolder().getPath() + "/output/" + resourcePack.getName() + ".zip");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
