package net.outmoded.animated_skript.skript.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.AnimatedSkript;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;
import java.util.UUID;


public class ExprGetActiveModelsUuid extends SimpleExpression<UUID> {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprGetActiveModelsUuid.class, UUID.class)
                        .addPatterns(
                                "[animated-skript] [get] %activemodel%('s|s) uuid"
                        )
                        .supplier(ExprGetActiveModelsUuid::new)
                        .build());

    }

    private Expression<ModelClass> modelClass; // if true = loaded-models | if false = active-models

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
        modelClass = (Expression<ModelClass>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        ModelClass modelClass1 = modelClass.getSingle(event);

        if (modelClass1 != null){
            return modelClass1.getUuid().toString();

        }

        return "";
    }

    @Override
    @Nullable
    protected UUID[] get(Event event) {
        ModelClass modelClass1 = modelClass.getSingle(event);
        if (modelClass1 != null){
            return new UUID[] {modelClass1.getUuid()};

        }

        return new UUID[] {};
    }
}

