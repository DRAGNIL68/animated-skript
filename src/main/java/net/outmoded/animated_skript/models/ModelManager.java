package net.outmoded.animated_skript.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.outmoded.animated_skript.AnimatedSkript;
import net.outmoded.animated_skript.events.ModelRemovedEvent;
import net.outmoded.animated_skript.events.ModelSpawnedEvent;
import net.outmoded.animated_skript.events.AnimatedSkriptReload;
import net.outmoded.animated_skript.pack.Namespace;
import net.outmoded.animated_skript.pack.ResourcePack;
import net.outmoded.animated_skript.pack.jsonObjects.McMeta;
import net.outmoded.animated_skript.pack.jsonObjects.Model;
import net.outmoded.animated_skript.skript.SkriptManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import static net.outmoded.animated_skript.Config.debugMode;
import static org.bukkit.Bukkit.getServer;

public class ModelManager {

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
        return spawnNewModel(modelType, location, UUID.randomUUID()); // as shrimple as that
    }

    public ModelClass spawnNewModel(String modelType, Location location, UUID uuid){ // this one lets you set the uuid of the model
        try{

            if (loadedModelExists(modelType)) {

                ModelClass newModel = new ModelClass(location, modelType, uuid);

                ModelSpawnedEvent event = new ModelSpawnedEvent(uuid, modelType, newModel);
                Bukkit.getPluginManager().callEvent(event);

                activeModels.put(uuid, newModel);

                // this block of code just adds the new model to the chunkmap for easy save data handling
                String chunk_id = location.getWorld().getName()+"|x-"+location.getChunk().getX()+"|z-"+location.getChunk().getZ(); // world|x-3|z-4

                if (!ModelPersistence.chunkMap.containsKey(chunk_id)){
                    ArrayList<UUID> arrayList = new ArrayList<>();
                    arrayList.add(newModel.uuid);

                    ModelPersistence.chunkMap.put(chunk_id, arrayList);
                }
                else{
                    ModelPersistence.chunkMap.get(chunk_id).add(newModel.uuid);

                }

                ModelPersistence.getInstance().addModel(newModel);
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

    public void tickAllAnimations() {
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
        return loadedModels.containsKey(uuid);
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
        ModelRemovedEvent event = new ModelRemovedEvent(uuid, model.getModelType(), model);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            model.deleteModelNodes();
            model.getOrigin().remove();


            String chunk_id = model.getOriginLocation().getWorld().getName()+"|x-"+model.getOriginLocation().getChunk().getX()+"|z-"+model.getOriginLocation().getChunk().getZ();
            if (ModelPersistence.chunkMap.containsKey(chunk_id)){
                ModelPersistence.chunkMap.get(chunk_id).remove(model.uuid);
                AnimatedSkript.getInstance().getLogger().warning("frog`1123");
            }

            ModelPersistence.getInstance().removeModel(model.uuid);

            activeModels.remove(uuid);
        }


    } // deletes an active model

    /*
    deletes the model only from the world not the savedata.
    this exists because I am retarded
     */
    public void removeActiveModelFromWorld(UUID uuid) {
        if (!activeModels.containsKey(uuid)){
            return;
        }

        ModelClass model = activeModels.get(uuid);
        model.deleteModelNodes();
        model.getOrigin().remove();
        activeModels.remove(uuid);




    }

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

            Iterator<String> itr1 = models.fieldNames(); // this code loops textures a strips out the animated-java file path
            while (itr1.hasNext()) {
                String modelUuidKey = itr1.next();

                JsonNode modelUuid = models.get(modelUuidKey);
                JsonNode textures = modelUuid.get("model").get("textures");

                Map<String, String> update = new HashMap<>();

                Iterator<String> itr = textures.fieldNames(); // this code loops textures a strips out the animated-java file path
                while (itr.hasNext()) {
                    String key_field = itr.next(); // this is the key for a texture
                    String value = textures.get(key_field).asText();
                    String newVal = animatedSkript.getNamespaceAsString() + ":item/" + modelName + "/" + value.substring(value.lastIndexOf("/") + 1);
                    update.put(key_field, newVal);



                }

                update.forEach((key, newValue) -> { // this modifies the json file loaded in memory to reflect the changes
                    ((ObjectNode) textures).put(key, newValue);
                });


                if ( modelUuid.get("model").has("parent")){

                    JsonNode modelModelData = modelUuid.get("model");

                    // modifying parent
                    // "animated_java:item/frog/bone" -> animated-skript:item/{model_name}/{bone_uuid}

                    String parentNewVal = animatedSkript.getNamespaceAsString() + ":" + modelName + "/default/" + modelUuidKey;
                    ((ObjectNode) modelModelData).put("parent", parentNewVal);

                }


            }

            models.forEach(modelUuid -> {










            });
            // this code uses the old (0.1) version of the pack gen from the outmodedlib, it is terrible and is very hard to use or understand.
            Iterator<String> itr = models.fieldNames(); // this code writes model data to the resource pack.
            while (itr.hasNext()) {
                String key_field = itr.next();
                String value = models.get(key_field).asText();
                JsonNode valueAsJsonNode = models.get(key_field);

                animatedSkript.createGenericFile(key_field + ".json","models/" + modelName + "/" + variant.get("name").asText() + "/", valueAsJsonNode.get("model").toString());
                animatedSkript.writeJsonObject(new Model("animated-skript:"+modelName + "/" + variant.get("name").asText() + "/" + key_field), "items/" + modelName + "/" + variant.get("name").asText());
            }
        });


    }

    public void loadModelConfigs() { // loads json configs for models into memory

        AnimatedSkriptReload event = new AnimatedSkriptReload();

        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            resetErrorCount();
            resourcePack = new ResourcePack("animated-skript");
            resourcePack.writeJsonObject(new McMeta("§3Animated-Skript", 46), "");
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
