package net.outmoded.modelengine.events;

import net.outmoded.modelengine.models.ModelClass;
import net.outmoded.modelengine.models.ModelManager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public final class OnModelStartAnimationEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final UUID uuid;
    private final String modelType;
    private final String animation;
    private ModelClass modelClass;

    public OnModelStartAnimationEvent(UUID uuid, String type, String animationName) {
        this.uuid = uuid;
        modelType = type;
        animation = animationName;
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
