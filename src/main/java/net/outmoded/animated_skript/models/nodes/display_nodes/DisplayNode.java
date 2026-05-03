package net.outmoded.animated_skript.models.nodes.display_nodes;

import org.bukkit.util.Transformation;

abstract public class DisplayNode {
    private int id;
    private Transformation transformation;

    public DisplayNode(int id){
        this.id = id;

    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTransformation(Transformation transformation){
        this.transformation = transformation;
    }

    public Transformation getTransformation() {
        return transformation;
    }


}
