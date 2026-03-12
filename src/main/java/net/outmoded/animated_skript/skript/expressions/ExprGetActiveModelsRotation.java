package net.outmoded.animated_skript.skript.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;
import org.joml.Quaternionf;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;

public class ExprGetActiveModelsRotation extends SimpleExpression<Quaternionf> {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprGetActiveModelsRotation.class, Quaternionf.class)
                        .addPatterns(
                                "[animated-skript] [get] %activemodel%('s|s) rotation"
                        )
                        .supplier(ExprGetActiveModelsRotation::new)
                        .build());

    }

    private Expression<ModelClass> modelClassExpression; // if true = loaded-models | if false = active-models

    @Override
    public Class<? extends Quaternionf> getReturnType() {
        //1
        return Quaternionf.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        modelClassExpression = (Expression<ModelClass>) exprs[0];

        return true;

    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return "";
    }

    @Override
    @Nullable
    protected Quaternionf[] get(Event event) {
        ModelClass modelClass = modelClassExpression.getSingle(event);

        if (modelClass != null){
            return new Quaternionf[]{modelClass.getRotation()};

        }



        return new Quaternionf[]{};
    }
}

