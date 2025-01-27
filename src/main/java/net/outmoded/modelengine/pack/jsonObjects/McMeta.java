package net.outmoded.modelengine.pack.jsonObjects;

import net.outmoded.modelengine.models.jsonObjects.ModelJsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class McMeta extends Writable {

    private final Map<String, Object> pack = new HashMap<>();
    public McMeta(String description, int version){
        super("pack.mcmeta");
        pack.put("pack_format", version);
        pack.put("description", description);


    }

}
