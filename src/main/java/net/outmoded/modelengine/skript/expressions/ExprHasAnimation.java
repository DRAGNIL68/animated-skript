package net.outmoded.modelengine.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.modelengine.models.ModelClass;
import net.outmoded.modelengine.skript.SkriptManager;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprHasAnimation extends SimpleExpression<Boolean> {

    static {
        Skript.registerExpression(ExprLastSpawnedActiveModel.class, ModelClass.class, ExpressionType.COMBINED, "[animated-skript] %activemodel% has animation %string%");
    }

    private Expression<ModelClass> activeModel;
    private Expression<String> animationName;

    @Override
    public Class<? extends Boolean> getReturnType() {
        //1
        return Boolean.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) exprs[0];
        animationName = (Expression<String>) exprs[1];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return null;
    }

    @Override
    protected Boolean[] get(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);
        String string = animationName.getSingle(event);

        if (string != null){
            if (modelClass != null){
                return new Boolean[] {modelClass.hasAnimation(string)};

            }

        }
        return new Boolean[] {};
    }
}

