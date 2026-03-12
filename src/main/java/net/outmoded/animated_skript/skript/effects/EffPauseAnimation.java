package net.outmoded.animated_skript.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;

public class EffPauseAnimation extends Effect {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffPauseAnimation.class)
                        .addPatterns(
                                "[animated-skript] (:pause||unpause||resume) the active animation %string% of [the] %activemodel%"
                        )
                        .supplier(EffPauseAnimation::new)
                        .build());

    }

    private Expression<ModelClass> activeModel;
    private Expression<String> stringExpression;

    private Boolean isPlay = false; // if true = pause, if false = unpause/resume
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[1];
        stringExpression = (Expression<String>) expressions[0];
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
        String string = stringExpression.getSingle(event);
        if (modelClass != null && string != null){

            if (isPlay){
                modelClass.pauseActiveAnimation(string, true);

            }
            else {
                modelClass.pauseActiveAnimation(string, false);
            }
        }



    }
}
