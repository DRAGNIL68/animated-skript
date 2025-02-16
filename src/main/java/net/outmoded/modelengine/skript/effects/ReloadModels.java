package net.outmoded.modelengine.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.modelengine.models.ModelManager;
import net.outmoded.modelengine.models.ModelPersistence;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ReloadModels extends Effect {

    static {
        Skript.registerEffect(ReloadModels.class, "[animated-skript] reload (model-configs|active-models)");
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
            ModelManager.loadModelConfigs();
            ModelManager.reloadAllActiveModels();
            ModelPersistence.saveModels();
        }
        else{
            ModelManager.reloadAllActiveModels();
            ModelPersistence.saveModels();
        }
    }
}
