package net.outmoded.animated_skript.skript.expressions.node;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.new_stuff.Node;
import org.bukkit.entity.Display;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.UUID;


public class ExprGetActiveModelsNode extends SimpleExpression<Node> {

    static {
        Skript.registerExpression(ExprGetActiveModelsNode.class, Node.class, ExpressionType.COMBINED, "[animated-skript] [get] [the] node %string% of %activemodel%");
    }

    private Expression<ModelClass> modelClass;
    private Expression<String> stringExpression;// if true = loaded-models | if false = active-models

    @Override
    public Class<? extends Node> getReturnType() {
        //1
        return Node.class;
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
    protected Node[] get(Event event) {
        ModelClass modelClass1 = modelClass.getSingle(event);
        String string = stringExpression.getSingle(event);

        if (modelClass1 != null && string != null){
            return new Node[]{modelClass1.getNode(UUID.fromString(string))};
        }


        return null;
    }
}

