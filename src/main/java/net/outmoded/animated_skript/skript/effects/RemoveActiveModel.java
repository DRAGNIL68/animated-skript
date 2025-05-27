package net.outmoded.animated_skript.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.ModelManager;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class RemoveActiveModel extends Effect {

    static {
        Skript.registerEffect(RemoveActiveModel.class, "[animated-skript] remove [the] active-model %activemodel%");
    }

    private Expression<ModelClass> activeModel;
    private Expression<Location> locationExpression;
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);

        if (modelClass != null){
            ModelManager.getInstance().removeActiveModel(modelClass.getUuid());


        }
    }
}
