package net.outmoded.modelengine.events;

import net.outmoded.modelengine.models.ModelClass;
import net.outmoded.modelengine.models.ModelManager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class OnModelPauseAnimationEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private UUID uuid;
    private String modelType;
    private String animation;
    private boolean loopMode;
    private ModelClass modelClass;

    public OnModelPauseAnimationEvent(UUID uuid, String type, String animationName, boolean loopMode) {
        this.uuid = uuid;
        modelType = type;
        animation = animationName;
        this.loopMode = loopMode;
        this.modelClass = ModelManager.getInstance().getActiveModel(uuid);

    }

    public boolean isLoopMode() {
        return loopMode;
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

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
