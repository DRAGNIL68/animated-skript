package net.outmoded.animated_skript.pack.jsonObjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TestWritableJson extends Writable {
    @JsonIgnore
    private int num;

    private TestNonWritableClass testNonWritableClass;

    @JsonProperty("renamedField")
    private String frog;

    public TestWritableJson(String frog){
        super(frog + ".txt");
        this.frog = frog;
        testNonWritableClass = new TestNonWritableClass(frog);
    }



}
