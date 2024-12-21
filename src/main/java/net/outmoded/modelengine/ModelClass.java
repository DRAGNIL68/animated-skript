package net.outmoded.modelengine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.outmoded.modelengine.events.OnModelEndAnimationEvent;
import net.outmoded.modelengine.events.OnModelStartAnimationEvent;
import org.bukkit.*;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;


import java.util.*;


import static net.outmoded.modelengine.Config.debugMode;
import static org.bukkit.Bukkit.getServer;

public class ModelClass { // TODO: destroy this shit code
    private final Map<String, Display> activeNodes = new HashMap<>();
    private final Map<String, JsonNode> loadedAnimations = new HashMap<>();

    public final String modelType;
    private Location origin;
    private JsonNode config;
    private String alias; // not used
    private String uuid;

    //current animation info
    String currentAnimationName = null;
    JsonNode frames = null;
    Integer currentFrameTime = 0; // in ticks 1T = 0.05S | 0.05 x 20 = 1S
    Integer maxFrameTime = 0;
    Integer loopDelay = 0; // in ticks does not need converting
    Boolean isActive = false;
    Boolean loopMode = false;

    ModelClass(Location location, String modelType, String uuid) {
        this.modelType = modelType;
        this.uuid = uuid;
        origin = location;
        config = ModelManager.getLoadedModel(modelType);
        loadAnimations();
        spawnModelNodes();
    }

