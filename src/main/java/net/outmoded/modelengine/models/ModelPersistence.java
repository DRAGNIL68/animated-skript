package net.outmoded.modelengine.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.outmoded.modelengine.models.jsonObjects.ChunkJsonObject;
import net.outmoded.modelengine.models.jsonObjects.FileJsonObject;
import net.outmoded.modelengine.models.jsonObjects.ModelJsonObject;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.io.IOException;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ModelPersistence implements Listener {
    private static FileJsonObject currentConfig;


    @EventHandler
    private static void onChuckLoad(ChunkLoadEvent event){

    }

    @EventHandler
    private static void onChuckUnload(ChunkUnloadEvent event){
        //event.isSaveChunk()
    }

    public static void loadLastConfig(){
        // loads config from file (current config)

    }


    public static void saveModels() {
        try {
            FileJsonObject fileJsonObject = new FileJsonObject();

            UUID[] models = ModelManager.getAllActiveModelsUuids();


            for (UUID uuid : models) {
                Chunk chunk = ModelManager.getActiveModel(uuid).getOriginLocation().getChunk();

                ModelClass model = ModelManager.getActiveModel(uuid);

                String chunkName = "chunk-x" + chunk.getX() + "-z" + chunk.getZ();
                if (fileJsonObject.getChunk(chunkName) == null) {
                    fileJsonObject.addChunk(new ChunkJsonObject(chunk));
                }

                fileJsonObject.getChunk(chunkName).addModel(new ModelJsonObject(model));
                // "chunk-x3-z4"

                if (!chunk.isLoaded()){
                    ModelManager.removeActiveModel(uuid);

                }

            }
            fileJsonObject.writeToDisk();
        }
        catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private static void loadModels(){

    }
}
