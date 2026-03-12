package net.outmoded.animated_skript.skript.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.AnimatedSkript;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.skript.SkriptManager;
import org.bukkit.event.Event;

import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;


public class ExprLastSpawnedActiveModel extends SimpleExpression<ModelClass> {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprLastSpawnedActiveModel.class, ModelClass.class)
                        .addPatterns(
                                "[animated-skript] [get] last spawned active-model"
                        )
                        .supplier(ExprLastSpawnedActiveModel::new)
                        .build());

    }


    @Override
    public Class<? extends ModelClass> getReturnType() {
        //1
        return ModelClass.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return "";
    }

    @Override
    @Nullable
    protected ModelClass[] get(Event event) {
        return new ModelClass[] {SkriptManager.getLastSpawnedModelClass()};
    }
}

