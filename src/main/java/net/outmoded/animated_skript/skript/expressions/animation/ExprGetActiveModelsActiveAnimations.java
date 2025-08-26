package net.outmoded.animated_skript.skript.expressions.animation;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.nodes.ActiveAnimation;
import net.outmoded.animated_skript.models.nodes.Animation;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.ArrayList;


public class ExprGetActiveModelsActiveAnimations extends SimpleExpression<Animation> {

    static {
        Skript.registerExpression(ExprGetActiveModelsActiveAnimations.class, Animation.class, ExpressionType.COMBINED, "[animated-skript] [get] [all|the] %activemodel%('s|s) active-animations");
    }

    private Expression<ModelClass> modelClass;

    @Override
    public Class<? extends Animation> getReturnType() {
        //1
        return Animation.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return false;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        modelClass = (Expression<ModelClass>) exprs[0];


        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return "";
    }

    @Override
    @Nullable
    protected Animation[] get(Event event) {
        ModelClass modelClass1 = modelClass.getSingle(event);
        if (modelClass1 != null){
            ArrayList<Animation> animations = new ArrayList<>();

            for (ActiveAnimation animation : modelClass1.getActiveAnimations()){
                // this is a mega hacky fix
                Animation newAnimation = new Animation();
                newAnimation.name = animation.animationReference.name;
                newAnimation.loopDelay = animation.animationReference.loopDelay;
                newAnimation.uuid = animation.animationReference.uuid;
                newAnimation.maxFrameTime = animation.animationReference.maxFrameTime;
                newAnimation.loopMode = animation.animationReference.loopMode;
                newAnimation.currentFrameTime = animation.currentFrameTime;
                newAnimation.isPaused = animation.isPaused;

                animations.add(newAnimation);
            }
            return animations.toArray(new Animation[0]);
        }


        return null;
    }
}

