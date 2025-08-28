package net.outmoded.animated_skript.listeners;

import net.outmoded.animated_skript.AnimatedSkript;
import net.outmoded.animated_skript.events.ActiveModelHitboxAttack;
import net.outmoded.animated_skript.events.ModelFrameSetAnimationEvent;
import net.outmoded.animated_skript.events.ActiveModelHitboxInteract;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.ModelManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Interaction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class OnPlayerInteractEvent implements Listener {


    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event){
        NamespacedKey key = new NamespacedKey(AnimatedSkript.getInstance(), "nodeType");
        NamespacedKey nodeUuid = new NamespacedKey(AnimatedSkript.getInstance(), "nodeUuid");
        NamespacedKey uuidKey = new NamespacedKey(AnimatedSkript.getInstance(), "modelUuid");
        if (event.getRightClicked() instanceof Interaction){
            Interaction interaction = (Interaction) event.getRightClicked();

            if (interaction.getPersistentDataContainer().has(uuidKey)){
                UUID hitboxUuid = UUID.fromString(interaction.getPersistentDataContainer().get(nodeUuid, PersistentDataType.STRING));
                UUID modelUuid = UUID.fromString(interaction.getPersistentDataContainer().get(uuidKey, PersistentDataType.STRING));
                ActiveModelHitboxInteract newEvent = new ActiveModelHitboxInteract(ModelManager.getInstance().getActiveModel(modelUuid), event.getPlayer(), hitboxUuid);
                Bukkit.getPluginManager().callEvent(newEvent);

            }
        }

    }


    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event){
        NamespacedKey key = new NamespacedKey(AnimatedSkript.getInstance(), "nodeType");
        NamespacedKey uuidKey = new NamespacedKey(AnimatedSkript.getInstance(), "modelUuid");
        NamespacedKey nodeUuid = new NamespacedKey(AnimatedSkript.getInstance(), "nodeUuid");

        if (event.getEntity() instanceof Interaction){
            Interaction interaction = (Interaction) event.getEntity();

            if (interaction.getPersistentDataContainer().has(uuidKey)){
                UUID hitboxUuid = UUID.fromString(interaction.getPersistentDataContainer().get(nodeUuid, PersistentDataType.STRING));
                UUID modelUuid = UUID.fromString(interaction.getPersistentDataContainer().get(uuidKey, PersistentDataType.STRING));
                ModelClass modelClass = ModelManager.getInstance().getActiveModel(modelUuid);

                ActiveModelHitboxAttack activeModelHitboxAttack = new ActiveModelHitboxAttack(modelClass, hitboxUuid, event.getDamager(), event.isCritical(), event.getDamageSource(), event.getCause(), event.getDamage() ,event.getFinalDamage());
                Bukkit.getPluginManager().callEvent(activeModelHitboxAttack);
                if (activeModelHitboxAttack.isCancelled()){
                    event.setCancelled(true);
                }

            }
        }

    }
}
