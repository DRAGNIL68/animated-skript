package net.outmoded.animated_skript.skript.expressions.animation;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.nodes.ActiveAnimation;
import net.outmoded.animated_skript.models.nodes.Animation;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;


public class ExprGetAnimationFromActiveModel extends SimpleExpression<Animation> {


    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprGetAnimationFromActiveModel.class, Animation.class)
                        .addPatterns(
                                "[animated-skript] [get] [the] animation %string% from active-model %activemodel%"
                        )
                        .supplier(ExprGetAnimationFromActiveModel::new)
                        .build());

    }

    private Expression<ModelClass> modelClassExpression;
    private Expression<String> stringExpression;

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
        modelClassExpression = (Expression<ModelClass>) exprs[1];
        stringExpression = (Expression<String>) exprs[0];


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
        ModelClass modelClass = modelClassExpression.getSingle(event);
        String string = stringExpression.getSingle(event);

        if (modelClass != null){


            if (string == null){
                return new Animation[] {};
            }
            Animation animation = modelClass.getAnimation(string);
            return new Animation[] {animation};
        }


        return new Animation[] {};
    }
}

