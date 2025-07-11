package net.outmoded.animated_skript.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.outmoded.animated_skript.AnimatedSkript;
import net.outmoded.animated_skript.events.OnModelRemovedEvent;
import net.outmoded.animated_skript.events.OnModelSpawnedEvent;
import net.outmoded.animated_skript.events.OnReloadEvent;
import net.outmoded.animated_skript.pack.Namespace;
import net.outmoded.animated_skript.pack.ResourcePack;
import net.outmoded.animated_skript.pack.jsonObjects.McMeta;
import net.outmoded.animated_skript.pack.jsonObjects.Model;
import net.outmoded.animated_skript.skript.SkriptManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import static net.outmoded.animated_skript.Config.debugMode;
import static org.bukkit.Bukkit.getServer;

public class ModelManager {

    private ModelManager instance;

    private final Map<String, JsonNode> loadedModels = new HashMap<>(); // stores all model types I.e. there configs: key = file name
    private final Map<UUID, ModelClass> activeModels = new HashMap<>(); // stores all models that are active on the server
    private ResourcePack resourcePack;
    private Namespace animatedSkript;
    private int errorCount = 0;
    private static ModelManager modelManager;

    private ModelManager(){


    }

    public static ModelManager getInstance() {
        if (modelManager == null) {
            modelManager = new ModelManager();
        }
        return modelManager;
    }


    public void increaseErrorCount(){
        errorCount++;
    }

    public void resetErrorCount(){
        errorCount = 0;
    }

    public int getErrorCount(){
        return errorCount;
    }


