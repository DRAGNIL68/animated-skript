package net.outmoded.animated_skript.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class AnimationControl extends Effect {

    static {
        Skript.registerEffect(AnimationControl.class, "[animated-skript] (:play|stop) animation %string% of %activemodel%");
    }

    private Expression<ModelClass> activeModel;
    private Expression<String> string;

    private Boolean isPlay = false;
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[1];
        string = (Expression<String>) expressions[0];
        isPlay = parser.hasTag("play");
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);
        String string1 = string.getSingle(event);
        if (modelClass != null && string1 != null){

            if (modelClass.hasAnimation(string1)){

                if (isPlay){
                    modelClass.playAnimation(string1);

                }
                else{
                    modelClass.resetAnimation();

                }

            }


        }


    }
}
