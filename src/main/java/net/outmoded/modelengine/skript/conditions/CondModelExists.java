package net.outmoded.modelengine.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.modelengine.models.ModelManager;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

import java.util.UUID;

import static org.bukkit.Bukkit.getServer;


public class CondModelExists extends Condition {

    static {
        Skript.registerCondition(CondModelExists.class, "[animated-skript] (:loaded-model|active-model) %string% exits");
    }

    private boolean type; // if true = loaded-models | if false = active-models
    private Expression<String> text;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        type = parseResult.hasTag("loaded-models");
        text = (Expression<String>) expressions[0];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "Model Exits: " + type;
    }

    @Override
    public boolean check(Event event) {
        if (type){

            return ModelManager.loadedModelExists(text.toString());
        }
        String uuidAsString = text.toString().replace("\"", "");
        UUID uuid = null;
        try {
            uuid = UUID.fromString(uuidAsString);

        }catch (IllegalArgumentException e){
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error " + text.toString());
            return false;

        }
        return ModelManager.activeModelExists(uuid);
    }



}

