package net.outmoded.animated_skript.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import net.outmoded.animated_skript.events.ModelAnimationFrameSetEvent;
import net.outmoded.animated_skript.events.ModelAnimationPauseEvent;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.nodes.Animation;
import org.bukkit.event.Event;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;

public class EvtOnAnimationPaused extends SkriptEvent {

    public static void register(SyntaxRegistry registry) {
        registry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtOnAnimationPaused.class, "Animation Paused")
                .supplier(EvtOnAnimationPaused::new)
                .addEvent(ModelAnimationPauseEvent.class)
                .addPatterns("[animated-skript] animation paused")
                .build());

        EventValues.registerEventValue(ModelAnimationPauseEvent.class, ModelClass.class, ModelAnimationPauseEvent::getActiveModel);
        EventValues.registerEventValue(ModelAnimationPauseEvent.class, Animation.class, ModelAnimationPauseEvent::getAnimation);
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
