package net.outmoded.animated_skript.events;

import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class ActiveModelHitboxInteract extends Event{
    private static final HandlerList handlers = new HandlerList();
    private final ModelClass modelClass;
    private final Player player;

    public UUID getHitboxUuid() {
        return hitboxUuid;
    }

    private final UUID hitboxUuid;

    public ActiveModelHitboxInteract(ModelClass modelClass, Player player, UUID hitboxUuid) {
        this.modelClass = modelClass;
        this.player = player;
        this.hitboxUuid = hitboxUuid;

    }

    public ModelClass getActiveModel() {
        return modelClass;
    }

    public Player getPlayer() {
        return player;
    }


    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
