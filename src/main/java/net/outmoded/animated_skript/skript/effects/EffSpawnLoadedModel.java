package net.outmoded.animated_skript.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelManager;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;

public class EffSpawnLoadedModel extends Effect {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffSpawnLoadedModel.class)
                        .addPatterns(
                                "[animated-skript] spawn [the] loaded-model %string% at [the] %location%"
                        )
                        .supplier(EffSpawnLoadedModel::new)
                        .build());

    }

    private Expression<String> string;
    private Expression<Location> locationExpression;
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        string = (Expression<String>) expressions[0];
        locationExpression = (Expression<Location>) expressions[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        if (string.getSingle(event) != null && locationExpression != null){
            ModelManager.getInstance().spawnNewModel(string.getSingle(event), locationExpression.getSingle(event));


        }
    }
}
