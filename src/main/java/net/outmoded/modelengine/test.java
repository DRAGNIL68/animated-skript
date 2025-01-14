package net.outmoded.modelengine;

import net.outmoded.modelengine.pack.Namespace;
import net.outmoded.modelengine.pack.ResourcePack;
import net.outmoded.modelengine.pack.jsonObjects.TestWritableJsonObject;

public class test {

    public void runPack(){
        ResourcePack resourcePack = new ResourcePack();

        resourcePack.createMcmeta(32, "§4frogs are cool");
        resourcePack.writeJsonObject(new TestWritableJsonObject("frog"), "" );

        Namespace animatedSkript = new Namespace("animated-skript", resourcePack);
        animatedSkript.createPath("/items/cool_shit");



        // custom item -> model data start at 0 unless there is an offset

    }




}
