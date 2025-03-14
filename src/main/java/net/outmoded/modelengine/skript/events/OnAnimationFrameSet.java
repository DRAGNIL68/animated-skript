package net.outmoded.modelengine.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import net.outmoded.modelengine.events.OnModelFrameSetAnimationEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class OnAnimationFrameSet extends SkriptEvent {

    static {
        Skript.registerEvent("Animation Paused", OnAnimationFrameSet.class, OnModelFrameSetAnimationEvent.class, "[animated-skript] animation frame set");


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
