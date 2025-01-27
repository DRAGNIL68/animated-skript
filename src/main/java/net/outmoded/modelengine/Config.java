package net.outmoded.modelengine;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static org.bukkit.Bukkit.getServer;

public class Config {
    private static boolean debug = true;


    private final static Config configInstance = new Config();

    private Config(){ // constructor makes it private

    }

    private static File configFile;
    private static YamlConfiguration configYml;

    public static void load(){
        File dataFolder = ModelEngine.getInstance().getDataFolder();

        configFile = new File(ModelEngine.getInstance().getDataFolder(), "config.yml");

        File contentsFolder = new File(ModelEngine.getInstance().getDataFolder(), "contents");
        File outputFolder = new File(ModelEngine.getInstance().getDataFolder(), "output");

        if (!configFile.exists())
            ModelEngine.getInstance().saveResource("config.yml", false);

        if (!contentsFolder.exists()){
            contentsFolder.mkdir();
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Created Contents Folder");

        }
        if (!outputFolder.exists()){
            outputFolder.mkdir();

        }


        configYml = new YamlConfiguration();
        configYml.options().parseComments(true);





        try{
            configYml.load(configFile);
            File[] listedFiles = contentsFolder.listFiles();
            if (listedFiles != null){
                for (File modelfile : listedFiles) {

                }
            }

            debug = Boolean.valueOf(configYml.getString("debug"));


        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public static boolean debugMode(){
        return debug;
    }


}

