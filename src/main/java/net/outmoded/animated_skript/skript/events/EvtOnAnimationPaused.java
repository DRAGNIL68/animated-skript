package net.outmoded.animated_skript.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import net.outmoded.animated_skript.events.ModelPauseAnimationEvent;
import net.outmoded.animated_skript.events.ModelPauseAnimationEvent;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.new_stuff.Animation;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EvtOnAnimationPaused extends SkriptEvent {

    static {
        Skript.registerEvent("Animation Paused", EvtOnAnimationPaused.class, ModelPauseAnimationEvent.class, "[animated-skript] animation paused");
        EventValues.registerEventValue(ModelPauseAnimationEvent.class, ModelClass.class, ModelPauseAnimationEvent::getActiveModel);
        EventValues.registerEventValue(ModelPauseAnimationEvent.class, Animation.class, ModelPauseAnimationEvent::getAnimation);

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
