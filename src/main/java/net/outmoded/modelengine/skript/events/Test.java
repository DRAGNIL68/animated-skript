package net.outmoded.modelengine.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;

import ch.njol.skript.registrations.EventValues;
import net.outmoded.modelengine.events.OnModelSpawnedEvent;
import org.bukkit.event.Event;


import javax.annotation.Nullable;

public class Test extends SkriptEvent {

    static {
        Skript.registerEvent("Model Spawn", Test.class, OnModelSpawnedEvent.class, "[animated-skript] on model spawn");
        EventValues.registerEventValue(OnModelSpawnedEvent.class, String.class, event -> event.getUuid().toString());

    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return false;
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
