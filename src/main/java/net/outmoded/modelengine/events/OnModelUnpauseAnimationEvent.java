package net.outmoded.modelengine.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class OnModelUnpauseAnimationEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private UUID uuid;
    private String modelType;
    private String animation;
    private boolean loopMode;

    public OnModelUnpauseAnimationEvent(UUID uuid, String type, String animationName, boolean loopMode) {
        this.uuid = uuid;
        modelType = type;
        animation = animationName;
        this.loopMode = loopMode;

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

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
