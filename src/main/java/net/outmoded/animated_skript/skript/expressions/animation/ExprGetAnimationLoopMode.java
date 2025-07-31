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


public class ExprGetAnimationLoopMode extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprGetAnimationLoopMode.class, String.class, ExpressionType.COMBINED, "[animated-skript] [get] [the] %activemodelanimation%('s|s) loopmode");
    }

    private Expression<Animation> animationExpression;

    @Override
    public Class<? extends String> getReturnType() {
        //1
        return String.class;
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
    protected String[] get(Event event) {

        Animation animation = animationExpression.getSingle(event);


        if (animation != null && animation.loopMode != null){
            return new String[] {animation.loopMode};
        }

        return null;

    }
}

