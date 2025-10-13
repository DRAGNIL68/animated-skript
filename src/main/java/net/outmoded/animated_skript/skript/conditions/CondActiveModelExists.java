package net.outmoded.animated_skript.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.Config;
import net.outmoded.animated_skript.models.ModelManager;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;


public class CondActiveModelExists extends Condition {

    static {
        Skript.registerCondition(CondActiveModelExists.class, "[animated-skript] active-model with uuid %string% exits");
    }

    private Expression<String> text;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        text = (Expression<String>) expressions[0];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    public boolean check(Event event) {

        String uuidAsString = text.getSingle(event);
        UUID uuid = null;
        try {
            uuid = UUID.fromString(uuidAsString);

        }catch (IllegalArgumentException e){
            if (Config.debugMode())
                getServer().getConsoleSender().sendMessage(ChatColor.RED + "Error " + text.getSingle(event));

            return false;

        }
        return ModelManager.getInstance().activeModelExists(uuid);
    }



}

