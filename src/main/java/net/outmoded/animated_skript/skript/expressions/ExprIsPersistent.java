package net.outmoded.animated_skript.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;

public class ExprIsPersistent extends SimpleExpression<Boolean> { // TODO: needs moving to a condition, not sure why it's an expression

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprIsPersistent.class, Boolean.class)
                        .addPatterns(
                                "[animated-skript] [get] %activemodel%('s|s) persistence"
                        )
                        .supplier(ExprIsPersistent::new)
                        .build());

    }

    private Expression<ModelClass> activeModel;

    @Override
    public Class<? extends Boolean> getReturnType() {
        //1
        return Boolean.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) exprs[0];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return "";
    }

    @Override
    protected Boolean[] get(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);
        if (modelClass == null){
            return new Boolean[] {false};

        }


        return new Boolean[] {modelClass.getPersistence()};
    }
}

