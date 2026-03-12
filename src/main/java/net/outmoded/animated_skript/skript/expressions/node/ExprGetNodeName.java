package net.outmoded.animated_skript.skript.expressions.node;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.AnimatedSkript;
import net.outmoded.animated_skript.models.nodes.Node;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;
import java.lang.String;


public class ExprGetNodeName extends SimpleExpression<String> {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprGetNodeName.class, String.class)
                        .addPatterns(
                                "[animated-skript] [get] [the] active-node %activemodelnode%('s|s) name"
                        )
                        .supplier(ExprGetNodeName::new)
                        .build());

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
    protected String[] get(Event event) {

        Node node = nodeExpression.getSingle(event);


        if (node != null && node.name != null)

        {
            return new String[] {node.name};
        }

        return null;

    }
}

