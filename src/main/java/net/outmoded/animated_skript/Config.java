package net.outmoded.animated_skript;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;

public class Config {
    private static boolean debug = true;
    private static boolean generatePack = true;
    private static int autoSaveTimer = 5;
    private static final HashMap<String, String> lang = new HashMap<>();
    private static YamlConfiguration configYml;

    private static File configFile;

    private final static Config configInstance = new Config();

    private Config(){ // constructor makes it private

    }



    public static void load(){

        File lang = new File(AnimatedSkript.getInstance().getDataFolder(), "lang.yml");
        configFile = new File(AnimatedSkript.getInstance().getDataFolder(), "config.yml");
        File contentsFolder = new File(AnimatedSkript.getInstance().getDataFolder(), "contents");
        File outputFolder = new File(AnimatedSkript.getInstance().getDataFolder(), "output");


        if (!configFile.exists())
            AnimatedSkript.getInstance().saveResource("config.yml", false);

        if (!lang.exists())
            AnimatedSkript.getInstance().saveResource("lang.yml", false);

        if (!contentsFolder.exists()){
            contentsFolder.mkdir();

            final Component logo = MiniMessage.miniMessage().deserialize(
                    "<color:#1235ff>[</color><color:#3daeff>animated-skript</color><color:#1235ff>]</color> "
            );

            final Component text = MiniMessage.miniMessage().deserialize(
                    "<color:#0dff1d>Created Contents Folder</green>"
            );

            getServer().getConsoleSender().sendMessage(logo.append(text));

        }

        if (!outputFolder.exists()){
            outputFolder.mkdir();

        }


        configYml = new YamlConfiguration();
        configYml.options().parseComments(true);





       try{
           configYml.load(configFile);
           File[] listedFiles = contentsFolder.listFiles();
           if (listedFiles != null) {
               for (File modelfile : listedFiles) {

               }
           }

           debug = Boolean.valueOf(configYml.getString("debug"));
           autoSaveTimer = Integer.valueOf(configYml.getString("auto_save_timer"));
           generatePack = Boolean.valueOf(configYml.getString("generate_resource_pack"));

       } catch (Exception e) {
            e.printStackTrace();

       }


    }
 
    public static boolean debugMode(){
        return debug;
    }

    public static int getAutoSaveTimer(){
        return autoSaveTimer;
    }

    public static boolean shouldGeneratePack(){
        return generatePack;
    }

    public static void loadLang(){

        configYml = new YamlConfiguration();
        configYml.options().parseComments(true);
        File configFile = new File(AnimatedSkript.getInstance().getDataFolder(), "lang.yml");
        try {
            configYml.load(configFile);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        lang.clear();
        for (String key : configYml.getKeys(false)) {
            lang.put(key, String.valueOf(configYml.getString(key)));
        }


    }

    public static String getLang(String key){
        if (lang.containsKey(key))
            return lang.get(key);

        return null;
    }
}

