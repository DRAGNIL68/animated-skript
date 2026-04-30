package net.outmoded.animated_skript.models;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.Quaternion4f;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import net.outmoded.animated_skript.AnimatedSkript;
import net.outmoded.animated_skript.Config;
import net.outmoded.animated_skript.models.nodes.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PacketUtils {

    public static void sendModelToPlayers(ModelClass modelClass, ArrayList<Player> players){
        for (Node node : modelClass.nodeMap.values()){

            if (node.type.equals("item_display")){
                //sendItemDisplayToPlayers(modelClass, node, players,)

            }
            else if (node.type.equals("bone")){
                //sendItemDisplayToPlayers()

            }
        }


    }

    public static void sendItemDisplayToPlayers(ModelClass modelClass, Node node, ArrayList<Player> players, ItemStack itemStack){
        int id;
        if (modelClass.displayIds.containsKey(node.uuid)){
            id = modelClass.displayIds.get(node.uuid);
            AnimatedSkript.getInstance().getLogger().warning("node already exists");
        }
        else {
            id = SpigotReflectionUtil.generateEntityId();
            modelClass.displayIds.put(node.uuid, id);
            AnimatedSkript.getInstance().getLogger().warning("node dose not exist");
        }

        if (Config.debugMode())
            AnimatedSkript.getInstance().getLogger().warning("sending fake node with id:"+id+" to "+players);

        ItemDisplay origin = modelClass.origin;

        WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(
                    id,
                    Optional.of(UUID.randomUUID()),
                    EntityTypes.ITEM_DISPLAY,
                    new Vector3d(origin.getLocation().getX(), origin.getLocation().getY(), origin.getLocation().getZ()),
                    origin.getLocation().getPitch(),
                    origin.getLocation().getYaw(),
                    0,
                    0,
                    Optional.empty()
        );

        Quaternionf quaternionf = node.transformation.getLeftRotation();
        Quaternion4f quaternion4f = new Quaternion4f(quaternionf.x, quaternionf.y, quaternionf.z, quaternionf.w);

        Vector3f vector3f = node.transformation.getTranslation();
        com.github.retrooper.packetevents.util.Vector3f vector3f1 = new com.github.retrooper.packetevents.util.Vector3f(vector3f.x, vector3f.y, vector3f.z);

        Vector3f vector3fScale = node.transformation.getScale();
        com.github.retrooper.packetevents.util.Vector3f vector3f1Scale = new com.github.retrooper.packetevents.util.Vector3f(vector3fScale.x, vector3fScale.y, vector3fScale.z);

        WrapperPlayServerEntityMetadata playServerEntityMetadata = new WrapperPlayServerEntityMetadata(id,
                List.of(
                        new EntityData(23, EntityDataTypes.ITEMSTACK, SpigotReflectionUtil.decodeBukkitItemStack(itemStack)),
                        new EntityData(11, EntityDataTypes.VECTOR3F, vector3f1),
                        new EntityData(12, EntityDataTypes.VECTOR3F, vector3f1Scale),
                        new EntityData(13, EntityDataTypes.QUATERNION, quaternion4f)
                ));


        for (Player player : players){
            User user = PacketEvents.getAPI().getPlayerManager().getUser(player);

            user.sendPacket(spawnPacket);
            user.sendPacket(playServerEntityMetadata);
        }

    }
}
