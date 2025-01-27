package net.outmoded.modelengine.pack.jsonObjects;

public class TestNonWritableClass extends NonWritable {


    private final String name;

    TestNonWritableClass(String name) {
        this.name = name;

    }

    public String getName(){
        return name;
    }
}
