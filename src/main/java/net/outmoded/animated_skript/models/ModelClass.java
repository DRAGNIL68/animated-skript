package net.outmoded.animated_skript.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import net.outmoded.animated_skript.AnimatedSkript;
import net.outmoded.animated_skript.events.*;
import net.outmoded.animated_skript.models.new_stuff.Animation;
import net.outmoded.animated_skript.models.new_stuff.Frame;
import net.outmoded.animated_skript.models.new_stuff.Node;
import net.outmoded.animated_skript.models.new_stuff.Variant;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.outmoded.animated_skript.Config.debugMode;
import static org.bukkit.Bukkit.getServer;

public class ModelClass {
    public final Map<String, Variant> variants = new HashMap<>();
    public final Map<String, UUID> activeCameras = new HashMap<>(); // stores a reference to a camera by name
    public final Map<UUID, Node> nodeMap = new HashMap<>();
    public final Map<String, Animation> animationMap = new HashMap<>();
    public final Map<UUID, Display> activeNodes = new HashMap<>();
    public final Map<UUID, Interaction> activeHitboxes = new HashMap<>();
    public boolean isPersistent = true; //

    public final String modelType;
    public ItemDisplay origin;
    public final UUID uuid;
    private float modelScale = 1;
    private String activeVariant = "default";

    //current animation info
    Animation animation = null;
    Integer currentFrameTime = 0; // in ticks 1T = 0.05S | 0.05 x 20 = 1S
    boolean isActive = false;

