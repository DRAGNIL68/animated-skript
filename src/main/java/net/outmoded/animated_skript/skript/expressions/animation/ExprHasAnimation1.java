package net.outmoded.animated_skript.skript.expressions.animation;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.new_stuff.Animation;
import net.outmoded.animated_skript.skript.expressions.ExprLastSpawnedActiveModel;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprHasAnimation1 extends SimpleExpression<Boolean> { // TODO: needs moving to a condition, not sure why it's an expression

    static {
        Skript.registerExpression(ExprLastSpawnedActiveModel.class, ModelClass.class, ExpressionType.COMBINED, "[animated-skript] %activemodel% has animation %activemodelanimation%");
    }

    private Expression<ModelClass> activeModel;
    private Expression<Animation> animationName;

    @Override
    public Class<? extends Boolean> getReturnType() {
        //1
        return Boolean.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) exprs[0];
        animationName = (Expression<Animation>) exprs[1];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return "";
    }

    @Override
    protected Boolean[] get(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);
        Animation animation = animationName.getSingle(event);

        if (animation != null){
            if (modelClass != null){
                return new Boolean[] {modelClass.hasAnimation(animation.name)};

            }

        }
        return new Boolean[] {};
    }
}

