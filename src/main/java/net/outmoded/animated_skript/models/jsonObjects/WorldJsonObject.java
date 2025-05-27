package net.outmoded.animated_skript.models.jsonObjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

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
