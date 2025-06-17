package net.outmoded.animated_skript.listeners;

import net.outmoded.animated_skript.AnimatedSkript;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class OnJoin implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        NamespacedKey namespacedKey = new NamespacedKey(AnimatedSkript.getInstance(), "isSpectating");
        if (event.getPlayer().getPersistentDataContainer().has(namespacedKey)){
            event.getPlayer().setGameMode(GameMode.valueOf(event.getPlayer().getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING)));
            event.getPlayer().getPersistentDataContainer().remove(namespacedKey);
        }

    }
}
