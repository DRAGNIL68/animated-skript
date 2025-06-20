package net.outmoded.animated_skript.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.outmoded.animated_skript.AnimatedSkript;
import net.outmoded.animated_skript.events.*;
import net.outmoded.animated_skript.models.new_stuff.Animation;
import net.outmoded.animated_skript.models.new_stuff.Frame;
import net.outmoded.animated_skript.models.new_stuff.Node;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

import static net.outmoded.animated_skript.Config.debugMode;
import static org.bukkit.Bukkit.getServer;

public class ModelClass {
    public final Map<String, UUID> activeCameras = new HashMap<>(); // stores a reference to a camera by name
    public final Map<UUID, Node> nodeMap = new HashMap<>();
    public final Map<String, Animation> animationMap = new HashMap<>();
    public final Map<UUID, Display> activeNodes = new HashMap<>();
    public boolean isPersistent = true; //

    public final String modelType;
    public ItemDisplay origin;
    public final UUID uuid;
    private float modelScale = 1;

    //current animation info
    Animation animation = null;
    Integer currentFrameTime = 0; // in ticks 1T = 0.05S | 0.05 x 20 = 1S
    boolean isActive = false;

    public ModelClass(@NotNull Location location, @NotNull String modelType, @NotNull UUID uuid) {
        this.modelType = modelType;
        this.uuid = uuid;
        origin = location.getWorld().spawn(location, ItemDisplay.class);
        origin.setPersistent(false);

        NamespacedKey UuidKey = new NamespacedKey(AnimatedSkript.getInstance(), "modelUuid");
        NamespacedKey key = new NamespacedKey(AnimatedSkript.getInstance(), "nodeType");

        origin.getPersistentDataContainer().set(UuidKey, PersistentDataType.STRING, this.uuid.toString());
        origin.getPersistentDataContainer().set(key, PersistentDataType.STRING, "origin");


        loadConfig();
    }

    public void loadConfig(){
        deleteModelNodes();
        loadJsonConfig();
        loadAnimations();
        spawnModelNodes();
    }

