package net.outmoded.animated_skript.skript.expressions.event.hitbox;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.events.ActiveModelHitboxAttack;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nullable;

public class ExprEventGetEventDamageCause extends SimpleExpression<EntityDamageEvent.DamageCause> {

    static {
        Skript.registerExpression(ExprEventGetEventDamageCause.class, EntityDamageEvent.DamageCause.class, ExpressionType.COMBINED, "[animated-skript] event-damage cause");
    }



    @Override
    public Class<? extends EntityDamageEvent.DamageCause> getReturnType() {
        //1
        return EntityDamageEvent.DamageCause.class;
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
    protected EntityDamageEvent.DamageCause[] get(Event event) {

        // there is a better way to do this, but I don't care.
        if (event instanceof ActiveModelHitboxAttack event1){
            return new EntityDamageEvent.DamageCause[] {event1.getDamageCause()};

        }
        else {
            return new EntityDamageEvent.DamageCause[] {};
        }



    }

}
