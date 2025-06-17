package net.outmoded.animated_skript.models.jsonObjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChunkJsonObject {
    private final String name;

    public final int x;
    public final int z;

    private final Map<UUID, ModelJsonObject> models = new HashMap<>();



    public ChunkJsonObject(int chunkX, int chunkZ){
        this.name = "chunk-x" + chunkX + "-z" + chunkZ;
        x = chunkX;
        z = chunkZ;
    }

    public String getName(){
        return name;
    }

    public void addModel(ModelJsonObject modelJsonObject){ // allows overriding
        models.put(modelJsonObject.getUuid(), modelJsonObject);

    }

    public void removeModel(ModelJsonObject modelJsonObject){ // allows overriding
        if (models.containsKey(modelJsonObject.uuid))
            models.remove(modelJsonObject.getUuid(), modelJsonObject);

    }

    public Map<UUID, ModelJsonObject> getAllModels(){
        return models;
    }
    public ModelJsonObject getModel(UUID modelUuid){
        return models.get(modelUuid);
    }

    public boolean hasModel(UUID modelUuid){
        return models.containsKey(modelUuid);
    }

}
