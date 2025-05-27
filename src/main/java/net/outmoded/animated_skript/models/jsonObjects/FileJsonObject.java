package net.outmoded.animated_skript.models.jsonObjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.outmoded.animated_skript.AnimatedSkript;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.outmoded.animated_skript.Config.debugMode;
import static org.bukkit.Bukkit.getServer;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FileJsonObject {

    private final Map<String, WorldJsonObject> worlds = new HashMap<>();


    public void addWorld(WorldJsonObject worldJsonObject){
        if (!worlds.containsKey(worldJsonObject.getName()))
            worlds.put(worldJsonObject.getName(), worldJsonObject);
        if (debugMode())
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "animated-skript: world added to save data");
    }

    public WorldJsonObject getWorld(String chunkName){
        if (worlds.containsKey(chunkName))
            return worlds.get(chunkName);
        return null;
    }
    public boolean hasWorld(String worldName){
        return worlds.containsKey(worldName);
    }

    public void writeToDisk() {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            File dataFolder = AnimatedSkript.getInstance().getDataFolder();
            objectMapper.writeValue(new File(dataFolder.getPath() + "/save_data.json"), this);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
