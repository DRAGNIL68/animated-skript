package net.outmoded.animated_skript.events;

import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.ModelManager;
import net.outmoded.animated_skript.models.nodes.Animation;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class ModelAnimationEndEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private UUID uuid;
    private String modelType;
    private String animation;
    private String loopMode;
    private ModelClass modelClass;

    public ModelAnimationEndEvent(UUID uuid, String type, String animationName, String loopMode) {
        this.uuid = uuid;
        modelType = type;
        animation = animationName;
        this.loopMode = loopMode;
        this.modelClass = ModelManager.getInstance().getActiveModel(uuid);

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

    public Animation getAnimation() {
        return modelClass.getAnimation(animation);
    }

    public String getLoopMode() {
        return loopMode;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
