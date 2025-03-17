package net.outmoded.modelengine.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.outmoded.modelengine.ModelEngine;
import net.outmoded.modelengine.models.jsonObjects.ChunkJsonObject;
import net.outmoded.modelengine.models.jsonObjects.FileJsonObject;
import net.outmoded.modelengine.models.jsonObjects.ModelJsonObject;
import net.outmoded.modelengine.models.jsonObjects.WorldJsonObject;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

import static net.outmoded.modelengine.Config.debugMode;
import static org.bukkit.Bukkit.getServer;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ModelPersistence implements Listener {
    private static FileJsonObject currentConfig;


    @EventHandler
    private static void onChuckLoad(ChunkLoadEvent event){
        String name = "chunk-x" + event.getChunk().getX() + "-z" + event.getChunk().getZ();
        if (!currentConfig.hasWorld(event.getWorld().getName())){
            return;
        }
        
        if (!currentConfig.getWorld(event.getWorld().getName()).hasChunk(name)){
            return;
        }

        ChunkJsonObject chunkJsonObject = currentConfig.getWorld(event.getWorld().getName()).getChunk(name);
        Iterator<ModelJsonObject> iterator = chunkJsonObject.getAllModels().values().iterator();

        while (iterator.hasNext()){
            ModelJsonObject modelJsonObject = iterator.next();
            ModelManager.spawnNewModel(modelJsonObject.modelType, modelJsonObject.locationAsLocation, modelJsonObject.uuid);

            ModelClass modelClass = ModelManager.getActiveModel(modelJsonObject.getUuid());
            if (modelClass.hasAnimation(modelJsonObject.currentAnimation)){

                modelClass.playAnimation(modelJsonObject.currentAnimation);

            }
            iterator.remove();

            if (debugMode())
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "animated-skript: model loaded from save data");

        }
        // load models in that chunk and remove them from json config

    }

    @EventHandler
    private static void onEntitiesUnload(EntitiesUnloadEvent event){
        NamespacedKey key = new NamespacedKey(ModelEngine.getInstance(), "nodeType");
        for(Entity entity : event.getEntities()) {
            if (entity instanceof ItemDisplay){
                if (entity.getPersistentDataContainer().has(key)){
                    if (Objects.equals(entity.getPersistentDataContainer().get(key, PersistentDataType.STRING), "origin")){
                        String worldName = entity.getWorld().getName();

                        if (!currentConfig.hasWorld(worldName)){
                            WorldJsonObject worldJsonObject = new WorldJsonObject(worldName);
                            currentConfig.addWorld(worldJsonObject);

                        }


                        ChunkJsonObject chunkJsonObject = new ChunkJsonObject(entity.getChunk().getX(), entity.getChunk().getZ());
                        WorldJsonObject world = currentConfig.getWorld(worldName);

                        world.addChunk(chunkJsonObject);
                        ChunkJsonObject chunk = world.getChunk(chunkJsonObject.getName());
                        NamespacedKey UuidKey = new NamespacedKey(ModelEngine.getInstance(), "modelUuid");

                        if (entity.getPersistentDataContainer().has(UuidKey)){

                            String uuid = entity.getPersistentDataContainer().get(UuidKey, PersistentDataType.STRING);
                            ModelClass modelClass = ModelManager.getActiveModel(UUID.fromString(uuid));
                            if (modelClass != null){
                                ModelJsonObject modelJsonObject = new ModelJsonObject(modelClass);

                                if (modelClass.getPersistence())
                                    return;

                                chunk.addModel(modelJsonObject);
                                ModelManager.removeActiveModel(UUID.fromString(uuid));

                                if (debugMode())
                                    getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "animated-skript: saved model");

                            }


                        }




                    }

                }

            }
        }
    }


    public static void loadLastConfig(){

        ObjectMapper objectMapper = new ObjectMapper();
        File configFile = new File(ModelEngine.getInstance().getDataFolder(), "save_data.json");

        if (configFile.exists()) {
            try {
                currentConfig = new FileJsonObject();

                JsonNode modelFileAsJsonNode = objectMapper.readTree(configFile);
                JsonNode worlds = modelFileAsJsonNode.get("worlds");

                for (JsonNode world : worlds) {
                    String name = world.get("name").asText();


                    WorldJsonObject worldJsonObject = new WorldJsonObject(name);

                    JsonNode chunks = world.get("chunks");

                    for (JsonNode chunk : chunks) {
                        String chunkName = chunk.get("name").asText();
                        ChunkJsonObject chunkJsonObject = new ChunkJsonObject(chunk.get("x").asInt(), chunk.get("z").asInt());
                        worldJsonObject.addChunk(chunkJsonObject);

                        JsonNode models = chunk.get("models");
                        for (JsonNode model : models){
                            World world1 = Bukkit.getWorld(name);

                            Double[] locationAsArray = objectMapper.treeToValue(model.get("location"), Double[].class);
                            Location location = new Location(world1, locationAsArray[0], locationAsArray[1], locationAsArray[2], locationAsArray[3].floatValue(), locationAsArray[4].floatValue());

                            ModelJsonObject modelJsonObject = new ModelJsonObject(UUID.fromString(model.get("uuid").asText()),model.get("model_type").asText(), location, model.get("current_animation").asText());
                            chunkJsonObject.addModel(modelJsonObject);


                        }

                    }

                    currentConfig.addWorld(worldJsonObject);
                }




            } catch (IOException e) {
                throw new RuntimeException(e);
            }






        }
        else {

            currentConfig = new FileJsonObject();
        }
    }

    public static void writeToDisk(){ // writes current config to disk
        currentConfig.writeToDisk();

        if (debugMode())
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "animated-skript: writing save data to disk");

    }

    public static FileJsonObject getCurrentConfig(){
        return currentConfig;
    }
    public static void saveAllActiveModelsToCurrentConfig(){
        if (ModelManager.getAllActiveModelsUuids().length == 0){
            return;
        }

        for (UUID uuid : ModelManager.getAllActiveModelsUuids()){
            ModelClass modelClass = ModelManager.getActiveModel(uuid);
            Location location = modelClass.getOriginLocation();

            if (!currentConfig.hasWorld(location.getWorld().getName())){
                currentConfig.addWorld(new WorldJsonObject(location.getWorld().getName()));
            }

            ChunkJsonObject chunkJsonObject = new ChunkJsonObject(location.getChunk().getX(), location.getChunk().getZ());

            currentConfig.getWorld(location.getWorld().getName()).addChunk(chunkJsonObject); // it will fail to make a new chunk if it exists

            ModelJsonObject modelJsonObject = new ModelJsonObject(modelClass);

            currentConfig.getWorld(location.getWorld().getName()).getChunk(chunkJsonObject.getName()).addModel(modelJsonObject);

        }


    }

}

