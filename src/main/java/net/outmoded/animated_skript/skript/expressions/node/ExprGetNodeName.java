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
import java.lang.String;


public class ExprGetNodeName extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprGetNodeName.class, String.class, ExpressionType.COMBINED, "[animated-skript] [get] [the] %activemodelnode%('s|s) name");
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
        return null;
    }

    @Override
    @Nullable
    protected String[] get(Event event) {

        if (nodeExpression.getSingle(event) != null){
            return new String[] {nodeExpression.getSingle(event).name};
        }

        return null;

    }
}

