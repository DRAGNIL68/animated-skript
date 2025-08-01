package net.outmoded.animated_skript.listeners;

import net.outmoded.animated_skript.AnimatedSkript;
import net.outmoded.animated_skript.Config;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.persistence.PersistentDataType;


public class OnEntityDismountEvent implements Listener {

    @EventHandler()
    public void onEntityDismountEvent(EntityDismountEvent event) { // this code may or may not actually work
        // this code checks if an entity is part of a model and if it is the code fixes it

        if (event.getDismounted() instanceof Display ) {
            if (event.getEntity() instanceof Display || event.getEntity() instanceof Interaction){

                Display origin = (Display) event.getDismounted();
                Display node = (Display) event.getEntity();

                NamespacedKey key = new NamespacedKey(AnimatedSkript.getInstance(), "nodeType");
                NamespacedKey key1 = new NamespacedKey(AnimatedSkript.getInstance(), "isTeleporting");

                if (origin.getPersistentDataContainer().has(key1)){
                    AnimatedSkript.getInstance().getLogger().warning("fuking bug1");
                    return;
                }


                if (origin.getPersistentDataContainer().has(key)){

                    if (node.getPersistentDataContainer().has(key)){
                        AnimatedSkript.getInstance().getLogger().warning("fuking bug");
                        origin.addPassenger(node);
                    }
                }

            }


        }

    }

    @EventHandler
    public void onPlayerStopsSpectatingNode(EntityDismountEvent event){

        NamespacedKey namespacedKey = new NamespacedKey(AnimatedSkript.getInstance(), "isSpectating");
        NamespacedKey key = new NamespacedKey(AnimatedSkript.getInstance(), "nodeType");
        

        
        if (event.getEntity() instanceof Player && event.getDismounted().getPersistentDataContainer().has(key)){
            if (!event.getDismounted().isValid()){
                if (event.getEntity().getPersistentDataContainer().has(namespacedKey)) {
                    ((Player) event.getEntity()).setGameMode(GameMode.valueOf(event.getEntity().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING)));
                    event.getEntity().getPersistentDataContainer().remove(namespacedKey);
                }
           }

            else if (event.getEntity().getPersistentDataContainer().has(namespacedKey)){
                event.setCancelled(true);
            }


        }

    }

}
