package net.outmoded.animated_skript.pack.jsonObjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tint {
    @JsonProperty("type")
    private String type = "minecraft:dye";

    @JsonProperty("default")
    private int defaultC = 16777215;

}