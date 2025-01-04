package net.outmoded.modelengine.pack;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PackBuilder {
    FileSystem fileSystem;
    private final Map<String, String> namespaces = new HashMap<>();

    public void startBuild(){
        // For a simple file system with Unix-style paths and behavior:
        try {

            fileSystem = Jimfs.newFileSystem(Configuration.unix());

            createPath("/assets");
            addNamespace("minecraft");


        } catch (Exception e){
            throw new RuntimeException(e);

        }

    }


    public void endBuild(){
        // clear old pack
        // set new pack to active
        // clear generated pack variable

    }

    public void addNamespace(String namespace){
        createPath("/assets/" + namespace);
        namespaces.put(namespace, "/assets/" + namespace);

    }

    public void createPath(String path){
        try {
            Path directory = fileSystem.getPath(path);
            Files.createDirectory(directory);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void createMcmeta(String description){
        try {
            Path directory = fileSystem.getPath("/");
            Files.createDirectory(directory);

            Path hello = directory.resolve("pack.mcmeta");
            Files.write(hello, ImmutableList.of("{\"pack\":{\"pack_format\":32,\"description\":\"" + description + "\"}}"), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void createFile(String fileName ,String contents, String path){
        try {
            Path directory = fileSystem.getPath(path);
            Files.createDirectory(directory);

            Path file = directory.resolve(fileName);
            Files.write(file, ImmutableList.of(contents), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
