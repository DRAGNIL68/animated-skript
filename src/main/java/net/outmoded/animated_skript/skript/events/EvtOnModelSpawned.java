package net.outmoded.animated_skript.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;

import ch.njol.skript.registrations.EventValues;
import net.outmoded.animated_skript.events.ModelSpawnedEvent;
import net.outmoded.animated_skript.events.ModelSpawnedEvent;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;


import javax.annotation.Nullable;

public class EvtOnModelSpawned extends SkriptEvent {

    static {
        Skript.registerEvent("Model Spawned", EvtOnModelSpawned.class, ModelSpawnedEvent.class, "[animated-skript] model spawned");
        EventValues.registerEventValue(ModelSpawnedEvent.class, ModelClass.class, ModelSpawnedEvent::getActiveModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {

        return true;
    }

    @Override
    public boolean check(Event e) {
        ModelSpawnedEvent event = (ModelSpawnedEvent) e;
        //event.

        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "";
    }

}
