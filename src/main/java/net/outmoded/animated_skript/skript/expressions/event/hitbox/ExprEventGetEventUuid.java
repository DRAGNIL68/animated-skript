package net.outmoded.animated_skript.skript.expressions.event.hitbox;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.events.*;
import net.outmoded.animated_skript.skript.events.EvtOnHitboxAttacked;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.UUID;

public class ExprEventGetEventUuid extends SimpleExpression<UUID> {

    static {
        Skript.registerExpression(ExprEventGetEventUuid.class, UUID.class, ExpressionType.COMBINED, "[animated-skript] event-hitbox uuid");
    }



    @Override
    public Class<? extends UUID> getReturnType() {
        //1
        return UUID.class;
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
        else if (getParser().isCurrentEvent(ActiveModelHitboxInteract.class)){
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
    protected UUID[] get(Event event) {

        // there is a better way to do this, but I don't care.
        if (event instanceof ActiveModelHitboxInteract event1){;
            return new UUID[] {event1.getHitboxUuid()};

        }
        else if (event instanceof ActiveModelHitboxAttack event1){
            return new UUID[] {event1.getHitboxUuid()};

        }
        else {
            return new UUID[] {};
        }



    }

}
