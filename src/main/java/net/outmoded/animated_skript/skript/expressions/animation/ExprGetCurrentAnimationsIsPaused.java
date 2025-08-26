package net.outmoded.animated_skript.skript.expressions.animation;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.nodes.Animation;
import org.bukkit.event.Event;

import javax.annotation.Nullable;


public class ExprGetCurrentAnimationsIsPaused extends SimpleExpression<Boolean> {
    
    static {
        Skript.registerExpression(ExprGetCurrentAnimationsIsPaused.class, Boolean.class, ExpressionType.COMBINED, "[animated-skript] [get] current-animation %activemodelanimation%('s|s) paused state");
    }

    private Expression<Animation> animationExpression;

    @Override
    public Class<? extends Boolean> getReturnType() {
        //1
        return Boolean.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return false;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        animationExpression = (Expression<Animation>) exprs[0];
        

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return "";
    }

    @Override
    @Nullable
    protected Boolean[] get(Event event) {
        Animation animation = animationExpression.getSingle(event);
        if (animation != null){
            
            return new Boolean[]{animation.isPaused};
        }


        return null;
    }
}

