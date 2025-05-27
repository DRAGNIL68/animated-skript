package net.outmoded.animated_skript.events;

import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public final class OnModelSpawnedEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    private UUID uuid;
    private String modelType;
    private ModelClass modelClass;
    public OnModelSpawnedEvent(UUID uuid, String type, ModelClass modelClass) {
        this.uuid = uuid;
        modelType = type;
        this.modelClass = modelClass;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getModelType() {
        return modelType;
    }

    public ModelClass getActiveModel() {
        return modelClass;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
