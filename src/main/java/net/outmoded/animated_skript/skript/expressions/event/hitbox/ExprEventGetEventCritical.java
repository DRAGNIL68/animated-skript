package net.outmoded.animated_skript.skript.expressions.event.hitbox;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.events.ActiveModelHitboxAttack;
import net.outmoded.animated_skript.events.ActiveModelHitboxInteract;
import org.bukkit.event.Event;

import javax.annotation.Nullable;


public class ExprEventGetEventCritical extends SimpleExpression<Boolean> {

    static {
        Skript.registerExpression(ExprEventGetEventCritical.class, Boolean.class, ExpressionType.COMBINED, "[animated-skript] event-is critical");
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