    public void loadAnimations(){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            resetAnimation();
            JsonNode root = config;

            JsonNode nodes = root.path("animations");

            nodes.forEach(loopedAnimation -> {
                String name = loopedAnimation.get("name").asText(); // uuid of animation
                loadedAnimations.put(name, loopedAnimation);

            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void loadConfig(){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            config = ModelManager.getLoadedModel(modelType); // gets json config for model
            deleteModelNodes(); // deletes nodes
            spawnModelNodes(); // respawns nodes
            loadAnimations(); // loads animations and resets current animation


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    // TODO: this method is to long: brake it down into smaller parts
    public void spawnModelNodes(){
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = config.get("nodes");

            root.forEach(node -> {
                try {

                    JsonNode defaultTransform = node.get("default_transform");
                    JsonNode decomposed = defaultTransform.get("decomposed");


                    Float[] posAsArray = objectMapper.treeToValue(defaultTransform.get("pos"), Float[].class);
                    Integer[] scaleAsArray = objectMapper.treeToValue(defaultTransform.get("scale"), Integer[].class); // "scale": [1, 1, 1]
                    Float[] translationAsArray = objectMapper.treeToValue(decomposed.get("translation"), Float[].class); // "translation": [0, 0, 0]
                    Float[] left_rotationAsArray = objectMapper.treeToValue(decomposed.get("left_rotation"), Float[].class); // "left_rotation": [0, 1, 0, 0]
                    String uuid = node.get("uuid").asText();

                    Display display = null;

                    Vector posAsVector = new Vector(posAsArray[0], posAsArray[1], posAsArray[2]);
                    Location posAsLoc = new Location(origin.getWorld(), posAsArray[0], posAsArray[1], posAsArray[2]);

                    String displayType = node.get("type").asText();

                    if (displayType.equals("struct")) {


                        ItemDisplay itemDisplay = origin.getWorld().spawn(origin, ItemDisplay.class);
                        itemDisplay.setItemStack(new ItemStack(Material.SKELETON_SKULL));
                        display = itemDisplay;
                    }
                    else if (displayType.equals("block_display")) {

                        Material material = Material.valueOf(node.get("block").asText().replace("minecraft:", "").toUpperCase());
                        BlockDisplay blockDisplay = origin.getWorld().spawn(origin, BlockDisplay.class);
                        blockDisplay.setBlock(material.createBlockData());
                        display = blockDisplay;
                    }
                    else if (displayType.equals("item_display")) {

                        Material material = Material.valueOf(node.get("item").asText().replace("minecraft:", "").toUpperCase());
                        ItemDisplay itemDisplay = origin.getWorld().spawn(origin, ItemDisplay.class);
                        itemDisplay.setItemStack(new ItemStack(material));
                        display = itemDisplay;
                    }
                    else if (displayType.equals("text_display")) {

                        String text = node.get("text").asText();
                        String align = node.get("align").asText().toUpperCase();
                        boolean shadow = node.get("shadow").asBoolean();
                        boolean seeThrough = node.get("see_through").asBoolean();

                        TextDisplay textDisplay = origin.getWorld().spawn(origin, TextDisplay.class);

                        textDisplay.setText(text);
                        textDisplay.setShadowed(shadow);
                        textDisplay.setSeeThrough(seeThrough);
                        textDisplay.setAlignment(TextDisplay.TextAlignment.valueOf(align));

                        display = textDisplay;
                    }
                    else {
                        throw new RuntimeException("Corrupted node in json file: " + modelType + ".json node: " + node.get("uuid"));
                    }

                    if (node.has("config")) {

                        if (node.get("config").has("override_brightness")) {
                            boolean overrideBrightness = node.get("config").get("override_brightness").asBoolean();

                            if (overrideBrightness) {

                                if (node.get("config").has("brightness_override")) {
                                    Integer brightnessOverride = node.get("config").get("brightness_override").asInt();
                                    Display.Brightness brightness = new Display.Brightness(brightnessOverride, brightnessOverride);
                                    display.setBrightness(brightness);
                                }
                            }
                        }

                        if (node.get("config").has("shadow_radius")) {
                            display.setShadowRadius(node.get("config").get("shadow_radius").asInt());
                        }

                        if (node.get("config").has("name")) {
                            String name = node.get("config").get("name").asText();
                            //display.customName(name);
                        }

                        if (node.get("config").has("custom_name_visible")) {
                            display.setCustomNameVisible(node.get("config").get("name").asBoolean());
                        }
                    }

                    Quaternionf quaternion = new Quaternionf(left_rotationAsArray[0], left_rotationAsArray[1], left_rotationAsArray[2], left_rotationAsArray[3]); // fuck math
                    // TODO: NOTE Blockbench North is Minecraft's South should fix at some point ¯\_(ツ)_/¯
                    //display.teleport(origin.clone().add(posAsVector));
                    display.setTeleportDuration(50);
                    display.setPersistent(false);
                    display.setTransformation(
                            new Transformation(
                                    new Vector3f(translationAsArray[0], translationAsArray[1], translationAsArray[2]), // translation
                                    new AxisAngle4f().set(quaternion), // left rot
                                    new Vector3f(scaleAsArray[0], scaleAsArray[1], scaleAsArray[2]), // scale
                                    new AxisAngle4f() // no right rotation
                            )
                    );
                    display.setCustomName(uuid);

                    activeNodes.put(uuid, display);

                    if (debugMode())
                        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Spawning Node: " + uuid + " Of Model: " + this.uuid);


                }catch (Exception e){
                    e.printStackTrace();
                }

            });
    } // spawns model nodes

    public void deleteModelNodes(){
       for (Display node : activeNodes.values()) {
           node.remove();

       }
   }

    public void playAnimation(String name){
        if (name == null) {
            return;
        }




        if (loadedAnimations.containsKey(name)){

            OnModelStartAnimationEvent event = new OnModelStartAnimationEvent(uuid, modelType, name);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {


                //sets the animation
                JsonNode animation = loadedAnimations.get(name);
                if (animation != null) {

                    currentAnimationName = name;
                    frames = animation.get("frames");
                    loopDelay = animation.get("loop_delay").asInt();
                    maxFrameTime = animation.get("duration").asInt();

                    if (Objects.equals(animation.get("loop_mode").asText(), "loop")) {
                        loopMode = true;
                    } else {
                        loopMode = false;
                    }

                    if (debugMode())
                        getServer().getConsoleSender().sendMessage(ChatColor.RED + "Animation Info: Name:" + currentAnimationName + " LoopMode: " + loopMode + " AnimationTimeInTicks: " + animation.get("duration").asInt());
                }
            }

        }
    }

    public void tickAnimation(){
        if (currentAnimationName == null){ // TODO: stop models from ticking if they have no current animation
            if (!isActive){
                return;
            }

            isActive = false;
            resetAnimation();

            ObjectMapper objectMapper = new ObjectMapper();
            config.get("nodes").forEach(node -> {
                try {
                    Display display = activeNodes.get(node.get("uuid").asText());

                    JsonNode defaultTransform = node.get("default_transform");
                    JsonNode decomposed = defaultTransform.get("decomposed");

                    Float[] posAsArray = objectMapper.treeToValue(defaultTransform.get("pos"), Float[].class);
                    Integer[] scaleAsArray = objectMapper.treeToValue(defaultTransform.get("scale"), Integer[].class); // "scale": [1, 1, 1]
                    Float[] translationAsArray = objectMapper.treeToValue(decomposed.get("translation"), Float[].class); // "translation": [0, 0, 0]
                    Float[] left_rotationAsArray = objectMapper.treeToValue(decomposed.get("left_rotation"), Float[].class); // "left_rotation": [0, 1, 0, 0]


                    display.teleport(origin);
                    Quaternionf quaternion = new Quaternionf(left_rotationAsArray[0], left_rotationAsArray[1], left_rotationAsArray[2], left_rotationAsArray[3]);

                    display.setTransformation(
                            new Transformation(
                                    new Vector3f(translationAsArray[0], translationAsArray[1], translationAsArray[2]), // translation
                                    new AxisAngle4f().set(quaternion) , // left rot
                                    new Vector3f(scaleAsArray[0], scaleAsArray[1], scaleAsArray[2]), // scale
                                    new AxisAngle4f() // no right rotation
                            )
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        else {
            frames.forEach(data -> {


                if (currentFrameTime == (Math.round(data.get("time").asDouble() * 20))) {
                    Iterator<String> itr = data.get("node_transforms").fieldNames(); // gets list of all nodes in frame
                    JsonNode nodeTransforms = data.get("node_transforms");

                    while (itr.hasNext()) {  //to get the key fields

                        String key_field = itr.next();
                        JsonNode node = nodeTransforms.get(key_field);


                        Display display = activeNodes.get(key_field);
                        JsonNode decomposed = node.get("decomposed");

                        ObjectMapper objectMapper = new ObjectMapper();
                        try {

                            if (decomposed != null) {


                                Float[] posAsArray = objectMapper.treeToValue(node.get("pos"), Float[].class);
                                Integer[] scaleAsArray = objectMapper.treeToValue(node.get("scale"), Integer[].class); // "scale": [1, 1, 1]
                                Float[] translationAsArray = objectMapper.treeToValue(decomposed.get("translation"), Float[].class); // "translation": [0, 0, 0]
                                Float[] left_rotationAsArray = objectMapper.treeToValue(decomposed.get("left_rotation"), Float[].class); // "left_rotation": [0, 1, 0, 0]
                                Quaternionf quaternion = new Quaternionf(left_rotationAsArray[0], left_rotationAsArray[1], left_rotationAsArray[2], left_rotationAsArray[3]);



                                display.teleport(origin);
                                display.setTransformation(
                                        new Transformation(
                                                new Vector3f(translationAsArray[0], translationAsArray[1], translationAsArray[2]), // translation
                                                new AxisAngle4f(quaternion), // left rot
                                                new Vector3f(scaleAsArray[0], scaleAsArray[1], scaleAsArray[2]), // scale
                                                new AxisAngle4f() // no right rotation
                                        )
                                );

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        if (currentFrameTime >= maxFrameTime){
            if (!loopMode) {
                resetAnimation();
            }
            else{
                currentFrameTime = 0;
            }
        }

        currentFrameTime += 1;

    }

    public void setOrigin(Location location){
        location = location;
    };

    public String[] getAnimations(){
       String[] animationUuids = new String[loadedAnimations.size()];
       for (String key : loadedAnimations.keySet()) {
           Integer count = 0;
           animationUuids[count] = key;
       }
       return animationUuids;
    };

    public void resetAnimation(){

        OnModelEndAnimationEvent event = new OnModelEndAnimationEvent(uuid, modelType, currentAnimationName, loopMode);
        Bukkit.getPluginManager().callEvent(event);

        currentAnimationName = null;
        frames = null;
        currentFrameTime = 0; // in ticks 1T = 0.05S | 0.05 x 20 = 1S
        loopDelay = 0;
        maxFrameTime = 0;
        loopMode = false;


    }

}
