package net.outmoded.modelengine.pack.jsonObjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestWritableJsonObject extends WritableObject {
    @JsonIgnore
    private int num;

    private TestNonWritableClass testNonWritableClass;

    @JsonProperty("renamedField")
    private String frog;

    public TestWritableJsonObject(String frog){
        this.frog = frog;
        testNonWritableClass = new TestNonWritableClass(frog);
    }



}
