package net.outmoded.modelengine.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;

import ch.njol.skript.registrations.EventValues;
import net.outmoded.modelengine.events.OnModelSpawnedEvent;
import net.outmoded.modelengine.models.ModelClass;
import org.bukkit.event.Event;


import javax.annotation.Nullable;

public class OnModelSpawned extends SkriptEvent {

    static {
        Skript.registerEvent("Model Spawned", OnModelSpawned.class, OnModelSpawnedEvent.class, "[animated-skript] model spawned");

    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {

        return true;
    }

    @Override
    public boolean check(Event e) {
        OnModelSpawnedEvent event = (OnModelSpawnedEvent) e;
        //event.

        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "";
    }

}
