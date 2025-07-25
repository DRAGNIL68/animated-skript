package net.outmoded.animated_skript.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;


public class ExprGetActiveModelsLocation extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprGetActiveModelsLocation.class, Location.class, ExpressionType.COMBINED, "[animated-skript] [get] location of %activemodel%");
    }
    private Location location;
    private Expression<ModelClass> modelClass; // if true = loaded-models | if false = active-models

    @Override
    public Class<? extends Location> getReturnType() {
        //1
        return Location.class;
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
    protected Location[] get(Event event) {
        ModelClass modelClass1 = modelClass.getSingle(event);
        if (modelClass1 != null){
            location = modelClass1.getOriginLocation();

        }

        if (location != null){
            return new Location[] {location};

        }
        return null;
    }
}

