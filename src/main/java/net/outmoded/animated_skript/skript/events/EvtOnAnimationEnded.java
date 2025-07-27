package net.outmoded.animated_skript.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import net.outmoded.animated_skript.events.ModelEndAnimationEvent;
import net.outmoded.animated_skript.events.ModelEndAnimationEvent;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.new_stuff.Animation;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EvtOnAnimationEnded extends SkriptEvent {

    static {
        Skript.registerEvent("Animation Ended", EvtOnAnimationEnded.class, ModelEndAnimationEvent.class, "[animated-skript] animation ended");
        EventValues.registerEventValue(ModelEndAnimationEvent.class, ModelClass.class, ModelEndAnimationEvent::getActiveModel);
        EventValues.registerEventValue(ModelEndAnimationEvent.class, Animation.class, ModelEndAnimationEvent::getAnimation);

    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {

        return true;
    }

    @Override
    public boolean check(Event e) {

        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "";
    }

}
