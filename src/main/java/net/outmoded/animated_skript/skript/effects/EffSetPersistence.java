package net.outmoded.animated_skript.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffSetPersistence extends Effect {
    // Persistence
    static {
        Skript.registerEffect(EffSetPersistence.class, "[animated-skript] set %activemodel%('s|s) persistence to %boolean%");
    }

    private Expression<ModelClass> activeModel;
    private Expression<Boolean> booleanExpression;

    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[0];
        booleanExpression = (Expression<Boolean>) expressions[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);
        boolean bool = booleanExpression.getSingle(event);
        if (modelClass != null){

            if (bool){
                modelClass.setPersistence(true);

            }
            else {

                modelClass.setPersistence(false);
            }
        }



    }
}
