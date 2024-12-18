package net.outmoded.modelengine.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class OnModelRemovedEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private String uuid;
    private String modelType;

    public OnModelRemovedEvent(String model, String type) {
        uuid = model;
        modelType = type;
    }

    public String getUuid() {
        return uuid;
    }

    public String getModelType() {
        return modelType;
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
