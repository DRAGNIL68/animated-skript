package net.outmoded.animated_skript.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffPauseAnimation extends Effect {

    static {
        Skript.registerEffect(EffPauseAnimation.class, "[animated-skript] (:pause||unpause||resume) animation of [the] %activemodel%");
    }

    private Expression<ModelClass> activeModel;

    private Boolean isPlay = false; // if true = pause, if false = unpause/resume
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[0];
        isPlay = parser.hasTag("pause");
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

            if (isPlay){
                modelClass.pauseCurrentAnimation(true);

            }
            else {

                modelClass.pauseCurrentAnimation(false);
            }
        }



    }
}
