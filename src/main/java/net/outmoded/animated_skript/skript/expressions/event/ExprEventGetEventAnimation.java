package net.outmoded.animated_skript.skript.expressions.event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.events.*;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprEventGetEventAnimation extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprEventGetEventAnimation.class, String.class, ExpressionType.COMBINED, "[animated-skript] event-animation");
    }



    @Override
    public Class<? extends String> getReturnType() {
        //1
        return String.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {

        if (getParser().isCurrentEvent(ModelStartAnimationEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(ModelEndAnimationEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(ModelPauseAnimationEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(ModelUnpauseAnimationEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(ModelFrameSetAnimationEvent.class)){
            return true;
        }


        return false;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return null;
    }

    @Override
    @Nullable
    protected String[] get(Event event) {

        // there is a better way to do this, but I don't care.
        if (event instanceof ModelStartAnimationEvent event1){;
            return new String[] {event1.getAnimation()};

        }
        else if (event instanceof ModelEndAnimationEvent event1){
            return new String[] {event1.getAnimation()};

        }
        else if (event instanceof ModelUnpauseAnimationEvent event1){
            return new String[] {event1.getAnimation()};

        }
        else if (event instanceof ModelPauseAnimationEvent event1){
            return new String[] {event1.getAnimation()};

        }
        else if (event instanceof ModelFrameSetAnimationEvent event1){
            return new String[] {event1.getAnimation()};

        }
        else {
            return null;
        }



    }

}
