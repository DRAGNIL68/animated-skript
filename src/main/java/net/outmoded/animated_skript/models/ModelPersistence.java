package net.outmoded.animated_skript.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.kyori.adventure.text.event.ClickEvent;
import net.outmoded.animated_skript.AnimatedSkript;
import net.outmoded.animated_skript.models.nodes.ActiveAnimation;
import net.outmoded.animated_skript.models.nodes.Animation;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;

import java.io.File;
import java.sql.*;
import java.util.*;

import static net.outmoded.animated_skript.Config.debugMode;
import static org.bukkit.Bukkit.getServer;

public final class ModelPersistence implements Listener {
    public static Map<String, ArrayList<ModelClass>> chunkMap = new HashMap<>();

    private static final ModelPersistence SAVEDATA_DATABASE = new ModelPersistence();
    protected Connection connection;


    @EventHandler
    private static void onEntitiesUnload(EntitiesUnloadEvent event){
        String chunk_id = event.getChunk().getWorld().getName()+"|x-"+event.getChunk().getX()+"|z-"+event.getChunk().getZ(); // world|x-3|z-4
        if (chunkMap.containsKey(chunk_id)){

            for(ModelClass modelClass : chunkMap.get(chunk_id)) {
                if (!modelClass.isPersistent)
                    continue;

                SAVEDATA_DATABASE.removeModel(modelClass.uuid);
                SAVEDATA_DATABASE.addModel(modelClass);
                ModelManager.getInstance().removeActiveModel(modelClass.uuid);
            }

            chunkMap.remove(chunk_id);

            if (debugMode())
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "animated-skript: saved model");
        }

    }

    public void saveAllModels(){
        for(ModelClass modelClass : ModelManager.getInstance().getAllActiveModels()) {
            SAVEDATA_DATABASE.removeModel(modelClass.uuid);
            SAVEDATA_DATABASE.addModel(modelClass);
        }
    }

    @EventHandler
    private static void onChunkLoad(ChunkLoadEvent event){
        String chunk_id = event.getChunk().getWorld().getName()+"|x-"+event.getChunk().getX()+"|z-"+event.getChunk().getZ(); // world|x-3|z-4
        // get all entries with the corresponding chunk_id
        for (DatabaseModelClass databaseModelClass : SAVEDATA_DATABASE.getModelsInChunk(chunk_id)){

            if (ModelManager.getInstance().activeModelExists(databaseModelClass.uuid)){
                continue;
            }

            if (!ModelManager.getInstance().loadedModelExists(databaseModelClass.type)){
                continue;
            }



            ModelClass modelClass = ModelManager.getInstance().spawnNewModel(databaseModelClass.type, databaseModelClass.location, databaseModelClass.uuid);

            if (databaseModelClass.databaseAnimations != null){
                for (DatabaseAnimation databaseAnimation : databaseModelClass.databaseAnimations){
                    modelClass.playAnimation(databaseAnimation.animationName);
                    modelClass.setActiveAnimationFrame(databaseAnimation.animationName, databaseAnimation.currentFrame);
                    modelClass.pauseActiveAnimation(databaseAnimation.animationName, databaseAnimation.isPaused);


                }


            }

            SAVEDATA_DATABASE.removeModel(databaseModelClass.uuid);

            if (debugMode())
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "animated-skript: model loaded from save data");
        }
    }


    private ModelPersistence(){
        // do stuff
    }

    public void load(){
        this.connect();
        this.createTables();

    }

    private void connect() {
        try {
            if (this.connection != null && !this.connection.isClosed())
                return;

            Class.forName("org.sqlite.JDBC");

            final File file = new File(AnimatedSkript.getInstance().getDataFolder(), "save_data.sqlite");

            if (!file.exists())
                file.createNewFile();

            final String jdbcUrl = "jdbc:sqlite:" + file.getAbsolutePath();
            this.connection = DriverManager.getConnection(jdbcUrl);

        } catch (final Exception ex) {
            AnimatedSkript.getInstance().getLogger().severe(" Failed to connect to database: " + ex.getMessage());

            ex.printStackTrace();
        }
    }

    private void createTables() {
        try {
            final Statement statement = this.connection.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");

            statement.execute("CREATE TABLE IF NOT EXISTS save_data (" +
                    "model_uuid VARCHAR(36) PRIMARY KEY, " +
                    "model_type NOT NULL," +
                    // this is for the origin of the model
                    "chunk_id NOT NULL," + // world|x-4|z-4
                    "chunk_id NOT NULL," +
                    "location NOT NULL," + // stores json :sob:
                    "animations NOT NULL" + // stores json for the current animations and what time they are on
                    ")");

            // Create index for faster lookups
            statement.execute("CREATE INDEX IF NOT EXISTS idx_chunk_ids ON save_data (chunk_id)");



            statement.close();

        } catch (final SQLException ex) {
            AnimatedSkript.getInstance().getLogger().severe("Failed to create tables: " + ex.getMessage());

            ex.printStackTrace();
        }
    }

    /**
     * Close the database connection
     */
    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();

                AnimatedSkript.getInstance().getLogger().info("Database connection closed.");
            }
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static ModelPersistence getInstance() {
        return SAVEDATA_DATABASE;
    }

    public boolean addModel(ModelClass modelClass) {
        synchronized (this.connection) {
            try {

                Location location = modelClass.getOriginLocation();
                String chunk_id = location.getWorld().getName()+"|x-"+location.getChunk().getX()+"|z-"+location.getChunk().getZ();

                final PreparedStatement statement = this.connection.prepareStatement(
                        "INSERT INTO save_data (model_uuid, model_type, chunk_id, location, animations ) VALUES (?, ?, ?, ?, ?)");

                statement.setString(1, modelClass.getUuid().toString());
                statement.setString(2, modelClass.modelType);
                statement.setString(3, chunk_id);
                statement.setString(4, encodeLocationAsJson(location));
                statement.setString(5, databaseAnimationToJson(modelClass));

                final int rows = statement.executeUpdate();
                statement.close();
                return rows > 0;


            } catch (final SQLException ex) {
                ex.printStackTrace();

                return false;
            }
        }
    }

    public List<DatabaseModelClass> getModelsInChunk(String chunkId) {
        final List<DatabaseModelClass> modelClasses = new ArrayList<>();

        try {
            String sql = "SELECT * FROM save_data WHERE chunk_id = ?";

            final PreparedStatement statement = this.connection.prepareStatement(sql);

            statement.setString(1, chunkId);

            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
                modelClasses.add(DatabaseModelClass.fromResultSet(resultSet));

            resultSet.close();
            statement.close();

        } catch (final SQLException ex) {
            ex.printStackTrace();
        }

        return modelClasses;
    }

    public boolean removeModel(UUID modelUuid) {
        synchronized (this.connection) {
            try {

                final PreparedStatement statement = this.connection.prepareStatement(
                        "DELETE FROM save_data WHERE model_uuid = ?");

                statement.setString(1, modelUuid.toString()); // Bind the UUID parameter

                final int rows = statement.executeUpdate();
                statement.close();

                return rows > 0;

            } catch (final SQLException ex) {
                ex.printStackTrace();

                return false;
            }
        }
    }

    public static String encodeLocationAsJson(Location location){
        ObjectMapper objectMapper = new ObjectMapper();

        String json = null;
        try {

            json = objectMapper.writeValueAsString(DatabaseLocation.toDatabaseLocation(location));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return json;
    }

    public static Location decodeJsonAsLocation(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        DatabaseLocation location = null;
        try {
            location = objectMapper.readValue(json, DatabaseLocation.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return location.toLocation();
    }

    public static String databaseAnimationToJson(ModelClass modelClass){
        ArrayList<DatabaseAnimation> databaseAnimations = new ArrayList<>();
        for (ActiveAnimation animation : modelClass.getActiveAnimations()){
            DatabaseAnimation newAnimation = new DatabaseAnimation();
            newAnimation.animationName = animation.animationReference.name;
            newAnimation.currentFrame = animation.animationReference.currentFrameTime;
            newAnimation.isPaused = animation.animationReference.isPaused;
            databaseAnimations.add(newAnimation);

        }

        ObjectMapper objectMapper = new ObjectMapper();


        try {
            return objectMapper.writeValueAsString(databaseAnimations);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseAnimation[] jsonToDatabaseAnimations(String json){
        ArrayList<DatabaseAnimation> databaseAnimations = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();


        JsonNode root = null;
        try {
            root = objectMapper.readTree(json);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        for (JsonNode node : root) {
            DatabaseAnimation databaseAnimation = new DatabaseAnimation();

            databaseAnimation.animationName = node.get("animationName").asText();
            databaseAnimation.currentFrame = node.get("currentFrame").asInt();
            databaseAnimation.isPaused = node.get("isPaused").asBoolean(false);

            databaseAnimations.add(databaseAnimation);


        }

        return databaseAnimations.toArray(new DatabaseAnimation[0]);
    }

    public final static class DatabaseModelClass{
        public final UUID uuid;
        public final String type;
        public final String chunkId;
        public final Location location;
        public final DatabaseAnimation[] databaseAnimations;


        DatabaseModelClass(UUID uuid, String type, String chunkId, String location, String animations){
            this.uuid = uuid;
            this.type = type;
            this.chunkId = chunkId;
            this.location = decodeJsonAsLocation(location);
            this.databaseAnimations = jsonToDatabaseAnimations(animations);


        }


        public static DatabaseModelClass fromResultSet(ResultSet row) throws SQLException {
            return new DatabaseModelClass(
                    UUID.fromString(row.getString("model_uuid")),
                    row.getString("model_type"),
                    row.getString("chunk_id"),
                    row.getString("location"),
                    row.getString("animations"));
        }



    }

    public final static class DatabaseLocation{
        @JsonProperty("world")
        public String world;

        @JsonProperty("chunk_x")
        public String chunkX;

        @JsonProperty("chunk_z")
        public String chunkZ;

        @JsonProperty
        public String x;

        @JsonProperty
        public String y;

        @JsonProperty
        public String z;

        @JsonProperty
        public String pitch;

        @JsonProperty
        public String yaw;


        DatabaseLocation(String world, String chunkX, String chunkZ, String x, String y, String z, String pitch, String yaw){

            this.world = world;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
            this.x = x;
            this.y = y;
            this.z = z;
            this.pitch = pitch;
            this.yaw = yaw;
        }

        @JsonCreator
        public DatabaseLocation() {}

        public Location toLocation(){
            Location location = new Location(
                    Bukkit.getWorld(world),
                    Double.parseDouble(x),
                    Double.parseDouble(y),
                    Double.parseDouble(z),
                    Float.parseFloat(yaw),
                    Float.parseFloat(pitch));


            return location;
        }

        public static DatabaseLocation toDatabaseLocation(Location location){
            return new DatabaseLocation(
                    location.getWorld().getName(),
                    String.valueOf(location.getChunk().getX()),
                    String.valueOf(location.getChunk().getZ()),
                    String.valueOf(location.x()),
                    String.valueOf(location.y()),
                    String.valueOf(location.z()),
                    String.valueOf(location.getPitch()),
                    String.valueOf(location.getYaw()));


        }



    }

    public final static class DatabaseAnimation{
        @JsonProperty
        public String animationName;

        @JsonProperty
        public Integer currentFrame;

        @JsonProperty
        public Boolean isPaused;

        @JsonCreator
        DatabaseAnimation(){}

    }
}

