package net.outmoded.animated_skript.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.Config;
import net.outmoded.animated_skript.models.ModelManager;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;

import java.util.UUID;

import static org.bukkit.Bukkit.getServer;


public class CondLoadedModelExists extends Condition {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.CONDITION,
                SyntaxInfo.builder(CondLoadedModelExists.class)
                        .addPatterns(
                                "[animated-skript] loaded-model %string% exists"
                        )
                        .supplier(CondLoadedModelExists::new)
                        .build());

    }

    private boolean type; // if true = loaded-models | if false = active-models
    private Expression<String> text;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        text = (Expression<String>) expressions[0];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    public boolean check(Event event) {
        return ModelManager.getInstance().loadedModelExists(text.toString());

    }



}

