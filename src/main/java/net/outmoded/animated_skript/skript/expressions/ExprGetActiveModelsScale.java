package net.outmoded.animated_skript.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.ModelManager;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.UUID;

import static net.outmoded.animated_skript.Config.debugMode;
import static org.bukkit.Bukkit.getServer;

public class ExprGetActiveModelsScale extends SimpleExpression<Double> {

    static {
        Skript.registerExpression(ExprGetActiveModelsScale.class, Double.class, ExpressionType.COMBINED, "[animated-skript] [get] %activemodel%('s|s) scale");
    }

    private Expression<ModelClass> modelClassExpression; // if true = loaded-models | if false = active-models

    @Override
    public Class<? extends Double> getReturnType() {
        //1
        return Double.class;
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
    protected Double[] get(Event event) {
        ModelClass modelClass = modelClassExpression.getSingle(event);
        return new Double[]{modelClass.getScale().doubleValue()};


    }
}

