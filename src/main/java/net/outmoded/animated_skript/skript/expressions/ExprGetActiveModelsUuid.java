package net.outmoded.animated_skript.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.UUID;


public class ExprGetActiveModelsUuid extends SimpleExpression<UUID> {

    static {
        Skript.registerExpression(ExprGetActiveModelsUuid.class, UUID.class, ExpressionType.COMBINED, "[animated-skript] [get] %activemodel%('s|s) uuid");
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
        //4
        return "";
    }

    @Override
    @Nullable
    protected UUID[] get(Event event) {
        ModelClass modelClass1 = modelClass.getSingle(event);
        if (modelClass1 != null){
            return new UUID[] {modelClass1.getUuid()};
        }


        return null;
    }
}

