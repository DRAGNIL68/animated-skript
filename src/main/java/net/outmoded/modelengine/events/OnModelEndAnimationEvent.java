package net.outmoded.modelengine.events;

import net.outmoded.modelengine.models.ModelClass;
import net.outmoded.modelengine.models.ModelManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class OnModelEndAnimationEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private UUID uuid;
    private String modelType;
    private String animation;
    private boolean loopMode;
    private ModelClass modelClass;

    public OnModelEndAnimationEvent(UUID uuid, String type, String animationName, boolean loopMode) {
        this.uuid = uuid;
        modelType = type;
        animation = animationName;
        this.loopMode = loopMode;
        this.modelClass = ModelManager.getActiveModel(uuid);

    }

    public ModelClass getActiveModel() {
        return modelClass;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getModelType() {
        return modelType;
    }

    public String getAnimation() {
        return animation;
    }

    public boolean getLoopMode() {
        return loopMode;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
