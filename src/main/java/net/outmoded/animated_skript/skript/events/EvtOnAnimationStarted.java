package net.outmoded.animated_skript.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import net.outmoded.animated_skript.events.ModelAnimationStartEvent;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.nodes.Animation;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EvtOnAnimationStarted extends SkriptEvent {

    static {
        Skript.registerEvent("Animation Started", EvtOnAnimationStarted.class, ModelAnimationStartEvent.class, "[animated-skript] animation started");

        EventValues.registerEventValue(ModelAnimationStartEvent.class, ModelClass.class, ModelAnimationStartEvent::getActiveModel);
        EventValues.registerEventValue(ModelAnimationStartEvent.class, Animation.class, ModelAnimationStartEvent::getAnimation);

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
