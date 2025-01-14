package net.outmoded.modelengine.pack;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class PackUtils {


    public String textureToBase64(String filePath){
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
            return Base64.getEncoder().encodeToString(fileContent);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void base64ToTexture(String textureAsBase64, String path){
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(textureAsBase64);
            FileUtils.writeByteArrayToFile(new File(path), decodedBytes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
