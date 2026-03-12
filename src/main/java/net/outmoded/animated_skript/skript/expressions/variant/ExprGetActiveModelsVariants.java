package net.outmoded.animated_skript.skript.expressions.variant;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.nodes.Variant;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;


public class ExprGetActiveModelsVariants extends SimpleExpression<Variant> {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprGetActiveModelsVariants.class, Variant.class)
                        .addPatterns(
                                "[animated-skript] get all %activemodel%('s|s) variant"
                        )
                        .supplier(ExprGetActiveModelsVariants::new)
                        .build());

    }

    private Expression<ModelClass> modelClass;

    @Override
    public Class<? extends Variant> getReturnType() {
        //1
        return Variant.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return false;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        modelClass = (Expression<ModelClass>) exprs[0];


        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return "";
    }

    @Override
    @Nullable
    protected Variant[] get(Event event) {
        ModelClass modelClass1 = modelClass.getSingle(event);
        if (modelClass1 != null){

            modelClass1.getAllVariants();
        }


        return null;
    }
}

