package net.outmoded.animated_skript.pack;


import net.outmoded.animated_skript.pack.jsonObjects.Writable;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Namespace {

    private final String namespace;
    private final String namespacePath;
    private final ResourcePack resourcePack;

    public Namespace(String namespace, ResourcePack resourcePack) {
        this.namespace = namespace;
        this.resourcePack = resourcePack;
        namespacePath = "assets/" + namespace + "/";
        resourcePack.registerNamespace(this);
    }


    public String getNamespacePathAsString() {
        return namespacePath;
    }

    public Path getNamespacePath() {
        return Paths.get(namespacePath);
    }

    public String getNamespaceAsString() {
        return namespace;
    }

    public void writeJsonObject(Writable object, String writePath){
        resourcePack.writeJsonObject(object, namespacePath + writePath);
    }

    public void createGenericFile(String fileName, String  filePath, String contents){
        resourcePack.createGenericFile(fileName,namespacePath + filePath, contents);
    }

    public void copyFileFromDisk(String copyPath, String  pastePath) {
        resourcePack.copyFileFromDisk(copyPath, pastePath + pastePath);
    }

}
