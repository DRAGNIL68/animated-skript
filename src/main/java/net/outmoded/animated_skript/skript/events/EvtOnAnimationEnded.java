package net.outmoded.animated_skript.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import net.outmoded.animated_skript.events.ModelAnimationEndEvent;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.nodes.Animation;
import net.outmoded.animated_skript.skript.conditions.CondLoadedModelExists;
import net.outmoded.animated_skript.skript.expressions.animation.ExprGetCurrentAnimationsIsPaused;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;

public class EvtOnAnimationEnded extends SkriptEvent {

    public static void register(SyntaxRegistry registry) {
        registry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtOnAnimationEnded.class, "Animation Ended")
                .supplier(EvtOnAnimationEnded::new)
                .addEvent(ModelAnimationEndEvent.class)
                .addPatterns("[animated-skript] animation ended")
                .build());

        EventValues.registerEventValue(ModelAnimationEndEvent.class, ModelClass.class, ModelAnimationEndEvent::getActiveModel);
        EventValues.registerEventValue(ModelAnimationEndEvent.class, Animation.class, ModelAnimationEndEvent::getAnimation);
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
