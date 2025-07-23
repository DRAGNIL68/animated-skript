package net.outmoded.animated_skript.skript.expressions.variants;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.new_stuff.Variant;
import org.bukkit.event.Event;

import javax.annotation.Nullable;


public class ExprGetVariantsName extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprGetVariantsName.class, String.class, ExpressionType.COMBINED, "[animated-skript] get %variants%('s|s) name");
    }

    private Expression<Variant> variantExpression; // if true = loaded-models | if false = active-models

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
        variantExpression = (Expression<Variant>) exprs[0];


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
        Variant variant = variantExpression.getSingle(event);
        if (variant != null){
            return new String[] {variant.name};
        }


        return null;
    }
}

