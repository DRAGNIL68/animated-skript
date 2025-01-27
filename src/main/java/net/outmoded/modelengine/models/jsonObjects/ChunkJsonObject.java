package net.outmoded.modelengine.models.jsonObjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.bukkit.Chunk;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ChunkJsonObject {
    final String name;

    private final Map<UUID, ModelJsonObject> models = new HashMap<>();

    public ChunkJsonObject(Chunk chunk){
        this.name = "chunk-x" + chunk.getX() + "-z" + chunk.getZ();
    }

    public String getName(){
        return name;
    }

    public void addModel(ModelJsonObject modelJsonObject){
        if (!models.containsKey(modelJsonObject.getUuid()))
            models.put(modelJsonObject.getUuid(), modelJsonObject);

    }
}
