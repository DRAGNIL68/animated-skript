package net.outmoded.animated_skript.skript.expressions.event.hitbox;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.events.ActiveModelHitboxAttack;
import net.outmoded.animated_skript.events.ActiveModelHitboxInteract;
import net.outmoded.animated_skript.skript.expressions.event.frameset.ExprEventGetEventNewFrame;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;


public class ExprEventGetEventCritical extends SimpleExpression<Boolean> {


    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprEventGetEventCritical.class, Boolean.class)
                        .addPatterns(
                                "[animated-skript] event-is critical"
                        )
                        .supplier(ExprEventGetEventCritical::new)
                        .build());

    }



    @Override
    public Class<? extends Boolean> getReturnType() {
        //1
        return Boolean.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {

        if (getParser().isCurrentEvent(ActiveModelHitboxAttack.class)){
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
    protected Boolean[] get(Event event) {

        // there is a better way to do this, but I don't care.
        if (event instanceof ActiveModelHitboxAttack event1){
            return new Boolean[] {event1.isCritical()};

        }
        else {
            return new Boolean[] {};
        }



    }

}