    public void loadJsonConfig(){
        try{
            if (ModelManager.getInstance().loadedModelExists(modelType)) {
                nodeMap.clear();
                UUID uuid = UUID.randomUUID();

                JsonNode config = ModelManager.getInstance().getLoadedModel(modelType);


                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = config.get("nodes");

                NamespacedKey key = new NamespacedKey(AnimatedSkript.getInstance(), "nodeType");
                NamespacedKey UuidKey = new NamespacedKey(AnimatedSkript.getInstance(), "modelUuid");

                for (JsonNode node : root) {
                    try {

                        Node modelNode = new Node();

                        JsonNode defaultTransform = node.get("default_transform");
                        JsonNode decomposed = defaultTransform.get("decomposed");


                        modelNode.scale = objectMapper.treeToValue(defaultTransform.get("scale"), Float[].class); // "scale": [1, 1, 1]
                        modelNode.translation = objectMapper.treeToValue(decomposed.get("translation"), Float[].class); // "translation": [0, 0, 0]
                        modelNode.leftRotation = objectMapper.treeToValue(decomposed.get("left_rotation"), Float[].class); // "left_rotation": [0, 1, 0, 0]
                        modelNode.pos = objectMapper.treeToValue(defaultTransform.get("pos"), Float[].class);

                        modelNode.uuid = UUID.fromString(node.get("uuid").asText());
                        modelNode.name = node.get("name").asText();

                                modelNode.type = node.get("type").asText();


                        if (modelNode.type.equals("block_display")) {
                            modelNode.typeSpecificProperties.put("block", node.get("block").asText().replace("minecraft:", "").toUpperCase());

                        } else if (modelNode.type.equals("item_display")) {
                            modelNode.typeSpecificProperties.put("item", node.get("item").asText().replace("minecraft:", "").toUpperCase());

                        } else if (modelNode.type.equals("text_display")) {
                            modelNode.typeSpecificProperties.put("text", node.get("text").asText());
                            modelNode.typeSpecificProperties.put("align", node.get("align").asText().toUpperCase());
                            modelNode.typeSpecificProperties.put("shadow", node.get("shadow").asBoolean());
                            modelNode.typeSpecificProperties.put("see_through", node.get("see_through").asBoolean());

                            if (node.get("config").has("billboard")) {
                                modelNode.typeSpecificProperties.put("billboard", node.get("config").get("billboard").asText().toUpperCase());


                            }
                        }
                        else if (modelNode.type.equals("camera")) {
                                // do nothing


                        }

                        if (node.has("config")) {

                            if (node.get("config").has("override_brightness")) {
                                boolean overrideBrightness = node.get("config").get("override_brightness").asBoolean();

                                if (overrideBrightness) {

                                    if (node.get("config").has("brightness_override")) {
                                        modelNode.typeSpecificProperties.put("brightness_override", node.get("config").get("brightness_override").asInt());
                                    }
                                }
                            }

                            if (node.get("config").has("shadow_radius")) {
                                modelNode.typeSpecificProperties.put("shadow_radios", node.get("config").get("shadow_radius").asInt());
                            }

                            if (node.get("config").has("name")) {

                                if (node.get("config").has("custom_name_visible")) {
                                    modelNode.typeSpecificProperties.put("custom_name_visible", node.get("config").get("custom_name_visible").asBoolean());
                                }
                                if (node.get("config").has("custom_name")) {
                                    modelNode.typeSpecificProperties.put("custom_name", node.get("config").get("name").asText());
                                }

                            }

                            if (node.get("config").has("glowing")) {
                                modelNode.typeSpecificProperties.put("glowing", node.get("config").get("glowing").asBoolean());
                            }

                            if (node.get("config").has("glow_color")) {
                                modelNode.typeSpecificProperties.put("glow_color", node.get("config").get("glow_color").asText());
                            }

                            if (node.get("config").has("invisible")) {
                                modelNode.typeSpecificProperties.put("invisible", node.get("config").get("invisible").asBoolean());

                            }

                        }

                        Quaternionf quaternion = new Quaternionf(modelNode.leftRotation[0], modelNode.leftRotation[1], modelNode.leftRotation[2], modelNode.leftRotation[3]); // fuck math
                        // TODO: NOTE Blockbench's North is Minecraft's South, should fix at some point ¯\_(ツ)_/¯
                        // TODO: NOTE Mr Aj has made custom textured models actually face north for some reason block displays still face south

                        if (modelNode.type.equals("bone")){
                            AxisAngle4f additionalRotation = new AxisAngle4f((float) Math.toRadians(180), 0, 1, 0); // 90-degree Y-axis rotation
                            quaternion.mul(new Quaternionf(additionalRotation));
                        }

                        if (modelNode.type.equals("struct") && debugMode()) {

                            Transformation transformation = new Transformation(
                                    new Vector3f(modelNode.translation[0], modelNode.translation[1], modelNode.translation[2]), // translation
                                    new AxisAngle4f().set(quaternion), // left rot
                                    new Vector3f(0.2F, 0.2F, 0.2F), // scale
                                    new AxisAngle4f() // no right rotation
                            );

                            modelNode.transformation = transformation;
                        }
                        else {



                            Transformation transformation = new Transformation(
                                    new Vector3f(modelNode.translation[0], modelNode.translation[1], modelNode.translation[2]), // translation
                                    new AxisAngle4f().set(quaternion), // left rot
                                    new Vector3f(modelNode.scale[0], modelNode.scale[1], modelNode.scale[2]), // scale
                                    new AxisAngle4f() // no right rotation
                            );

                            modelNode.transformation = transformation;
                        }

                        this.nodeMap.put(modelNode.uuid, modelNode);

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            else {
                ModelManager.getInstance().removeActiveModel(uuid);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setPersistence(Boolean persistence) {
        isPersistent = persistence;
    }

    public Boolean getPersistence() {
        return isPersistent;
    }

    public void loadAnimations(){
        try{
            animationMap.clear();
            if (ModelManager.getInstance().loadedModelExists(modelType)) {

                JsonNode config = ModelManager.getInstance().getLoadedModel(modelType);


                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = config.get("animations");

                for (Iterator<String> it = root.fieldNames(); it.hasNext(); ) {
                    String animationName = it.next();
                    JsonNode animation = root.get(animationName);
                    try {
                        Animation modelAnimation = new Animation();

                        modelAnimation.uuid = UUID.fromString(animationName);

                        modelAnimation.name = animation.get("name").asText();
                        modelAnimation.duration = animation.get("duration").asInt()-1;
                        modelAnimation.loopDelay = animation.get("loop_delay").asInt();
                        modelAnimation.loopMode = animation.get("loop_mode").asText();

                        JsonNode framesRoot = animation.get("frames");

                        for (JsonNode frame : framesRoot) {

                            Frame animationFrame = new Frame();
                            ArrayList<Node> nodes = new ArrayList<Node>();

                            double doubleTime = frame.get("time").asDouble();
                            Integer time = (int) (doubleTime * 20);
                            JsonNode nodeTransforms = frame.get("node_transforms");
                            for (Iterator<String> iter = nodeTransforms.fieldNames(); iter.hasNext(); ) {
                                String nodeTransformUuid = iter.next();
                                JsonNode nodeTransform = nodeTransforms.get(nodeTransformUuid);


                                Node frameNode = new Node();
                                frameNode.uuid = UUID.fromString(nodeTransformUuid);
                                JsonNode decomposed = nodeTransform.get("decomposed");

                                frameNode.scale = objectMapper.treeToValue(nodeTransform.get("scale"), Float[].class); // "scale": [1, 1, 1]
                                frameNode.translation = objectMapper.treeToValue(decomposed.get("translation"), Float[].class); // "translation": [0, 0, 0]
                                frameNode.leftRotation = objectMapper.treeToValue(decomposed.get("left_rotation"), Float[].class); // "left_rotation": [0, 1, 0, 0]
                                frameNode.pos = objectMapper.treeToValue(nodeTransform.get("pos"), Float[].class);

                                Quaternionf quaternion = new Quaternionf(frameNode.leftRotation[0], frameNode.leftRotation[1], frameNode.leftRotation[2], frameNode.leftRotation[3]); // fuck math




                                if (nodeMap.get(UUID.fromString(nodeTransformUuid)).type.equals("struct") && debugMode()) {

                                    AxisAngle4f additionalRotation = new AxisAngle4f((float) Math.toRadians(180), 0, 1, 0); // 90-degree Y-axis rotation
                                    quaternion.mul(new Quaternionf(additionalRotation));

                                    Transformation transformation = new Transformation(
                                            new Vector3f(frameNode.translation[0], frameNode.translation[1], frameNode.translation[2]), // translation
                                            new AxisAngle4f().set(quaternion), // left rot
                                            new Vector3f(0.2F, 0.2F, 0.2F), // scale
                                            new AxisAngle4f() // no right rotation
                                    );

                                    frameNode.transformation = transformation;
                                }
                                else {

                                    if (nodeMap.get(UUID.fromString(nodeTransformUuid)).type.equals("bone")){
                                        AxisAngle4f additionalRotation = new AxisAngle4f((float) Math.toRadians(180), 0, 1, 0); // 90-degree Y-axis rotation
                                        quaternion.mul(new Quaternionf(additionalRotation));
                                    }

                                    Transformation transformation = new Transformation(
                                            new Vector3f(frameNode.translation[0], frameNode.translation[1], frameNode.translation[2]), // translation
                                            new AxisAngle4f().set(quaternion), // left rot
                                            new Vector3f(frameNode.scale[0], frameNode.scale[1], frameNode.scale[2]), // scale
                                            new AxisAngle4f() // no right rotation
                                    );



                                    frameNode.transformation = transformation;
                                }
                                nodes.add(frameNode);

                            }

                            animationFrame.nodeTransforms = nodes;
                            modelAnimation.frames.put(time, animationFrame);
                        }

                        if (!animationMap.containsKey(modelAnimation.name)){
                            animationMap.put(modelAnimation.name, modelAnimation);
                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ApiStatus.Internal
    public void spawnModelNodes(){

        deleteModelNodes();
        activeCameras.clear();
        activeNodes.clear();

        NamespacedKey key = new NamespacedKey(AnimatedSkript.getInstance(), "nodeType");
        NamespacedKey UuidKey = new NamespacedKey(AnimatedSkript.getInstance(), "modelUuid");

        for (Node node : nodeMap.values()){

            Display display = null;
            switch (node.type) {

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

                    ItemStack boneItemStack = new ItemStack(Material.PAPER);


                    NamespacedKey modelKey = new NamespacedKey("animated-skript", modelType + "/" + "default" + "/" + node.uuid);
                    ItemMeta meta = boneItemStack.getItemMeta();
                    meta.setItemModel(modelKey);
                    boneItemStack.setItemMeta(meta);

                    ItemDisplay boneItemDisplay = origin.getWorld().spawn(origin.getLocation(), ItemDisplay.class);

                    boneItemDisplay.setItemStack(boneItemStack);
                    boneItemDisplay.getPersistentDataContainer().set(key, PersistentDataType.STRING, "bone");

                    display = boneItemDisplay;


                    break;

                case "block_display":

                    String material = (String) node.typeSpecificProperties.get("block");
                    Material blockDisplayMaterial = Material.valueOf(material.toUpperCase());
                    BlockDisplay blockDisplay = origin.getWorld().spawn(origin.getLocation(), BlockDisplay.class);

                    blockDisplay.getPersistentDataContainer().set(key, PersistentDataType.STRING, "block_display");

                    blockDisplay.setBlock(blockDisplayMaterial.createBlockData());
                    display = blockDisplay;

                    break;
                case "item_display":

                    String material1 = (String) node.typeSpecificProperties.get("item");
                    Material itemDisplayMaterial = Material.valueOf(material1.toUpperCase());
                    ItemDisplay itemDisplayItemDisplay = origin.getWorld().spawn(origin.getLocation(), ItemDisplay.class);

                    itemDisplayItemDisplay.getPersistentDataContainer().set(key, PersistentDataType.STRING, "item_display");

                    itemDisplayItemDisplay.setItemStack(new ItemStack(itemDisplayMaterial));
                    display = itemDisplayItemDisplay;

                    break;
                case "text_display":

                    String text = node.typeSpecificProperties.get("text").toString();
                    String align = node.typeSpecificProperties.get("align").toString();
                    boolean shadow = (boolean) node.typeSpecificProperties.get("shadow");
                    boolean seeThrough = (boolean) node.typeSpecificProperties.get("see_through");

                    TextDisplay textDisplay = origin.getWorld().spawn(origin.getLocation(), TextDisplay.class);

                    textDisplay.getPersistentDataContainer().set(key, PersistentDataType.STRING, "text_display");

                    textDisplay.setText(text);
                    textDisplay.setText(text);
                    textDisplay.setShadowed(shadow);
                    textDisplay.setSeeThrough(seeThrough);
                    textDisplay.setAlignment(TextDisplay.TextAlignment.valueOf(align));

                    if (node.typeSpecificProperties.containsKey("billboard")) {
                        textDisplay.setBillboard(Display.Billboard.valueOf((String) node.typeSpecificProperties.get("billboard")));

                    }

                    display = textDisplay;

                    break;
                case "camera":

                    ItemDisplay itemDisplay = origin.getWorld().spawn(origin.getLocation(), ItemDisplay.class);
                    itemDisplay.setItemStack(new ItemStack(Material.WITHER_SKELETON_SKULL));

                    itemDisplay.getPersistentDataContainer().set(key, PersistentDataType.STRING, "camera");

                    display = itemDisplay;

                    Display.Brightness brightness = new Display.Brightness(15, 15);
                    display.setBrightness(brightness);

                    activeCameras.put(node.name, node.uuid);

                    break;

                default:
                    throw new RuntimeException("Corrupted node in json file: " + modelType + ".json node: " + node.uuid);
            }


            display.setPersistent(false);
            activeNodes.put(node.uuid, display);
            //activeNodes.get(node.uuid).setTransformation(applyScale(node.transformation, modelScale));
            display.setTransformation(applyScale(node.transformation, modelScale));


        }
    }

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




        if (animationMap.containsKey(name)){

            OnModelStartAnimationEvent event = new OnModelStartAnimationEvent(uuid, modelType, name);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {


                resetAnimation();
                isActive = true;
                //sets the animation
                this.animation = animationMap.get(name);
                currentFrameTime = 0;

                if (debugMode())
                    getServer().getConsoleSender().sendMessage(ChatColor.RED + "Animation Info: Name:" + name + " LoopMode: " + this.animation.loopMode + " AnimationTimeInTicks: " + this.animation.duration);

            }

        }
        else {
            resetAnimation();
            this.animation = null;
        }
    }

    public void tickAnimation(){

        if (!isActive){
            return;
        }

        if (animation == null){ // TODO: stop models from ticking if they have no current animation

            resetAnimation();
        }

        else {
            if (animation.frames.containsKey(currentFrameTime)){ // should stop corrupted nodes form causing problems

                for (Node node : animation.frames.get(currentFrameTime).nodeTransforms){

                    if (activeNodes.containsKey(node.uuid)){
                        activeNodes.get(node.uuid).setTransformation(applyScale(node.transformation, modelScale));
                    }


                }
            }


        }

        if (animation != null){
            if (currentFrameTime >= animation.duration) {
                if (animation.loopMode.equals("loop")) {
                    currentFrameTime = 0;
                }
                else if (animation.loopMode.equals("hold")){
                    pauseCurrentAnimation(true);

                } else {
                    resetAnimation();

                }
            }

            currentFrameTime += 1;
        }





    }

    public void teleport(Location location){
        origin.teleport(location);
        for (Display node: activeNodes.values()){
            node.teleport(location);


        }
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

    public String getCurrentAnimationName(){
        if (animation == null)
            return "none";

        return animation.name;
    };

    public Animation getCurrentAnimation(){ // not a copy so people can mess with models to tether hearts content
        return animation;
    };

    public boolean hasCurrentAnimation(){
        return animation != null;
    };

    public String[] getAnimations(){
        return animationMap.keySet().toArray(String[]::new);
    };

    public void resetAnimation(){


        OnModelEndAnimationEvent event;
        if (animation == null){
            event = new OnModelEndAnimationEvent(uuid, modelType, "none", "none");

        }else{
            event = new OnModelEndAnimationEvent(uuid, modelType, animation.name, animation.loopMode);


            this.animation = null;
        }

        for (Node node : nodeMap.values()){
            activeNodes.get(node.uuid).setTransformation(applyScale(node.transformation, modelScale));

        }

        this.isActive = false;
        Bukkit.getPluginManager().callEvent(event);


    };

    public void pauseCurrentAnimation(Boolean bool){
        if (animation == null)
            return;

        if (!bool){
            OnModelPauseAnimationEvent event = new OnModelPauseAnimationEvent(uuid, modelType, animation.name, animation.loopMode);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                isActive = !bool;

            }

        }
        else{

            OnModelUnpauseAnimationEvent event = new OnModelUnpauseAnimationEvent(uuid, modelType, animation.name, animation.loopMode);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                isActive = !bool;

            }
        }
    };

    public Boolean isCurrentAnimationPaused(){
        return isActive;
    }

    public void setCurrentAnimationFrame(int ticks){
        if (animation == null)
            return;


        if (ticks > animation.duration){
            ticks = animation.duration;
        }

        OnModelFrameSetAnimationEvent event = new OnModelFrameSetAnimationEvent(uuid, modelType, animation.name, animation.loopMode, currentFrameTime, ticks, animation.duration );
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            currentFrameTime = ticks;

        }
    }

    public Integer getCurrentAnimationsFrame(){
        if (animation == null)
            return -1;


        return currentFrameTime;
    };

    public int getCurrentAnimationMaxFrame(){ // returns max frame time
        if (animation == null)
            return -1;


        return animation.duration;
    }

    public boolean hasAnimation(String key){
        return animationMap.containsKey(key);
    }

    public String getActiveVariant(){
        return "activeVariant";
    };

    public void setVariant(String variantKey) {
        //if (loadedVariants.containsKey(variantKey)){
            //activeVariant = variantKey;
        //}
    }

    //public String[] getAllVariants() {
        //return loadedVariants.keySet().toArray(String[]::new);
    //}

    //public boolean hasVariant(String variantKey) {
       // return loadedVariants.containsKey(variantKey);
    //}

   public UUID[] getAllNodes() {
        return activeNodes.keySet().toArray(UUID[]::new);
    }

    public Display getDisplayNode(UUID uuid){
        if (activeNodes.containsKey(uuid)){
            return activeNodes.get(uuid);
        }
        return null;
    }

    public Boolean hasDisplayNode(UUID uuid){
        return activeNodes.containsKey(uuid);
    }

    public Node getNode(UUID uuid){
        if (nodeMap.containsKey(uuid)){
            return nodeMap.get(uuid);
        }
        return null;
    }

    public Boolean hasNode(UUID uuid){
        return nodeMap.containsKey(uuid);
    }

    public Display getActiveCamera(String name){
        if (activeCameras.containsKey(name))
            return activeNodes.get(activeCameras.get(name));
        return null;

    }
    public boolean hasActiveCamera(String name){
        return activeCameras.containsKey(name);
    }

    public UUID getUuidFromActiveCamera(String name){
        if (activeCameras.containsKey(name)){
            return activeCameras.get(name);
        }
        return null;
    }

    public boolean spectateNode(Player player, UUID uuid){
        if (activeNodes.containsKey(uuid)){

            NamespacedKey namespacedKey = new NamespacedKey(AnimatedSkript.getInstance(), "isSpectating");
            player.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, player.getGameMode().toString());
            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(activeNodes.get(uuid));
            return true;

        }
        return false;
    }




    public void setDefaultVisibility(boolean visibility){ // sets default visibility for the entire model
        origin.setVisibleByDefault(visibility);
        for (Display display : activeNodes.values()){
            display.setVisibleByDefault(visibility);
        }

    }
    public void setVisibilityForPlayer(Player player, boolean visibility){ // sets visibility for the entire model for a player
        player.showEntity(AnimatedSkript.getInstance(), origin);
        for (Display display : activeNodes.values()){
            player.showEntity(AnimatedSkript.getInstance(), display);
        }

        Location location = player.getLocation(); // or any Location
        World world = location.getWorld();


    }

    public void setScale(float scale){
        modelScale = scale;
        isActive = true; // should tick once to update

    }

    // ################# internal stuff
    private Transformation applyScale(Transformation originalTransformation, float modelScale){ // should probably be in a util class

        Vector3f scale = new Vector3f(originalTransformation.getScale());
        Vector3f originalTranslation = new Vector3f(originalTransformation.getTranslation());

        Transformation transformation = new Transformation(

                originalTranslation.mul(modelScale), // translation
                new AxisAngle4f(originalTransformation.getLeftRotation()), // left rot
                scale.mul(modelScale), // scale
                new AxisAngle4f() // no right rotation
        );

        return transformation;
    }
}
