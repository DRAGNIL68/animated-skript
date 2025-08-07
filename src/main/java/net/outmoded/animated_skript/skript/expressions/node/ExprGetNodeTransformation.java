 package net.outmoded.animated_skript.skript.expressions.node;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.nodes.Node;
import org.bukkit.event.Event;
import org.bukkit.util.Transformation;

import javax.annotation.Nullable;


public class ExprGetNodeTransformation extends SimpleExpression<Transformation> {

    static {
        Skript.registerExpression(ExprGetNodeTransformation.class, Transformation.class, ExpressionType.COMBINED, "[animated-skript] [get] [the] active-node %activemodelnode%('s|s) transform");
    }

    private Expression<Node> nodeExpression;

    @Override
    public Class<? extends Transformation> getReturnType() {
        //1
        return Transformation.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        nodeExpression = (Expression<Node>) exprs[0];

        if (nodeExpression == null)
            return false;

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return "";
    }

    @Override
    @Nullable
    protected Transformation[] get(Event event) {

        Node node = nodeExpression.getSingle(event);


        if (node != null && node.transformation != null){
            return new Transformation[] {node.transformation};
        }

        return null;

    }
}

