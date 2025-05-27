package net.outmoded.animated_skript.pack.jsonObjects;

public class TestNonWritableClass extends NonWritable {


    private final String name;

    TestNonWritableClass(String name) {
        this.name = name;

    }

    public String getName(){
        return name;
    }
}
