package net.outmoded.animated_skript.skript.expressions.animation;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.new_stuff.Animation;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.UUID;


public class ExprGetAnimationUuid extends SimpleExpression<UUID> {

    static {
        Skript.registerExpression(ExprGetAnimationUuid.class, UUID.class, ExpressionType.COMBINED, "[animated-skript] [get] [the] %activemodelanimation%('s|s) uuid");
    }

    private Expression<Animation> animationExpression;

    @Override
    public Class<? extends UUID> getReturnType() {
        //1
        return UUID.class;
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
    protected UUID[] get(Event event) {

        Animation animation = animationExpression.getSingle(event);


        if (animation != null && animation.uuid != null){
            return new UUID[] {animation.uuid};
        }

        return null;

    }
}

