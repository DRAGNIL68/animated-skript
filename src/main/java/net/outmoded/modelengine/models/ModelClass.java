package net.outmoded.modelengine.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.kyori.adventure.text.format.TextColor;
import net.outmoded.modelengine.Config;
import net.outmoded.modelengine.ModelEngine;
import net.outmoded.modelengine.events.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.ApiStatus;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;


import java.io.DataInput;
import java.util.*;


import static net.outmoded.modelengine.Config.debugMode;
import static org.bukkit.Bukkit.getServer;

public class ModelClass { // TODO: destroy this shit code
    private final Map<String, Display> activeNodes = new HashMap<>();
    private final Map<String, JsonNode> loadedAnimations = new HashMap<>();
    private final Map<String, String> loadedVariants = new HashMap<>();

    private final String modelType;
    private final ItemDisplay origin;
    private JsonNode config;
    private final UUID uuid;
    private String activeVariant = "default";


    //current animation info
    private String currentAnimationName = null;
    private JsonNode frames = null;
    private Integer currentFrameTime = 0; // in ticks 1T = 0.05S | 0.05 x 20 = 1S
    private Integer maxFrameTime = 0;
    private Integer loopDelay = 0; // in ticks does not need converting
    private Boolean isActive = false;
    private Boolean loopMode = false;

    ModelClass(Location location, String modelType, UUID uuid) {
        this.modelType = modelType;
        this.uuid = uuid;
        origin = location.getWorld().spawn(location, ItemDisplay.class);
        origin.setPersistent(false);
        config = ModelManager.getLoadedModel(modelType);
        loadAnimations();
        spawnModelNodes();
    }


