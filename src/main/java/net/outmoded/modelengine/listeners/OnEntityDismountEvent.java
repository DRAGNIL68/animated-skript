package net.outmoded.modelengine.listeners;

import net.outmoded.modelengine.ModelEngine;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Display;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.persistence.PersistentDataType;


public class OnEntityDismountEvent implements Listener {

    @EventHandler
    public void onEntityDismountEvent(EntityDismountEvent event) { // this code may or may not actually work
        // this code checks if a entity is part of a model and if it is the code fixes it
        if (event.getDismounted() instanceof Display && event.getEntity() instanceof Display) {

            Display origin = (Display) event.getDismounted();
            Display node = (Display) event.getEntity();

            NamespacedKey key = new NamespacedKey(ModelEngine.getInstance(), "nodeType");

            if (origin.getPersistentDataContainer().has(key)){
                if (node.getPersistentDataContainer().has(key)){
                    origin.addPassenger(origin); // this code dose not work with
                }
            }

        }

    }

}
