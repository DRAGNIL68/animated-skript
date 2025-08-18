package net.outmoded.animated_skript.skript.effects.variant;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffStopAnimation extends Effect {

    static {
        Skript.registerEffect(EffStopAnimation.class, "[animated-skript] stop current animation %string% of %activemodel%");
    }

    private Expression<ModelClass> activeModel;
    private Expression<String> stringExpression;

    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[1];
        stringExpression = (Expression<String>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);
        String string = stringExpression.getSingle(event);

        if (modelClass != null && string != null){
            modelClass.stopActiveAnimation(string);
        }


    }
}
