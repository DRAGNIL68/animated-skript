package net.outmoded.animated_skript.skript.expressions.variant;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.nodes.Variant;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;


public class ExprGetVariantsDisplayName extends SimpleExpression<String> {


    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprGetVariantsDisplayName.class, String.class)
                        .addPatterns(
                                "[animated-skript] get %activemodelvariant%('s|s) display name"
                        )
                        .supplier(ExprGetVariantsDisplayName::new)
                        .build());

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
        return "";
    }

    @Override
    @Nullable
    protected String[] get(Event event) {
        Variant variant = variantExpression.getSingle(event);
        if (variant != null){
            return new String[] {variant.displayName};
        }


        return null;
    }
}

