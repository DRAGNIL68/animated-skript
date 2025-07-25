package net.outmoded.animated_skript.models.new_stuff;

import com.fasterxml.jackson.databind.JsonNode;
import org.bukkit.util.Transformation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
//skript can return type, name, uuid and transformation (%itemdisplaytransform%)
public class Node {
    public final Map<String, Object> typeSpecificProperties = new HashMap<>(); //
    public String type;
    public String name;
    public UUID uuid;
    public String parent;
    public Float[] translation = new Float[2];
    public Float[] leftRotation = new Float[2];
    public Float[] scale = new Float[2];
    public Float[] pos = new Float[2];
    public Transformation transformation;

    public Node(){

    }




}
