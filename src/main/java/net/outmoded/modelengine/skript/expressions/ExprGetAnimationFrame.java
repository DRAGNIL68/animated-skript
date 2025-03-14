package net.outmoded.modelengine.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.modelengine.models.ModelClass;
import net.outmoded.modelengine.models.ModelManager;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

import static org.bukkit.Bukkit.getServer;


public class ExprGetAnimationFrame extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprGetAnimationFrame.class, Integer.class, ExpressionType.COMBINED, "[animated-skript] [get] %activemodel%('s|s) current animation (:frame|max frame)");
    }
    private Expression<ModelClass> modelClass;
    private boolean type; // if true = frame, false = max frame

    @Override
    public Class<? extends Integer> getReturnType() {
        //1
        return Integer.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return false;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        type = parser.hasTag("frame");
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return null;
    }

    @Override
    @Nullable
    protected Integer[] get(Event event) {
        ModelClass modelClass1 = modelClass.getSingle(event);
        if (modelClass1 != null){
            if (!modelClass1.hasCurrentAnimation())
                return null;

            if (type)
                return new Integer[] {modelClass1.getCurrentAnimationsFrame()};
            else
                return new Integer[] {modelClass1.getCurrentAnimationMaxFrame()};

        }

        return null;

    }
}

