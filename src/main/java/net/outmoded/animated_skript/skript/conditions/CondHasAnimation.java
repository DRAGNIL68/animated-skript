package net.outmoded.animated_skript.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.new_stuff.Variant;
import org.bukkit.event.Event;

import javax.annotation.Nullable;


public class CondHasAnimation extends Condition {

    static {
        Skript.registerCondition(CondHasAnimation.class, "[animated-skript] %activemodel% has animation %string%");
    }

    private Expression<String> stringExpression;
    private Expression<ModelClass> modelClassExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        modelClassExpression = (Expression<ModelClass>) expressions[0];
        stringExpression = (Expression<String>) expressions[1];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    public boolean check(Event event) {
        String string = stringExpression.getSingle(event);
        ModelClass modelClass = modelClassExpression.getSingle(event);

        if (modelClass == null)
            return false;

        return modelClass.hasAnimation(string); // hasVariant() has null checking



    }



}

