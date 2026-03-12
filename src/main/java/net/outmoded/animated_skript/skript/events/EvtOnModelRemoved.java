package net.outmoded.animated_skript.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import net.outmoded.animated_skript.events.ActiveModelHitboxInteract;
import net.outmoded.animated_skript.events.ModelRemovedEvent;
import net.outmoded.animated_skript.events.ModelRemovedEvent;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;

public class EvtOnModelRemoved extends SkriptEvent {

    public static void register(SyntaxRegistry registry) {
        registry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtOnModelRemoved.class, "Model Removed")
                .supplier(EvtOnModelRemoved::new)
                .addEvent(ModelRemovedEvent.class)
                .addPatterns("[animated-skript] model spawned")
                .build());

        EventValues.registerEventValue(ModelRemovedEvent.class, ModelClass.class, ModelRemovedEvent::getActiveModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {

        return true;
    }

    @Override
    public boolean check(Event e) {
        //ModelSpawnedEvent event = (ModelSpawnedEvent) e;
        //event.

        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "";
    }

}
