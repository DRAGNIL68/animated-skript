package net.outmoded.animated_skript.skript.expressions.node;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.entity.Display;
import org.bukkit.event.Event;

import javax.annotation.Nullable;


public class ExprGetActiveModelsDisplayNodes extends SimpleExpression<Display> {

    static {
        Skript.registerExpression(ExprGetActiveModelsDisplayNodes.class, Display.class, ExpressionType.COMBINED, "[animated-skript] [get] [the||all] display nodes of %activemodel%");
    }

    private Expression<ModelClass> modelClass;

    @Override
    public Class<? extends Display> getReturnType() {
        //1
        return Display.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return false;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        modelClass = (Expression<ModelClass>) exprs[0];


        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return null;
    }

    @Override
    @Nullable
    protected Display[] get(Event event) {
        ModelClass modelClass1 = modelClass.getSingle(event);
        if (modelClass1 != null){

            return modelClass1.getAllDisplayNodes();
        }


        return null;
    }
}

