package net.outmoded.modelengine;

import net.outmoded.modelengine.pack.Namespace;
import net.outmoded.modelengine.pack.ResourcePack;
import net.outmoded.modelengine.pack.jsonObjects.Model;
import net.outmoded.modelengine.pack.jsonObjects.McMeta;

import static org.bukkit.Bukkit.getServer;

public class test {

    public static void runPack(){
        ResourcePack resourcePack = new ResourcePack("animated-skript"); // <- create new resourcePack
        resourcePack.writeJsonObject(new McMeta("frogs", 42), ""); // <- creates pack.mcmeta

        Namespace animatedSkript = new Namespace("animated-skript", resourcePack); // <- creates new namespace

        animatedSkript.createGenericFile("frog.txt", "frog", "ogg"); // generic file

        animatedSkript.copyFileFromDisk("plugins/packtest/katana.json", "models/item/katana.json"); // copy file from the disk to th texture pack
        animatedSkript.copyFileFromDisk("plugins/packtest/katana.png", "textures/item/katana.png");

        animatedSkript.writeJsonObject(new Model("animated-skript:item/katana"), "items"); // i.e. animated-skript:items/katana.json
        resourcePack.base64ToTexture("frog.png", "assets/", "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAAXNSR0IArs4c6QAAAORJREFUeF7t1sERwzAMA0GputSc6pRxDfcQndn8YdMEgctea5317t8u4z9iCygbHKB1AcUEEdABShAFYLC06AAtDBYTYBAGYRAGYbC06AAtDBYTYBAGYRAGYbC06AAtDBYTYBAGYRAGYbC06AAtDBYTJmAwOVg+/tFawID/AS6gnnHRi4AIlPv5A+3VApqwPwuY4MLNGVzAze1PeLcLmOBCmeGc8yn611+ABbz5ArJ7e3/zM0p+qjYPbwEuQARyjGqOiz4PrwN0gA7IMSoZrto8vA7QATogx6jmuOjz8DpAB+QO+AGBF6RNi0nBcwAAAABJRU5ErkJggg==");
        resourcePack.build("plugins/" + resourcePack.getName() + ".zip"); // <- generates zip



        // custom item -> model data start at 0 unless there is an offset

    }




}
