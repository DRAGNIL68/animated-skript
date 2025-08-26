package net.outmoded.animated_skript.events;

import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.ModelManager;
import net.outmoded.animated_skript.models.nodes.Animation;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class ModelFrameSetAnimationEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private UUID uuid;
    private String modelType;
    private String animation;
    private String loopMode;
    private int oldFrameTime;
    private int futureFrameTime;
    private int maxFrameTime;
    private ModelClass modelClass;

    public int getOldFrameTime() {
        return oldFrameTime;
    }

    public int getFutureFrameTime() {
        return futureFrameTime;
    }

    public int getMaxFrameTime() {
        return maxFrameTime;
    }

    public ModelFrameSetAnimationEvent(UUID uuid, String type, String animationName, String loopMode, int oldFrameTime, int futureFrameTime, int maxFrameTime) {
        this.uuid = uuid;
        modelType = type;
        animation = animationName;
        this.loopMode = loopMode;
        this.futureFrameTime = futureFrameTime;
        this.oldFrameTime = oldFrameTime;
        this.maxFrameTime = maxFrameTime;
        this.modelClass = ModelManager.getInstance().getActiveModel(uuid);

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

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