    public ModelClass spawnNewModel(String modelType, Location location){
        try{

            if (loadedModelExists(modelType)) {

                UUID uuid = UUID.randomUUID();

                ModelClass newModel = new ModelClass(location, modelType, uuid);

                OnModelSpawnedEvent event = new OnModelSpawnedEvent(uuid, modelType, newModel);
                Bukkit.getPluginManager().callEvent(event);

                activeModels.put(uuid, newModel);
                SkriptManager.setLastSpawnedModelClass(newModel);


                if (debugMode())
                    getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "New Model " + ChatColor.WHITE + modelType + ChatColor.GREEN + " With Uuid " + ChatColor.WHITE + uuid);

                return newModel;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public ModelClass spawnNewModel(String modelType, Location location, UUID uuid){ // this one lets you set the uuid of the model
        try{

            if (loadedModelExists(modelType)) {

                ModelClass newModel = new ModelClass(location, modelType, uuid);

                OnModelSpawnedEvent event = new OnModelSpawnedEvent(uuid, modelType, newModel);
                Bukkit.getPluginManager().callEvent(event);

                activeModels.put(uuid, newModel);
                SkriptManager.setLastSpawnedModelClass(newModel);

                if (debugMode())
                    getServer().getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("<color:#1235ff>New Model <white>" + modelType + " <color:#1235ff>With Uuid <white>" + uuid) );

                return newModel;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void addModelConfig(String name, JsonNode modelFile){
        if (!loadedModels.containsKey(name)){
            loadedModels.put(name, modelFile);
        }
    } // TODO: this dose not work because of the pack gen code

    public void tickAllAnimations() { // TODO: needs recoding
        for (ModelClass model : activeModels.values()) {
            model.tickAnimation();
        }

    } // loops animations of all active models

    public UUID[] getAllActiveModelsUuids() {
        UUID[] output = new UUID[activeModels.size()];
        Integer count = 0;
        for (UUID key : activeModels.keySet()) {
            output[count] = key;
            count++;

        }
        return output;
    } // get uuids of active models

    public String[] getAllActiveModelsUuidsAsString() {
        String[] output = new String[activeModels.size()];
        Integer count = 0;
        for (UUID key : activeModels.keySet()) {
            output[count] = String.valueOf(key);
            count++;

        }
        return output;
    } // get uuids of active models

    public String[] getAllLoadedModelsKeys() {
        String[] output = new String[loadedModels.size()];
        Integer count = 0;
        for (String key : loadedModels.keySet()) {
            output[count] = key;
            count++;

        }
        return output;
    } // get all names of loaded models

    public ModelClass[] getAllActiveModels() {
        return activeModels.values().toArray(ModelClass[]::new);
    }

    public ModelClass getActiveModel(UUID uuid) {
        if (activeModels.containsKey(uuid)) {
            return activeModels.get(uuid);
        }
        return null;
    } // returns a reference of the requested ModelClass

    public boolean  activeModelExists(UUID uuid) {
        if (activeModels.containsKey(uuid)) {
            return true;
        }
        return false;
    }

    public boolean loadedModelExists(String uuid) {
        if (loadedModels.containsKey(uuid)) {
            return true;
        }
        return false;
    }

    public JsonNode getLoadedModel(String name) {
        return loadedModels.get(name);
    } // returns a reference of the requested JsonNode

    public void reloadAllActiveModels() { // TODO: needs recoding
        resetErrorCount();


        for (ModelClass model : activeModels.values()) {
            model.loadConfig();
        }

    }


    public void removeActiveModel(UUID uuid) { // TODO: needs recoding
        if (!activeModels.containsKey(uuid)){
            return;
        }
        ModelClass model = activeModels.get(uuid);
        OnModelRemovedEvent event = new OnModelRemovedEvent(uuid, model.getModelType(), model);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            model.deleteModelNodes();
            model.getOrigin().remove();
            activeModels.remove(uuid);

        }


    } // deletes an active model


    // ########################################### TODO: needs recoding
    // model gen stuff still terrible but works more or less
    public void loadModelTextures(JsonNode model, String modelName){ // TODO: needs recoding
        if (model.get("textures") == null){
            return;
        }
        JsonNode textures = model.get("textures");

        for (JsonNode texture : textures) {
            String textureName = texture.get("name").asText();
            if (Pattern.compile("[^a-z0-9_]").matcher(textureName.replace(".png", "")).find()){
                final Component logo = MiniMessage.miniMessage().deserialize(
                        "<color:#1235ff>[</color><color:#3daeff>animated-skript</color><color:#1235ff>]</color> "
                );

                final Component warning = MiniMessage.miniMessage().deserialize(
                        "<red>"+modelName+" contains a texture ("+textureName+") name with special charters use only a-z 0-9 and _"
                );
                getServer().getConsoleSender().sendMessage(logo.append(warning));
                increaseErrorCount();

            }


            String textureSrc = texture.get("src").asText().replace("data:image/png;base64,", "");

            resourcePack.base64ToTexture(textureName, animatedSkript.getNamespacePathAsString() + "textures/item/" + modelName + "/", textureSrc);
            //resourcePack.base64ToTexture("frog.png", "assets/", textureSrc);

        }


    }

    public void loadModelData(JsonNode model, String modelName){ // terrible name, loads 3d model from json file
        if (model.get("variants") == null){
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

    public void loadModelConfigs() { // loads json configs for models into memory

        OnReloadEvent event = new OnReloadEvent();

        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            resetErrorCount();
            resourcePack = new ResourcePack("animated-skript");

            animatedSkript = new Namespace("animated-skript", resourcePack); // <- creates new namespace

            loadedModels.clear();

            ObjectMapper objectMapper = new ObjectMapper();
            File dataFolder = AnimatedSkript.getInstance().getDataFolder();


            File contentsFolder = new File(AnimatedSkript.getInstance().getDataFolder(), "contents");
            final Component logo = MiniMessage.miniMessage().deserialize(
                    "<color:#1235ff>[</color><color:#3daeff>animated-skript</color><color:#1235ff>]</color> "
            );

            try {
                File[] listedFiles = contentsFolder.listFiles();
                int errors = 0;
                if (listedFiles != null) {
                    for (File modelfile : listedFiles) {
                        if (!modelfile.getAbsoluteFile().isFile()) // TODO: fix this
                            return;


                        int dotIndex = modelfile.getName().lastIndexOf(".");

                        if (dotIndex >= 0) {
                            if (!modelfile.getName().endsWith("json")){
                                return;
                            }
                        }

                        JsonNode modelFileAsJsonNode = objectMapper.readTree(modelfile);
                        if (!loadedModels.containsKey(modelfile.getName())) {

                            int lastJsonIndex = modelfile.getName().lastIndexOf(".json");
                            loadedModels.put(modelfile.getName().substring(0, lastJsonIndex), modelFileAsJsonNode);

                            loadModelTextures(modelFileAsJsonNode, modelfile.getName().substring(0, lastJsonIndex));
                            loadModelData(modelFileAsJsonNode, modelfile.getName().substring(0, lastJsonIndex));
                            resourcePack.writeJsonObject(new McMeta("§3Animated-Skript", 46), "");
                            if (debugMode()){
                                final Component debug = MiniMessage.miniMessage().deserialize(
                                        "<dark_red>(Debug) File Name: " + modelfile.getName() + " Name: " + modelfile.getName().substring(0, lastJsonIndex)
                                );
                                getServer().getConsoleSender().sendMessage(logo.append(debug));


                            }


                        } else {
                            errors++;
                            int lastJsonIndex = modelfile.getName().lastIndexOf(".json");
                            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Model Already Has The Name " + modelfile.getName().substring(0, lastJsonIndex)); // this can never happen

                        }

                    }

                    final Component text = MiniMessage.miniMessage().deserialize(
                            "<color:#0dff1d>Finished Loading Models with</color> <dark_red>" + getErrorCount() + " <color:#0dff1d>Error(s)</color>"
                    );

                    getServer().getConsoleSender().sendMessage(logo.append(text));
                }
                resourcePack.build(AnimatedSkript.getInstance().getDataFolder().getPath() + "/output/" + resourcePack.getName() + ".zip"); // TODO: separate out pack gen

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // ###########################################

    public void stopSpectatingNode(Player player){
        NamespacedKey namespacedKey = new NamespacedKey(AnimatedSkript.getInstance(), "isSpectating");
        if (player.getPersistentDataContainer().has(namespacedKey)){
            player.setGameMode(GameMode.valueOf(player.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING)));
            player.getPersistentDataContainer().remove(namespacedKey);
        }
    }
}
