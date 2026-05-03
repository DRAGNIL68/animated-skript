package net.outmoded.animated_skript.models.nodes.display_nodes;

import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemModel;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ItemDisplayNode extends DisplayNode{
    private com.github.retrooper.packetevents.protocol.item.ItemStack decodedItemStack;

    public ItemDisplayNode(int id, ItemStack itemStack){
        super(id);
        this.decodedItemStack = SpigotReflectionUtil.decodeBukkitItemStack(itemStack);


    }

    public void setItemStackVariant(String modelType, String variant, UUID nodeUuid){
        ResourceLocation resourceLocation = new ResourceLocation("animated-skript:"+ modelType + "/" + variant + "/" + nodeUuid.toString());
        this.decodedItemStack.setComponent(ComponentTypes.ITEM_MODEL, new ItemModel(resourceLocation));
    }

    public void setItemStack(ItemStack itemStack){
        this.decodedItemStack = SpigotReflectionUtil.decodeBukkitItemStack(itemStack);
    }

    public com.github.retrooper.packetevents.protocol.item.ItemStack getDecodedItemStack() {
        return decodedItemStack;
    }

    public void setDecodedItemStack(com.github.retrooper.packetevents.protocol.item.ItemStack decodedItemStack) {
        this.decodedItemStack = decodedItemStack;
    }


}
