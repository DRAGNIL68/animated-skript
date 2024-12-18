package net.outmoded.modelengine.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class OnModelStartAnimationEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private String uuid;
    private String modelType;
    private String animation;

    public OnModelStartAnimationEvent(String model, String type, String animationName) {
        uuid = model;
        modelType = type;
        animation = animationName;

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

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