    protected ModelClass(@NotNull Location location, @NotNull String modelType, @NotNull UUID uuid) {
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

    @ApiStatus.Internal
    public void loadConfig(){
        deleteModelNodes();
        loadJsonConfig();
        loadAnimations();
        loadVariants();
        spawnModelNodes();
    }

    @ApiStatus.Internal
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
                        else if (modelNode.type.equals("locator")) { // hitbox and locator code
                            Pattern pattern = Pattern.compile("hitbox\\{w:([-+]?\\d*\\.?\\d+),h:([-+]?\\d*\\.?\\d+)}", Pattern.CASE_INSENSITIVE);
                            Matcher matcher = pattern.matcher(modelNode.name);
                            boolean matchFound = matcher.find();


                            if (matchFound){
                                modelNode.type = "hitbox";
                                double width = Double.parseDouble(matcher.group(1));
                                double height = Double.parseDouble(matcher.group(2));

                                modelNode.typeSpecificProperties.put("hitbox_width", width);
                                modelNode.typeSpecificProperties.put("hitbox_height", height);

                            }


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

    @ApiStatus.Internal
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
                                frameNode.name = nodeMap.get(frameNode.uuid).name;


                                JsonNode decomposed = nodeTransform.get("decomposed");

                                frameNode.scale = objectMapper.treeToValue(nodeTransform.get("scale"), Float[].class); // "scale": [1, 1, 1]
                                frameNode.translation = objectMapper.treeToValue(decomposed.get("translation"), Float[].class); // "translation": [0, 0, 0]
                                frameNode.leftRotation = objectMapper.treeToValue(decomposed.get("left_rotation"), Float[].class); // "left_rotation": [0, 1, 0, 0]
                                frameNode.pos = objectMapper.treeToValue(nodeTransform.get("pos"), Float[].class);

                                Pattern pattern = Pattern.compile("hitbox\\{w:([-+]?\\d*\\.?\\d+),h:([-+]?\\d*\\.?\\d+)}", Pattern.CASE_INSENSITIVE);
                                Matcher matcher = pattern.matcher(frameNode.name);
                                boolean matchFound = matcher.find();

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

                        if (this.animation != null){ // this updates active animation if the model is reloaded
                            if (animationMap.containsKey(this.animation.name)){
                                this.animation = animationMap.get(this.animation.name);

                            }

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
    public void loadVariants(){
        this.variants.clear();


        JsonNode config = ModelManager.getInstance().getLoadedModel(modelType);
        JsonNode variants = config.get("variants");

        for (JsonNode variant : variants) {
            Variant variantClass = new Variant();


            variantClass.name = variant.get("name").asText();
            variantClass.displayName = variant.get("display_name").asText();
            variantClass.uuid = UUID.fromString(variant.get("uuid").asText());

            JsonNode textureMap = variant.get("texture_map");


            Iterator<String> itr = textureMap.fieldNames();
            while (itr.hasNext()) {
                String key = itr.next();
                variantClass.textureMap.put(UUID.fromString(key), UUID.fromString(textureMap.get(key).asText()));

            }

            this.variants.put(variantClass.name, variantClass);
        }

    }

    public void setPersistence(Boolean persistence) {
        isPersistent = persistence;
    }

    public Boolean getPersistence() {
        return isPersistent;
    }

    @ApiStatus.Internal
    public void spawnModelNodes(){

        deleteModelNodes();
        activeCameras.clear();
        activeNodes.clear();
        activeHitboxes.clear();

        NamespacedKey key = new NamespacedKey(AnimatedSkript.getInstance(), "nodeType");
        NamespacedKey UuidKey = new NamespacedKey(AnimatedSkript.getInstance(), "modelUuid");
        NamespacedKey nodeUuid = new NamespacedKey(AnimatedSkript.getInstance(), "nodeUuid");

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
                    String name = node.name;



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
                case "hitbox":

                    Pattern pattern = Pattern.compile("hitbox\\{w:([-+]?\\d*\\.?\\d+),h:([-+]?\\d*\\.?\\d+)\\}", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(node.name);
                    boolean matchFound = matcher.find();


                    if (matchFound){

                        float width = Float.parseFloat(matcher.group(1));
                        float height = Float.parseFloat(matcher.group(2));

                        Interaction interaction = origin.getWorld().spawn(origin.getLocation(), Interaction.class);
                        interaction.getPersistentDataContainer().set(key, PersistentDataType.STRING, "hitbox");
                        interaction.getPersistentDataContainer().set(nodeUuid, PersistentDataType.STRING, node.uuid.toString());
                        interaction.getPersistentDataContainer().set(UuidKey, PersistentDataType.STRING, uuid.toString());

                        interaction.setInteractionHeight(height);
                        interaction.setInteractionWidth(width);

                        Location originLocation = origin.getLocation().clone();

                        Location location = originLocation.add(node.pos[0], node.pos[1], node.pos[2]);

                        interaction.teleport(location);
                        interaction.setPersistent(false);
                        activeHitboxes.put(node.uuid, interaction);



                    }
                    break;

                default:
                    throw new RuntimeException("Corrupted node in json file: " + modelType + ".json node: " + node.uuid);
            }


            if (display != null){
                display.setPersistent(false);

                display.setTransformation(applyScale(node.transformation, modelScale));
                activeNodes.put(node.uuid, display);
            }






        }

        setTint(Color.WHITE); // this stops tinted parts of the model from not having a texture for some strange reason
    }

    @ApiStatus.Internal
    public void deleteModelNodes(){
        for (Display node : activeNodes.values()) {
            node.remove();

        }
        for (Interaction node : activeHitboxes.values()) {
            node.remove();

        }

    }

    @ApiStatus.Internal
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
                        activeNodes.get(node.uuid).setInterpolationDelay(0);
                        activeNodes.get(node.uuid).setInterpolationDuration(1);
                        activeNodes.get(node.uuid).setTransformation(applyScale(node.transformation, modelScale));

                    }
                    else if (activeHitboxes.containsKey(node.uuid)){
                        Location originLocation = origin.getLocation().clone();
                        Location location = originLocation.add(node.pos[0], node.pos[1], node.pos[2]);


                        activeHitboxes.get(node.uuid).teleport(location);
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

        if (animation.frames.containsKey(currentFrameTime)){ // a bit janky but will be fixed

            for (Node node : animation.frames.get(currentFrameTime).nodeTransforms){

                if (activeHitboxes.containsKey(node.uuid)){
                    Location originLocation = origin.getLocation().clone();

                    Location location1 = originLocation.add(node.pos[0], node.pos[1], node.pos[2]);

                    location1 = originLocation.add(location1);


                    activeHitboxes.get(uuid).teleport(location1);
                }


            }
        }
        else {
            for (Node node : nodeMap.values()){ // this just sets there normal pos

                if (activeHitboxes.containsKey(node.uuid)){
                    Location originLocation = origin.getLocation().clone();

                    Location location1 = originLocation.add(node.pos[0], node.pos[1], node.pos[2]);

                    location1 = originLocation.add(location1);


                    activeHitboxes.get(uuid).teleport(location1);
                }


            }


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

    // ###############################################
    // animations

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

    public String[] getAnimationNames(){
        return animationMap.keySet().toArray(String[]::new);
    };

    public Animation[] getAnimations(){
        return animationMap.values().toArray(Animation[]::new);
    };

    public Animation getAnimation(String name){
        if (animationMap.containsKey(name))
            return animationMap.get(name);

        return null;
    };

    public void resetAnimation(){


        ModelEndAnimationEvent event;
        if (animation == null){
            event = new ModelEndAnimationEvent(uuid, modelType, "none", "none");

        }else{
            event = new ModelEndAnimationEvent(uuid, modelType, animation.name, animation.loopMode);


            this.animation = null;
        }

        for (Node node : nodeMap.values()){
            if (activeNodes.containsKey(node.uuid)){
                activeNodes.get(node.uuid).setInterpolationDelay(0);
                activeNodes.get(node.uuid).setInterpolationDuration(0);

                activeNodes.get(node.uuid).setTransformation(applyScale(node.transformation, modelScale));

            }
            else if (activeHitboxes.containsKey(node.uuid)){
                Location originLocation = origin.getLocation().clone();

                Location location = originLocation.add(node.pos[0], node.pos[1], node.pos[2]);

                activeHitboxes.get(node.uuid).teleport(location);
            }
        }




        this.isActive = false;
        Bukkit.getPluginManager().callEvent(event);


    };

    public void pauseCurrentAnimation(Boolean bool){
        if (animation == null)
            return;

        if (!bool){
            ModelPauseAnimationEvent event = new ModelPauseAnimationEvent(uuid, modelType, animation.name, animation.loopMode);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                isActive = !bool;

            }

        }
        else{

            ModelUnpauseAnimationEvent event = new ModelUnpauseAnimationEvent(uuid, modelType, animation.name, animation.loopMode);
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

        ModelFrameSetAnimationEvent event = new ModelFrameSetAnimationEvent(uuid, modelType, animation.name, animation.loopMode, currentFrameTime, ticks, animation.duration );
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

    public void playAnimation(String name){
        if (name == null) {
            return;
        }




        if (animationMap.containsKey(name)){

            ModelStartAnimationEvent event = new ModelStartAnimationEvent(uuid, modelType, name);
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

    // ###############################################
    // variant
    public Variant[] getAllVariants(){
        return variants.values().toArray(new Variant[0]);
    };

    public String getActiveVariant(){
        return activeVariant;
    };

    public boolean hasVariant(String variant) {
        if (variant == null)
            return false;

        return variants.containsKey(variant);
    }

    public boolean hasVariant(Variant variant) {
        if (variant == null)
            return false;

        return variants.containsKey(variant.name);
    }

    public void setActiveVariant(String variant) {
        if (variant == null)
            variant = "default";


        if (!variants.containsKey(variant)){
            return;
        }
        activeVariant = variant;
        for (UUID nodeKey : activeNodes.keySet()){
            Display node = activeNodes.get(nodeKey);

            NamespacedKey key = new NamespacedKey(AnimatedSkript.getInstance(), "nodeType");


            if (node.getPersistentDataContainer().has(key) && node.getPersistentDataContainer().get(key, PersistentDataType.STRING).equals("bone")){

                ItemStack boneItemStack = new ItemStack(Material.PAPER);
                NamespacedKey modelKey = new NamespacedKey("animated-skript", modelType + "/" + variant + "/" + nodeKey.toString());
                ItemMeta meta = boneItemStack.getItemMeta();
                meta.setItemModel(modelKey);
                boneItemStack.setItemMeta(meta);
                ItemDisplay itemDisplay = (ItemDisplay) node;

                itemDisplay.setItemStack(boneItemStack);
            }




        }
    }

    // ###############################################

    public void setTint(Color color) {

        for (UUID nodeKey : activeNodes.keySet()){
            Display node = activeNodes.get(nodeKey);

            NamespacedKey key = new NamespacedKey(AnimatedSkript.getInstance(), "nodeType");


            if (node.getPersistentDataContainer().has(key) && node.getPersistentDataContainer().get(key, PersistentDataType.STRING).equals("bone")){
                ItemDisplay itemDisplay = (ItemDisplay) node;
                ItemStack itemStack = itemDisplay.getItemStack();

                if (color == null)
                    itemStack.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor().color(Color.WHITE).build());
                else
                    itemStack.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor().color(color).build());

                itemDisplay.setItemStack(itemStack);
            }




        }
    }

    // ###############################################
    // node methods

   public Display[] getAllDisplayNodes() {
        return activeNodes.values().toArray(Display[]::new);
    }

    public UUID[] getAllDisplayNodesUuids() {
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

    // ###############################################
    // nodes (info)

    public Node[] getAllNodes() {
        return nodeMap.values().toArray(Node[]::new);
    }

    public UUID[] getAllNodesUuids() {
        return nodeMap.keySet().toArray(UUID[]::new);
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
    // ###############################################
    // hitbox node methods

    public UUID[] getAllHitboxes() {
        return activeHitboxes.keySet().toArray(UUID[]::new);
    }

    public Interaction getActiveHitbox(UUID uuid){
        if (activeHitboxes.containsKey(uuid)){
            return activeHitboxes.get(uuid);
        }
        return null;
    }

    public Boolean hasActiveHitbox(UUID uuid){
        return activeNodes.containsKey(uuid);
    }

    public Node getHitbox(UUID uuid){
        if (nodeMap.containsKey(uuid)){
            return nodeMap.get(uuid);
        }
        return null;
    }

    public Boolean hasHitbox(UUID uuid){
        return nodeMap.containsKey(uuid);
    }




    // ############################################### camera stuff, does not work because I am stupid

    @ApiStatus.Experimental
    public Display getActiveCamera(String name){
        if (activeCameras.containsKey(name))
            return activeNodes.get(activeCameras.get(name));
        return null;

    }

    @ApiStatus.Experimental
    public boolean hasActiveCamera(String name){
        return activeCameras.containsKey(name);
    }

    @ApiStatus.Experimental
    public UUID getUuidFromActiveCamera(String name){
        if (activeCameras.containsKey(name)){
            return activeCameras.get(name);
        }
        return null;
    }

    @ApiStatus.Experimental
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

    // ###############################################
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
