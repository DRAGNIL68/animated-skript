package net.outmoded.animated_skript.pack.jsonObjects;

import java.util.HashMap;
import java.util.Map;

public class Model extends Writable {
    private final Map<String, Object> model = new HashMap<>();

    public Model(String modelNamespaceid){
        super(modelNamespaceid.substring(modelNamespaceid.lastIndexOf('/') + 1) + ".json");
        model.put("type", "minecraft:model");
        model.put("model", modelNamespaceid);

    }
}
