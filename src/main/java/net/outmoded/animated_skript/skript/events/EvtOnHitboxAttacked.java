package net.outmoded.animated_skript.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import net.outmoded.animated_skript.events.ActiveModelHitboxInteract;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.UUID;

public class EvtOnHitboxAttacked extends SkriptEvent {

    static {
        Skript.registerEvent("On Hitbox Attacked", EvtOnHitboxAttacked.class, ActiveModelHitboxInteract.class, "[animated-skript] hitbox attacked");

        EventValues.registerEventValue(ActiveModelHitboxInteract.class, ModelClass.class, ActiveModelHitboxInteract::getActiveModel);





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
