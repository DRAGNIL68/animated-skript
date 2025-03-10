package net.outmoded.modelengine.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.modelengine.models.ModelClass;
import org.bukkit.entity.Display;
import org.bukkit.event.Event;

import javax.annotation.Nullable;


public class ExprGetActiveModelsNode extends SimpleExpression<Display> {

    static {
        Skript.registerExpression(ExprGetActiveModelsNode.class, Display.class, ExpressionType.COMBINED, "[animated-skript] [get] [the] node %string% of %activemodel%");
    }

    private Expression<ModelClass> modelClass;
    private Expression<String> stringExpression;// if true = loaded-models | if false = active-models

    @Override
    public Class<? extends Display> getReturnType() {
        //1
        return Display.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        modelClass = (Expression<ModelClass>) exprs[1];
        stringExpression = (Expression<String>) exprs[1];



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
        String string = stringExpression.getSingle(event);

        if (modelClass1 != null && string != null){
            return new Display[] {modelClass1.getNode(string)};
        }


        return null;
    }
}

