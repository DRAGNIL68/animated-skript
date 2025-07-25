package net.outmoded.animated_skript.skript.effects.scale;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffSetScale extends Effect {

    static {
        Skript.registerEffect(EffSetScale.class, "[animated-skript] set %activemodel%('s|s) scale to %double/integer%");
    }

    private Expression<ModelClass> activeModel;
    private Expression<?> expression;
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[0];
        expression = (Expression<?>) expressions[1];

        //if (!(expression.getReturnType() == Double.class) || !(expression.getReturnType() == Integer.class))
            //return false;


        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);

        if (modelClass == null){
            return;
        }

        if (expression.getReturnType() == Double.class){
            Double aDouble = (Double) expression.getSingle(event);
            if (aDouble != null ){
                modelClass.setScale(aDouble.floatValue());

            }

        }else {
            Integer integer = (Integer) expression.getSingle(event);
            if (integer != null ){
                modelClass.setScale(integer.floatValue());

            }
        }






    }
}
