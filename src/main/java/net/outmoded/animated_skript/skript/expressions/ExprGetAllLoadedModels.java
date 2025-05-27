package net.outmoded.animated_skript.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelManager;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprGetAllLoadedModels extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprGetAllLoadedModels.class, String.class, ExpressionType.COMBINED, "[animated-skript] [get] all [the] loaded-models");
    }



    @Override
    public Class<? extends String> getReturnType() {
        //1
        return String.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return false;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return null;
    }

    @Override
    @Nullable
    protected String[] get(Event event) {

        return ModelManager.getInstance().getAllLoadedModelsKeys();

    }
}

