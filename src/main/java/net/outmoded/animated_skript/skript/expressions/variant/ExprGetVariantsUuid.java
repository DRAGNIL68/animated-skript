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
import java.util.UUID;


public class ExprGetVariantsUuid extends SimpleExpression<UUID> {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprGetVariantsUuid.class, UUID.class)
                        .addPatterns(
                                "[animated-skript] get %activemodelvariant%('s|s) uuid"
                        )
                        .supplier(ExprGetVariantsUuid::new)
                        .build());

    }

    private Expression<Variant> variantExpression; // if true = loaded-models | if false = active-models

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
    protected UUID[] get(Event event) {
        Variant variant = variantExpression.getSingle(event);
        if (variant != null){
            return new UUID[] {variant.uuid};
        }


        return null;
    }
}

