package net.outmoded.modelengine.models.jsonObjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.outmoded.modelengine.models.ModelClass;
import org.bukkit.Location;

import java.util.UUID;

public class ModelJsonObject {
    public final UUID uuid;

    @JsonProperty("model_type")
    public final String modelType;

    @JsonProperty("location")
    public final Double[] location;

    @JsonProperty("current_animation")
    public final String currentAnimation;

    @JsonIgnore
    public final Location locationAsLocation;



    public ModelJsonObject(ModelClass modelClass){
        this.uuid = modelClass.getUuid();
        this.modelType = modelClass.getModelType();
        Location originLocation = modelClass.getOriginLocation();
        location = new Double[]{originLocation.getX(), originLocation.getY(), originLocation.getZ(), (double) originLocation.getPitch(), (double) originLocation.getYaw()} ;
        this.currentAnimation = modelClass.getCurrentAnimation();
        locationAsLocation = originLocation;
    }

    public ModelJsonObject(UUID uuid, String modelType, Location location, String currentAnimation){
        this.uuid = uuid;
        this.modelType = modelType;
        this.location = new Double[]{location.getX(), location.getY(), location.getZ(), (double) location.getYaw(), (double) location.getPitch()};
        this.currentAnimation = currentAnimation;
        locationAsLocation = location;
    }

    public UUID getUuid(){
        return uuid;
    }
}