    public void loadVariants(){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            resetAnimation();
            JsonNode root = config;

            JsonNode variants = root.path("variants");

            variants.forEach(variant -> { // WARNING: will override existing variant if it somehow has the same name
                String name = variant.get("name").asText(); // name of animation
                loadedVariants.put(name, variant.get("uuid").asText());

            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadAnimations(){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            resetAnimation();
            JsonNode root = config;
            loadedAnimations.clear();
            JsonNode nodes = root.path("animations");

            for (JsonNode loopedAnimation : nodes) {
                String name = loopedAnimation.get("name").asText(); // name of animation
                loadedAnimations.put(name, loopedAnimation);

            }
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

        NamespacedKey key = new NamespacedKey(ModelEngine.getInstance(), "nodeType");

        for (JsonNode node : root) {
            try {

                JsonNode defaultTransform = node.get("default_transform");
                JsonNode decomposed = defaultTransform.get("decomposed");

                
                
                Float[] scaleAsArray = objectMapper.treeToValue(defaultTransform.get("scale"), Float[].class); // "scale": [1, 1, 1]
                Float[] translationAsArray = objectMapper.treeToValue(decomposed.get("translation"), Float[].class); // "translation": [0, 0, 0]
                Float[] left_rotationAsArray = objectMapper.treeToValue(decomposed.get("left_rotation"), Float[].class); // "left_rotation": [0, 1, 0, 0]


                String uuid = node.get("uuid").asText();

                Display display = null;


                String nodeType = node.get("type").asText();

                switch (nodeType) {
                    case "struct":

                        ItemStack structItemStack = new ItemStack(Material.SKELETON_SKULL);
                        ItemDisplay structItemDisplay = origin.getWorld().spawn(origin.getLocation(), ItemDisplay.class);
                        structItemDisplay.setVisibleByDefault(false);
                        structItemDisplay.setItemStack(structItemStack);


                        structItemDisplay.getPersistentDataContainer().set(key, PersistentDataType.STRING, "struct");

                        if (debugMode()) {
                            structItemDisplay.setGlowing(true);
                            structItemDisplay.setVisibleByDefault(true);

                        }


                        display = structItemDisplay;

                        break;
                    case "bone":

                        ItemStack boneItemStack = new ItemStack(Material.SKELETON_SKULL);


                        NamespacedKey modelKey = new NamespacedKey("animated-skript", modelType + "/" + activeVariant + "/" + uuid);
                        ItemMeta meta = boneItemStack.getItemMeta();
                        meta.setItemModel(modelKey);
                        boneItemStack.setItemMeta(meta);

                        ItemDisplay boneItemDisplay = origin.getWorld().spawn(origin.getLocation(), ItemDisplay.class);

                        boneItemDisplay.setItemStack(boneItemStack);
                        boneItemDisplay.getPersistentDataContainer().set(key, PersistentDataType.STRING, "bone");

                        display = boneItemDisplay;

                        break;

                    case "block_display":


                        Material blockDisplayMaterial = Material.valueOf(node.get("block").asText().replace("minecraft:", "").toUpperCase());
                        BlockDisplay blockDisplay = origin.getWorld().spawn(origin.getLocation(), BlockDisplay.class);

                        blockDisplay.getPersistentDataContainer().set(key, PersistentDataType.STRING, "block_display");

                        blockDisplay.setBlock(blockDisplayMaterial.createBlockData());
                        display = blockDisplay;

                        break;
                    case "item_display":


                        Material itemDisplayMaterial = Material.valueOf(node.get("item").asText().replace("minecraft:", "").toUpperCase());
                        ItemDisplay itemDisplayItemDisplay = origin.getWorld().spawn(origin.getLocation(), ItemDisplay.class);

                        itemDisplayItemDisplay.getPersistentDataContainer().set(key, PersistentDataType.STRING, "item_display");

                        itemDisplayItemDisplay.setItemStack(new ItemStack(itemDisplayMaterial));
                        display = itemDisplayItemDisplay;

                        break;
                    case "text_display":

                        String text = node.get("text").asText();
                        String align = node.get("align").asText().toUpperCase();
                        boolean shadow = node.get("shadow").asBoolean();
                        boolean seeThrough = node.get("see_through").asBoolean();

                        TextDisplay textDisplay = origin.getWorld().spawn(origin.getLocation(), TextDisplay.class);

                        textDisplay.getPersistentDataContainer().set(key, PersistentDataType.STRING, "text_display");

                        textDisplay.setText(text);
                        textDisplay.setText(text);
                        textDisplay.setShadowed(shadow);
                        textDisplay.setSeeThrough(seeThrough);
                        textDisplay.setAlignment(TextDisplay.TextAlignment.valueOf(align));

                        if (node.get("config").has("billboard")) {
                            textDisplay.setBillboard(Display.Billboard.valueOf(node.get("config").get("billboard").asText().toUpperCase()));

                        }

                        display = textDisplay;

                        break;
                    case "camera":

                        ItemDisplay itemDisplay = origin.getWorld().spawn(origin.getLocation(), ItemDisplay.class);
                        itemDisplay.setItemStack(new ItemStack(Material.WITHER_SKELETON_SKULL));

                        itemDisplay.getPersistentDataContainer().set(key, PersistentDataType.STRING, "camera");

                        display = itemDisplay;

                        Display.Brightness brightness = new Display.Brightness(100, 100);
                        display.setBrightness(brightness);

                        break;

                    default:
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

                        if (node.get("config").has("custom_name_visible")) {
                            display.setCustomNameVisible(node.get("config").get("custom_name").asBoolean());
                        }
                        if (node.get("config").has("custom_name")) {

                            String name = node.get("config").get("name").asText();
                            display.setCustomName(name);
                        }

                    }

                    if (node.get("config").has("glowing")) {
                        display.setGlowing(node.get("config").get("glowing").asBoolean());
                    }

                    if (node.get("config").has("glow_color")) {
                        String hexCode = node.get("config").get("glow_color").asText();

                        TextColor glowColor = TextColor.fromHexString(hexCode);

                        //display.setGlowColorOverride();
                    }

                    if (node.get("config").has("invisible")) {
                        Boolean isInvisible = node.get("config").get("invisible").asBoolean();

                        if (!nodeType.equals("camera")) {
                            display.setVisibleByDefault(isInvisible);
                        }

                    }

                }


                display.setInterpolationDelay(-1);
                display.setInterpolationDuration(1);

                Quaternionf quaternion = new Quaternionf(left_rotationAsArray[0], left_rotationAsArray[1], left_rotationAsArray[2], left_rotationAsArray[3]); // fuck math
                // TODO: NOTE Blockbench's North is Minecraft's South, should fix at some point ¯\_(ツ)_/¯
                // TODO: NOTE Mr Aj has made custom textured models actually face north for some reason block displays still face south
                display.setPersistent(false);




                if (nodeType.equals("bone")) {
                    AxisAngle4f additionalRotation = new AxisAngle4f((float) Math.toRadians(180), 0, 1, 0); // 90-degree Y-axis rotation
                    quaternion.mul(new Quaternionf(additionalRotation));

                    display.setTransformation(
                            new Transformation(
                                    new Vector3f(translationAsArray[0], translationAsArray[1], translationAsArray[2]), // translation
                                    new AxisAngle4f().set(quaternion), // left rot
                                    new Vector3f(scaleAsArray[0], scaleAsArray[1], scaleAsArray[2]), // scale
                                    new AxisAngle4f() // no right rotation
                            )
                    );
                }
                else if (nodeType.equals("struct") && debugMode()) {

                    display.setTransformation(
                            new Transformation(
                                    new Vector3f(translationAsArray[0], translationAsArray[1], translationAsArray[2]), // translation
                                    new AxisAngle4f().set(quaternion), // left rot
                                    new Vector3f(0.2F, 0.2F, 0.2F), // scale
                                    new AxisAngle4f() // no right rotation
                            )
                    );
                }
                else {

                    display.setTransformation(
                            new Transformation(
                                    new Vector3f(translationAsArray[0], translationAsArray[1], translationAsArray[2]), // translation
                                    new AxisAngle4f().set(quaternion), // left rot
                                    new Vector3f(scaleAsArray[0], scaleAsArray[1], scaleAsArray[2]), // scale
                                    new AxisAngle4f() // no right rotation
                            )
                    );
                }


                display.setCustomName(uuid);

                activeNodes.put(uuid, display);

                origin.addPassenger(display);
                if (debugMode())
                    getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Spawning Node: " + uuid + " Of Model: " + this.uuid);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    } // spawns model nodes


    @ApiStatus.Internal
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

                    resetAnimation();

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

                    NamespacedKey key = new NamespacedKey(ModelEngine.getInstance(), "nodeType");
                    PersistentDataContainer container = display.getPersistentDataContainer();
                    String nodeType = container.get(key, PersistentDataType.STRING);


                    JsonNode defaultTransform = node.get("default_transform");
                    JsonNode decomposed = defaultTransform.get("decomposed");

                    Float[] posAsArray = objectMapper.treeToValue(defaultTransform.get("pos"), Float[].class);
                    Float[] scaleAsArray = objectMapper.treeToValue(defaultTransform.get("scale"), Float[].class); // "scale": [1, 1, 1]
                    Float[] translationAsArray = objectMapper.treeToValue(decomposed.get("translation"), Float[].class); // "translation": [0, 0, 0]
                    Float[] left_rotationAsArray = objectMapper.treeToValue(decomposed.get("left_rotation"), Float[].class); // "left_rotation": [0, 1, 0, 0]

                    Quaternionf quaternion = new Quaternionf(left_rotationAsArray[0], left_rotationAsArray[1], left_rotationAsArray[2], left_rotationAsArray[3]);




                    if (nodeType.equals("struct") && debugMode()) {
                        new Transformation(
                                new Vector3f(translationAsArray[0], translationAsArray[1], translationAsArray[2]), // translation
                                new AxisAngle4f().set(quaternion) , // left rot
                                new Vector3f(0.2F, 0.2F, 0.2F), // scale
                                new AxisAngle4f() // no right rotation
                        );
                    }

                    else if (nodeType.equals("bone")) {
                        AxisAngle4f additionalRotation = new AxisAngle4f((float) Math.toRadians(180), 0, 1, 0); // 90-degree Y-axis rotation
                        quaternion.mul(new Quaternionf(additionalRotation));

                        display.setTransformation(
                                new Transformation(
                                        new Vector3f(translationAsArray[0], translationAsArray[1], translationAsArray[2]), // translation
                                        new AxisAngle4f().set(quaternion), // left rot
                                        new Vector3f(scaleAsArray[0], scaleAsArray[1], scaleAsArray[2]), // scale
                                        new AxisAngle4f() // no right rotation
                                )
                        );
                    }

                    else{

                        display.setTransformation(
                                new Transformation(
                                        new Vector3f(translationAsArray[0], translationAsArray[1], translationAsArray[2]), // translation
                                        new AxisAngle4f().set(quaternion) , // left rot
                                        new Vector3f(scaleAsArray[0], scaleAsArray[1], scaleAsArray[2]), // scale
                                        new AxisAngle4f() // no right rotation
                                )
                        );

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        else {
            for (JsonNode data : frames) {
                if (currentFrameTime == (Math.round(data.get("time").asDouble() * 20))) {
                    Iterator<String> itr = data.get("node_transforms").fieldNames(); // gets list of all nodes in frame
                    JsonNode nodeTransforms = data.get("node_transforms");

                    while (itr.hasNext()) {  //to get the key fields

                        String key_field = itr.next();
                        JsonNode node = nodeTransforms.get(key_field);


                        Display display = activeNodes.get(key_field);
                        JsonNode decomposed = node.get("decomposed");

                        NamespacedKey key = new NamespacedKey(ModelEngine.getInstance(), "nodeType");
                        PersistentDataContainer container = display.getPersistentDataContainer();
                        String nodeType = container.get(key, PersistentDataType.STRING);

                        ObjectMapper objectMapper = new ObjectMapper();
                        try {

                            if (decomposed != null) {


                                Float[] posAsArray = objectMapper.treeToValue(node.get("pos"), Float[].class);
                                Float[] scaleAsArray = objectMapper.treeToValue(node.get("scale"), Float[].class); // "scale": [1, 1, 1]
                                Float[] translationAsArray = objectMapper.treeToValue(decomposed.get("translation"), Float[].class); // "translation": [0, 0, 0]
                                Float[] left_rotationAsArray = objectMapper.treeToValue(decomposed.get("left_rotation"), Float[].class); // "left_rotation": [0, 1, 0, 0]
                                Quaternionf quaternion = new Quaternionf(left_rotationAsArray[0], left_rotationAsArray[1], left_rotationAsArray[2], left_rotationAsArray[3]);


                                if (nodeType.equals("struct") && debugMode()) {
                                    new Transformation(
                                            new Vector3f(translationAsArray[0], translationAsArray[1], translationAsArray[2]), // translation
                                            new AxisAngle4f().set(quaternion), // left rot
                                            new Vector3f(0.2F, 0.2F, 0.2F), // scale
                                            new AxisAngle4f() // no right rotation
                                    );
                                } else if (nodeType.equals("bone")) {
                                    AxisAngle4f additionalRotation = new AxisAngle4f((float) Math.toRadians(180), 0, 1, 0); // 90-degree Y-axis rotation
                                    quaternion.mul(new Quaternionf(additionalRotation));

                                    display.setTransformation(
                                            new Transformation(
                                                    new Vector3f(translationAsArray[0], translationAsArray[1], translationAsArray[2]), // translation
                                                    new AxisAngle4f().set(quaternion), // left rot
                                                    new Vector3f(scaleAsArray[0], scaleAsArray[1], scaleAsArray[2]), // scale
                                                    new AxisAngle4f() // no right rotation
                                            )
                                    );
                                } else {

                                    display.setTransformation(
                                            new Transformation(
                                                    new Vector3f(translationAsArray[0], translationAsArray[1], translationAsArray[2]), // translation
                                                    new AxisAngle4f().set(quaternion), // left rot
                                                    new Vector3f(scaleAsArray[0], scaleAsArray[1], scaleAsArray[2]), // scale
                                                    new AxisAngle4f() // no right rotation
                                            )
                                    );

                                }

                            }
                            getOrigin();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
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

    public void setOriginLocation(Location location){
        origin.teleport(location);
    };

    public Location getOriginLocation(){
        return origin.getLocation();
    };

    public ItemDisplay getOrigin(){
        return origin;
    };

    public String getModelType(){
        return modelType;
    };

    public UUID getUuid(){
        return uuid;
    };

    public String getCurrentAnimation(){
        return currentAnimationName;
    };

    public String[] getAnimations(){
       String[] animationNames = new String[loadedAnimations.size()];
        Integer count = 0;
       for (String key : loadedAnimations.keySet()) {
           animationNames[count] = key;
           count++;
       }
       return animationNames;
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


    };

    public void pauseCurrentAnimation(Boolean bool){


        if (!bool){
            OnModelPauseAnimationEvent event = new OnModelPauseAnimationEvent(uuid, modelType, currentAnimationName, loopMode);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                isActive = !bool;

            }

        }
        else{

            OnModelUnpauseAnimationEvent event = new OnModelUnpauseAnimationEvent(uuid, modelType, currentAnimationName, loopMode);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                isActive = !bool;

            }
        }
    };

    public Boolean getIsCurrentAnimationPaused(){
        return isActive;
    }

    public void setCurrentAnimationFrame(int ticks){


        if (ticks > maxFrameTime){
            ticks = maxFrameTime;
        }

        OnModelFrameSetAnimationEvent event = new OnModelFrameSetAnimationEvent(uuid, modelType, currentAnimationName, loopMode, currentFrameTime, ticks, maxFrameTime );
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            currentFrameTime = ticks;

        }
    }

    public int getCurrentAnimationLength(){ // returns max frame time
        return maxFrameTime;
    }


    public boolean hasAnimation(String key){
        return loadedAnimations.containsKey(key);
    }


    public String getActiveVariant(){
        return activeVariant;
    };



    public void setVariant(String variantKey) {
        if (loadedVariants.containsKey(variantKey)){
            activeVariant = variantKey;
        }
    }

    public String[] getAllVariants() {
        String[] variants = new String[loadedVariants.size()];
        int count = 0;
        for (String key : loadedAnimations.keySet()) {
            variants[count] = key;
            count++;
        }
        return variants;
    }

    public boolean hasVariant(String variantKey) {
        return loadedVariants.containsKey(variantKey);
    }


    public String[] getAllNodes() {
        String[] variants = new String[activeNodes.size()];
        int count = 0;
        for (String key : activeNodes.keySet()) {
            variants[count] = key;
            count++;
        }
        return variants;
    }

    public Display getNode(String uuid){
        if (activeNodes.containsKey(uuid)){
            return activeNodes.get(uuid);
        }
        return null;
    }

    public Boolean hasNode(String uuid){
        return activeNodes.containsKey(uuid);
    }

}
