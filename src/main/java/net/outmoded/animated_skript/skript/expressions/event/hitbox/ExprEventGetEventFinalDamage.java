package net.outmoded.animated_skript.skript.expressions.event.hitbox;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.events.ActiveModelHitboxAttack;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprEventGetEventFinalDamage extends SimpleExpression<Double> {

    static {
        Skript.registerExpression(ExprEventGetEventFinalDamage.class, Double.class, ExpressionType.COMBINED, "[animated-skript] event-final damage");
    }



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
    protected Double[] get(Event event) {

        // there is a better way to do this, but I don't care.
        if (event instanceof ActiveModelHitboxAttack event1){
            return new Double[] {event1.getFinalDamage()};

        }
        else {
            return new Double[] {};
        }



    }

}
