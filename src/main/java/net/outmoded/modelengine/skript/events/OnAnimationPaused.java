package net.outmoded.modelengine.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import net.outmoded.modelengine.events.OnModelEndAnimationEvent;
import net.outmoded.modelengine.events.OnModelPauseAnimationEvent;
import net.outmoded.modelengine.events.OnModelRemovedEvent;
import net.outmoded.modelengine.models.ModelClass;
import net.outmoded.modelengine.models.ModelManager;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class OnAnimationPaused extends SkriptEvent {

    static {
        Skript.registerEvent("Animation Paused", OnAnimationPaused.class, OnModelPauseAnimationEvent.class, "[animated-skript] animation paused");


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
