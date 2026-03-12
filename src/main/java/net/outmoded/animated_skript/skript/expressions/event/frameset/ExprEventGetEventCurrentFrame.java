package net.outmoded.animated_skript.skript.expressions.event.frameset;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.events.ModelAnimationFrameSetEvent;
import net.outmoded.animated_skript.skript.expressions.node.ExprGetNodeUuid;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;
import java.util.UUID;

public class ExprEventGetEventCurrentFrame extends SimpleExpression<Integer> {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprEventGetEventCurrentFrame.class, Integer.class)
                        .addPatterns(
                                "[animated-skript] event-current frame"
                        )
                        .supplier(ExprEventGetEventCurrentFrame::new)
                        .build());

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

        if (getParser().isCurrentEvent(ModelAnimationFrameSetEvent.class)){
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
        if (event instanceof ModelAnimationFrameSetEvent event1){
            return new Integer[] {event1.getOldFrameTime()};

        }
        else {
            return new Integer[] {};
        }



    }

}
