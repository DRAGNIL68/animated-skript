package net.outmoded.animated_skript.models.nodes;

import org.bukkit.util.Transformation;

import java.util.HashMap;
import java.util.Map;
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
    public Node lightClone(){
        Node node = new Node();
        node.type = this.type;
        node.name = this.name;
        node.uuid = this.uuid;

        node.leftRotation = this.leftRotation.clone();
        node.scale = this.scale.clone();
        node.pos = this.pos.clone();

        node.transformation = new Transformation(this.transformation.getTranslation(), this.transformation.getLeftRotation(), this.transformation.getScale(), this.transformation.getRightRotation());


        return node;
    }




}
