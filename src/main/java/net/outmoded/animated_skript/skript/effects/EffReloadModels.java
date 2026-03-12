package net.outmoded.animated_skript.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.AnimatedSkript;
import net.outmoded.animated_skript.models.ModelManager;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;

public class EffReloadModels extends Effect {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffReloadModels.class)
                        .addPatterns(
                                "[animated-skript] reload (model-configs|active-models)"
                        )
                        .supplier(EffReloadModels::new)
                        .build());

    }

    private boolean isModelConfig = false; // true if model-configs, false if active-models
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        isModelConfig = parser.hasTag("model-configs");
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        if (isModelConfig){
            ModelManager.getInstance().loadModelConfigs();
            ModelManager.getInstance().reloadAllActiveModels();
            //ModelPersistence.saveModels();
        }
        else{
            ModelManager.getInstance().reloadAllActiveModels();
            //ModelPersistence.saveModels();
        }
    }
}
