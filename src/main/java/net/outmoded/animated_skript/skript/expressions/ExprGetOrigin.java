package net.outmoded.animated_skript.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;


public class ExprGetOrigin extends SimpleExpression<ItemDisplay> {


    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EXPRESSION,
                SyntaxInfo.Expression.builder(ExprGetOrigin.class, ItemDisplay.class)
                        .addPatterns(
                                "[animated-skript] [get] [the] %activemodel%('s|s) origin"
                        )
                        .supplier(ExprGetOrigin::new)
                        .build());

    }

    private Expression<ModelClass> modelClassExpression;

    @Override
    public Class<? extends ItemDisplay> getReturnType() {
        //1
        return ItemDisplay.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        modelClassExpression = (Expression<ModelClass>) exprs[0];

        if (modelClassExpression == null)
            return false;

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return "";
    }

    @Override
    @Nullable
    protected ItemDisplay[] get(Event event) {

        if (modelClassExpression.getSingle(event) != null){
            return new ItemDisplay[] {modelClassExpression.getSingle(event).getOrigin()};
        }

        return null;

    }
}

