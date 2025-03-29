package net.outmoded.modelengine.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.modelengine.models.ModelClass;
import net.outmoded.modelengine.models.ModelManager;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.UUID;



public class ExprGetOrigin extends SimpleExpression<ItemDisplay> {

    static {
        Skript.registerExpression(ExprGetOrigin.class, ItemDisplay.class, ExpressionType.COMBINED, "[animated-skript] [get] [the] %activemodel%('s|s) origin");
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
        return null;
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

