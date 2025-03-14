package net.outmoded.modelengine.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import net.outmoded.modelengine.events.OnModelEndAnimationEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class OnAnimationEnded extends SkriptEvent {

    static {
        Skript.registerEvent("Animation Paused", OnAnimationEnded.class, OnModelEndAnimationEvent.class, "[animated-skript] animation ended");


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
