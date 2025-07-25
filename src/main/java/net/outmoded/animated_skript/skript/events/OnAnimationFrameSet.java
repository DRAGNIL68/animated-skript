package net.outmoded.animated_skript.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import net.outmoded.animated_skript.events.ModelFrameSetAnimationEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class OnAnimationFrameSet extends SkriptEvent {

    static {
        Skript.registerEvent("Animation Frame Set", OnAnimationFrameSet.class, ModelFrameSetAnimationEvent.class, "[animated-skript] animation frame set");


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
