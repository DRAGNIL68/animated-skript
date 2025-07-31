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
import java.util.UUID;


public class ExprGetNodeUuid extends SimpleExpression<UUID> {

    static {
        Skript.registerExpression(ExprGetNodeUuid.class, UUID.class, ExpressionType.COMBINED, "[animated-skript] [get] [the] %activemodelnode%('s|s) uuid");
    }

    private Expression<Node> nodeExpression;

    @Override
    public Class<? extends UUID> getReturnType() {
        //1
        return UUID.class;
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
        return null;
    }

    @Override
    @Nullable
    protected UUID[] get(Event event) {
        Node node = nodeExpression.getSingle(event);

        if (node != null && node.uuid != null){
            return new UUID[] {node.uuid};
        }

        return null;

    }
}

