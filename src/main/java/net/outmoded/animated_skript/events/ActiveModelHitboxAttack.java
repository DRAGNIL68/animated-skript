package net.outmoded.animated_skript.events;

import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class ActiveModelHitboxAttack extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final ModelClass modelClass;
    private final UUID hitboxUuid;
    private final Entity damager;
    private final boolean isCritical;
    private final DamageSource damageSource;
    private final EntityDamageEvent.DamageCause damageCause;
    private final Double finalDamage;
    private final Double damage;

    private boolean aBoolean = false;

    public Double getFinalDamage() {
        return finalDamage;
    }

    public EntityDamageEvent.DamageCause getDamageCause() {
        return damageCause;
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }

    public boolean isCritical() {
        return isCritical;
    }

    public Entity getDamager() {
        return damager;
    }

    public UUID getHitboxUuid() {
        return hitboxUuid;
    }

    public ModelClass getModelClass() {
        return modelClass;
    }



    public ActiveModelHitboxAttack(ModelClass modelClass, UUID hitboxUuid, Entity damager, boolean isCritical, DamageSource damageSource, EntityDamageEvent.DamageCause damageCause,Double damage ,Double finalDamage) {
        this.modelClass = modelClass;
        this.hitboxUuid = hitboxUuid;

        this.damager = damager;
        this.isCritical = isCritical;
        this.damageSource = damageSource;
        this.damageCause = damageCause;
        this.damage = damage;
        this.finalDamage = finalDamage;

    }

    public ModelClass getActiveModel() {
        return modelClass;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return aBoolean;
    }

    @Override
    public void setCancelled(boolean b) {
        aBoolean = b;
    }
}
