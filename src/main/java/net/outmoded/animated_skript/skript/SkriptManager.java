package net.outmoded.animated_skript.skript;

import net.outmoded.animated_skript.models.ModelClass;

public class SkriptManager {

    static ModelClass lastSpawnedModelClass;


    public static void setLastSpawnedModelClass(ModelClass lastSpawnedModelClass) {
        SkriptManager.lastSpawnedModelClass = lastSpawnedModelClass;
    }

    public static ModelClass getLastSpawnedModelClass() {
        return lastSpawnedModelClass;
    }





}
