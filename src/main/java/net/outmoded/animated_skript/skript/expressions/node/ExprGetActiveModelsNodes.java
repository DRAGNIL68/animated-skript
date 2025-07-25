package net.outmoded.animated_skript.skript.expressions.node;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.new_stuff.Node;
import org.bukkit.event.Event;

import javax.annotation.Nullable;


public class ExprGetActiveModelsNodes extends SimpleExpression<Node> {

    static {
        Skript.registerExpression(ExprGetActiveModelsNodes.class, Node.class, ExpressionType.COMBINED, "[animated-skript] [get] [the||all] nodes of %activemodel%");
    }

    private Expression<ModelClass> modelClass;

    @Override
    public Class<? extends Node> getReturnType() {
        //1
        return Node.class;
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
    protected Node[] get(Event event) {
        ModelClass modelClass1 = modelClass.getSingle(event);
        if (modelClass1 != null){

            return modelClass1.getAllNodes();
        }


        return null;
    }
}

