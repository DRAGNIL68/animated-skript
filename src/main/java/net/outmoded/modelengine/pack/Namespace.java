package net.outmoded.modelengine.pack;


public class Namespace {

    private final String namespace;
    private final String namespacePath;
    private final ResourcePack resourcePack;

    public Namespace(String namespace, ResourcePack resourcePack) {
        this.namespace = namespace;
        this.resourcePack = resourcePack;
        namespacePath = "/assets/" + namespace;
        resourcePack.registerNamespace(this);
    }


    public String getNamespacePath() {
        return namespacePath;
    }

    public String getNamespaceAsString() {
        return namespace;
    }

    public void createPath(String path){
        resourcePack.createPath(namespacePath + path);
    }

    public void createGenericFile(String fileName, String filePath, String contents ){
        resourcePack.createGenericFile(fileName,namespacePath + filePath, contents);
    }

}
