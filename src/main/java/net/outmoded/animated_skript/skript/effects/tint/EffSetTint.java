package net.outmoded.animated_skript.skript.effects.tint;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffSetTint extends Effect {
    // Persistence
    static {
        Skript.registerEffect(EffSetTint.class, "[animated-skript] set %activemodel%('s|s) tint [colo[u]r] to %color%");
    }

    private Expression<ModelClass> activeModel;
    private Expression<ch.njol.skript.util.Color> colorExpression;

    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[0];
        colorExpression = (Expression<ch.njol.skript.util.Color>) expressions[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);
        ch.njol.skript.util.Color color = colorExpression.getSingle(event);
        if (modelClass != null && color != null){
            modelClass.setTint(color.asBukkitColor());

        }



    }
}
