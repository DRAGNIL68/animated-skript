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


public class ExprGetAnimationLoopDelay extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprGetAnimationLoopDelay.class, Integer.class, ExpressionType.COMBINED, "[animated-skript] [get] [the] %activemodelanimation%('s|s) loopdelay");
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
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        animationExpression = (Expression<Animation>) exprs[0];

        if (animationExpression == null)
            return false;

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return null;
    }

    @Override
    @Nullable
    protected Integer[] get(Event event) {

        Animation animation = animationExpression.getSingle(event);


        if (animation != null && animation.loopDelay != null){
            return new Integer[] {animation.loopDelay};
        }

        return null;

    }
}

