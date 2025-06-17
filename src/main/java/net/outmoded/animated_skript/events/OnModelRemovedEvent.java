package net.outmoded.animated_skript.events;

import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class OnModelRemovedEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private UUID uuid;
    private String modelType;
    private ModelClass modelClass;

    public OnModelRemovedEvent(UUID uuid, String type , ModelClass modelClass) {
        this.uuid = uuid;
        modelType = type;
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

    public @NotNull HandlerList getHandlers() {
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
