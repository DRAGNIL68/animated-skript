package net.outmoded.animated_skript.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import io.papermc.paper.event.player.PlayerFailMoveEvent;
import net.outmoded.animated_skript.events.ActiveModelHitboxAttack;
import net.outmoded.animated_skript.events.ActiveModelHitboxInteract;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.skriptlang.skript.bukkit.registration.BukkitSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;
import java.util.UUID;

public class EvtOnHitboxInteract extends SkriptEvent {

    public static void register(SyntaxRegistry registry) {
        registry.register(BukkitSyntaxInfos.Event.KEY, BukkitSyntaxInfos.Event.builder(EvtOnHitboxInteract.class, "Hitbox Interact")
                .supplier(EvtOnHitboxInteract::new)
                .addEvent(ActiveModelHitboxInteract.class)
                .addPatterns("[animated-skript] hitbox interact")
                .build());

        EventValues.registerEventValue(ActiveModelHitboxInteract.class, ModelClass.class, ActiveModelHitboxInteract::getActiveModel);
        EventValues.registerEventValue(ActiveModelHitboxInteract.class, Player.class, ActiveModelHitboxInteract::getPlayer);
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
