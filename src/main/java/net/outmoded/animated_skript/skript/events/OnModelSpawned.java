package net.outmoded.animated_skript.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;

import net.outmoded.animated_skript.events.ModelSpawnedEvent;
import org.bukkit.event.Event;


import javax.annotation.Nullable;

public class OnModelSpawned extends SkriptEvent {

    static {
        Skript.registerEvent("Model Spawned", OnModelSpawned.class, ModelSpawnedEvent.class, "[animated-skript] model spawned");

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
