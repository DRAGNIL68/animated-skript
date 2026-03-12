package net.outmoded.animated_skript.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;


public class CondHasVariant1 extends Condition {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.CONDITION,
                SyntaxInfo.builder(CondHasVariant1.class)
                        .addPatterns(
                                "[animated-skript] %activemodel% has [the] variant %activemodelvariant%"
                        )
                        .supplier(CondHasVariant1::new)
                        .build());

    }

    private Expression<String> variantExpression;
    private Expression<ModelClass> modelClassExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        modelClassExpression = (Expression<ModelClass>) expressions[0];
        variantExpression = (Expression<String>) expressions[1];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    public boolean check(Event event) {
        String string = variantExpression.getSingle(event);
        ModelClass modelClass = modelClassExpression.getSingle(event);

        if (modelClass == null)
            return false;

        return modelClass.hasVariant(string); // hasVariant() has null checking



    }



}

