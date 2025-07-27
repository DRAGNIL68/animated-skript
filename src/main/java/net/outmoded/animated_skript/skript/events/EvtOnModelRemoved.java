package net.outmoded.animated_skript.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import net.outmoded.animated_skript.events.ModelRemovedEvent;
import net.outmoded.animated_skript.events.ModelRemovedEvent;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EvtOnModelRemoved extends SkriptEvent {

    static {
        Skript.registerEvent("Model Removed", EvtOnModelRemoved.class, ModelRemovedEvent.class, "[animated-skript] model spawned");

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
