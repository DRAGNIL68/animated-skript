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


public class ExprGetCurrentAnimationsCurrentFrameTime extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprGetCurrentAnimationsCurrentFrameTime.class, Integer.class, ExpressionType.COMBINED, "[animated-skript] [get] current-animation %activemodelanimation%('s|s) (current frame time|current-frame)");
    }

    private Expression<Animation> animationExpression;

    @Override
    public Class<? extends Integer> getReturnType() {
        //1
        return Integer.class;
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
    protected Integer[] get(Event event) {
        Animation animation = animationExpression.getSingle(event);
        if (animation != null){

            return new Integer[]{animation.currentFrameTime};
        }


        return null;
    }
}

