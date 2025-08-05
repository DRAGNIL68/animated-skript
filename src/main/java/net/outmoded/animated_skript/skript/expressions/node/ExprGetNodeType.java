package net.outmoded.animated_skript.skript.expressions.node;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.nodes.Node;
import org.bukkit.event.Event;

import javax.annotation.Nullable;


public class ExprGetNodeType extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprGetNodeType.class, String.class, ExpressionType.COMBINED, "[animated-skript] [get] [the] active-node %activemodelnode%('s|s) type");
    }

    private Expression<Node> nodeExpression;

    @Override
    public Class<? extends String> getReturnType() {
        //1
        return String.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        nodeExpression = (Expression<Node>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return "";
    }

    @Override
    @Nullable
    protected String[] get(Event event) {

        Node node = nodeExpression.getSingle(event);

        if (node != null && node.type != null){
            return new String[] {node.type};
        }

        return null;

    }
}

