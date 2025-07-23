package net.outmoded.animated_skript.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.Config;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

import static org.bukkit.Bukkit.getServer;

public class EffTeleportActiveModel extends Effect {

    static {
        Skript.registerEffect(EffTeleportActiveModel.class, "[animated-skript] teleport active-model %activemodel% to %location%");
    }
    private Expression<ModelClass> activeModel;
    private Expression<Location> locationExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[0];
        locationExpression = (Expression<Location>) expressions[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);
        Location location = locationExpression.getSingle(event);
        if (modelClass != null && location != null){
            modelClass.teleport(location);
        }
        else{
            if (Config.debugMode())
                getServer().getConsoleSender().sendMessage("did not tp model");
        }
    }
}
