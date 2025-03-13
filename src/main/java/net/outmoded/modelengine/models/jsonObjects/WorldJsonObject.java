package net.outmoded.modelengine.models.jsonObjects;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.outmoded.modelengine.ModelEngine;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class WorldJsonObject {

    String name;
    private final Map<String, ChunkJsonObject> chunks = new HashMap<>();



    public WorldJsonObject(String worldName){
        name = worldName;

    }

    public void addChunk(ChunkJsonObject chunkJsonObject){
        if (!chunks.containsKey(chunkJsonObject.getName()))
            chunks.put(chunkJsonObject.getName(), chunkJsonObject);

    }

    public ChunkJsonObject getChunk(String chunkName){
        if (chunks.containsKey(chunkName))
            return chunks.get(chunkName);

        return null;
    }

    public boolean hasChunk(String chunkName){
        return chunks.containsKey(chunkName);
    }

    public String getName(){
        return name;
    }

}
