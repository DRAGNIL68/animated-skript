package net.outmoded.modelengine.skript;

import net.outmoded.modelengine.models.ModelClass;

public class SkriptManager {

    static ModelClass lastSpawnedModelClass;


    public static void setLastSpawnedModelClass(ModelClass lastSpawnedModelClass) {
        SkriptManager.lastSpawnedModelClass = lastSpawnedModelClass;
    }

    public static ModelClass getLastSpawnedModelClass() {
        return lastSpawnedModelClass;
    }





}
