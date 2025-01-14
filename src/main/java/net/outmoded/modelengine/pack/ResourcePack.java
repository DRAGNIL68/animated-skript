package net.outmoded.modelengine.pack;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import net.outmoded.modelengine.pack.jsonObjects.WritableObject;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.HashMap;
import java.util.Map;

public class ResourcePack {
    FileSystem fileSystem;
    private final Map<String, Namespace> namespaces = new HashMap<>();


    public ResourcePack(){
        fileSystem = Jimfs.newFileSystem(Configuration.unix());

        createPath("/assets");

        Namespace minecraft = new Namespace("minecraft", this);

    }


    public void compile(){
        // clear old pack
        // set new pack to active
        // clear generated pack variable

    }

    @ApiStatus.Internal
    public void registerNamespace(Namespace namespace){ // registers texture packs so i can keep track on namespaces
        namespaces.put(namespace.getNamespaceAsString(), namespace);

    }

    public void createPath(String path){
        try {
            Path directory = fileSystem.getPath(path);
            Files.createDirectory(directory);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void createMcmeta(int version, String description){
        try {
            Path directory = fileSystem.getPath("/");
            Files.createDirectory(directory);

            Path hello = directory.resolve("pack.mcmeta");
            Files.write(hello, ImmutableList.of("{\"pack\":{\"pack_format\":" + version + "\"description\":\"" + description + "\"}}"), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void createGenericFile(String fileName, String path, String contents){
        try {
            Path directory = fileSystem.getPath(path);

            if (!Files.exists(directory)) {
                createPath(path);
            }

            Files.createDirectory(directory);

            Path file = directory.resolve(fileName);

            Files.write(file, ImmutableList.of(contents), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public boolean namespaceExists(String namespace){
        if (namespaces.containsKey(namespace))
            return true;
        else{
            return false;
        }

    }

    public Namespace getNamespace(String namespace){
        if (namespaces.containsKey(namespace))
            return namespaces.get(namespace);

        return null;

    }

    public void writeJsonObject(WritableObject object, String path){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Path directory = fileSystem.getPath(path);
            objectMapper.writeValue(Files.createFile(directory).toFile(), object);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }







}
