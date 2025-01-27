package net.outmoded.modelengine.models.jsonObjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.outmoded.modelengine.ModelEngine;
import org.bukkit.Chunk;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FileJsonObject {

    final Map<String, ChunkJsonObject> chunks = new HashMap<>();


    public void addChunk(ChunkJsonObject chunkJsonObject){
        if (!chunks.containsKey(chunkJsonObject.getName()))
            chunks.put(chunkJsonObject.getName(), chunkJsonObject);

    }

    public ChunkJsonObject getChunk(String chunkName){
        if (chunks.containsKey(chunkName))

            return chunks.get(chunkName);

        return null;
    }

    public void writeToDisk() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File dataFolder = ModelEngine.getInstance().getDataFolder();
        objectMapper.writeValue(new File(dataFolder.getPath() + "/save_data.json"), this);
    }
}
