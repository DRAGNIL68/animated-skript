package net.outmoded.modelengine.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.modelengine.models.ModelClass;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class TeleportActiveModel extends Effect {

    static {
        Skript.registerEffect(TeleportActiveModel.class, "[animated-skript] (teleport||tp) %activemodel% to %location%");
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
            modelClass.getOrigin().teleport(location);
        }
    }
}
