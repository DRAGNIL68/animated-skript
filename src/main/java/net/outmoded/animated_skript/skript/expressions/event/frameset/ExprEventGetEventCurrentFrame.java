package net.outmoded.animated_skript.skript.expressions.event.frameset;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.events.ModelFrameSetAnimationEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprEventGetEventCurrentFrame extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprEventGetEventCurrentFrame.class, Integer.class, ExpressionType.COMBINED, "[animated-skript] event-current frame");
    }



    @Override
    public Class<? extends Integer> getReturnType() {
        //1
        return Integer.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {

        if (getParser().isCurrentEvent(ModelFrameSetAnimationEvent.class)){
            return true;
        }

        return false;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    @Nullable
    protected Integer[] get(Event event) {

        // there is a better way to do this, but I don't care.
        if (event instanceof ModelFrameSetAnimationEvent event1){
            return new Integer[] {event1.getOldFrameTime()};

        }
        else {
            return new Integer[] {};
        }



    }

}
