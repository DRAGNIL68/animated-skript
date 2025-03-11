package net.outmoded.modelengine.models.jsonObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.outmoded.modelengine.models.ModelClass;
import org.bukkit.Location;

import java.util.UUID;

public class ModelJsonObject {
    public final UUID uuid;

    @JsonProperty("model_type")
    public final String modelType;

    @JsonProperty("location")
    public final Location location;

    @JsonProperty("current_animation")
    public final String currentAnimation;


    public ModelJsonObject(ModelClass modelClass){
        this.uuid = modelClass.getUuid();
        this.modelType = modelClass.getModelType();
        Location location1 = modelClass.getOriginLocation();
        location = modelClass.getOriginLocation();
        this.currentAnimation = modelClass.getCurrentAnimation();
    }

    public UUID getUuid(){
        return uuid;
    }
}
