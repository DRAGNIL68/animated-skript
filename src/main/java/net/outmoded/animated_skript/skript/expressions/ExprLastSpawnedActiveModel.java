package net.outmoded.animated_skript.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.skript.SkriptManager;
import org.bukkit.event.Event;

import javax.annotation.Nullable;


public class ExprLastSpawnedActiveModel extends SimpleExpression<ModelClass> {

    static {
        Skript.registerExpression(ExprLastSpawnedActiveModel.class, ModelClass.class, ExpressionType.COMBINED, "[animated-skript] [get] last spawned active-model");
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

