package net.outmoded.animated_skript.listeners;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChunkLoadListener implements Listener {


    @EventHandler
    public void onPlayerChunkLoad(PlayerChunkLoadEvent event){
        Player player = event.getPlayer();
        Chunk chunk = event.getChunk();



    }

}
