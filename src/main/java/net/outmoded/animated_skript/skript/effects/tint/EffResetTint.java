package net.outmoded.animated_skript.skript.effects.tint;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.Color;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffResetTint extends Effect {
    // Persistence
    static {
        Skript.registerEffect(EffResetTint.class, "[animated-skript] reset %activemodel%('s|s) tint colo[u]r");
    }

    private Expression<ModelClass> activeModel;

    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);
        if (modelClass != null){
            modelClass.setTint(Color.WHITE);

        }



    }
}
