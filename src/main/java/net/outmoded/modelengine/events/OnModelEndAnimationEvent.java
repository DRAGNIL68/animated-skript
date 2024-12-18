package net.outmoded.modelengine.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class OnModelEndAnimationEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private String uuid;
    private String modelType;
    private String animation;
    private boolean loopMode;

    public OnModelEndAnimationEvent(String model, String type, String animationName, boolean loopMode) {
        uuid = model;
        modelType = type;
        animation = animationName;
        this.loopMode = loopMode;

    }

    public String getUuid() {
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

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
