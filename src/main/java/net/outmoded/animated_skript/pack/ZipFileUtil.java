package net.outmoded.animated_skript.pack;


import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileUtil {

    ZipOutputStream zipOutputStream;
    Path zipFilePath;
    FileSystem fileSystem;

    ZipFileUtil(String zipOutputPath, FileSystem fileSystem) throws IOException {
        this.fileSystem = fileSystem;
        Path zipPath = Paths.get(zipOutputPath);
        if (!Files.exists(zipPath.getParent())) {
            Files.createDirectories(zipPath);
        }

        Files.deleteIfExists(zipPath);

        zipFilePath = Files.createFile(Paths.get(zipOutputPath));
        zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFilePath));

    }

    public void addToZip(String pathToGetZipped){
        Path sourceDirPath = fileSystem.getPath(pathToGetZipped); // gets the path that was passed as a string

        try(Stream<Path> paths = Files.walk(sourceDirPath)) { // basically a loop
            paths
                .filter(path -> !Files.isDirectory(path)) // checks if it is a file or path
                .forEach(path -> {
                    try {
                        ZipEntry zipEntry = new ZipEntry(path.toString());
                        zipOutputStream.putNextEntry(zipEntry);
                        Files.copy(path, zipOutputStream);
                        zipOutputStream.closeEntry();


                    }
                    catch (IOException e) {
                            throw new RuntimeException(e);
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void endZip(){
        try {
            zipOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
