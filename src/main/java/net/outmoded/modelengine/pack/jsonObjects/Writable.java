package net.outmoded.modelengine.pack.jsonObjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Writable{

    @JsonIgnore
    private final String fileName;

    Writable(String fileName){
        this.fileName = fileName;
    }

    public String getFileName(){
        return fileName;

    }

}
