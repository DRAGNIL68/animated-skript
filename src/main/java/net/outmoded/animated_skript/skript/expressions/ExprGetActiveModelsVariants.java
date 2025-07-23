package net.outmoded.animated_skript.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.new_stuff.Variant;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.Arrays;


public class ExprGetActiveModelsVariants extends SimpleExpression<Variant> {

    static {
        Skript.registerExpression(ExprGetActiveModelsVariants.class, Variant.class, ExpressionType.COMBINED, "[animated-skript] get all %activemodel%('s|s) variants");
    }

    private Expression<ModelClass> modelClass;

    @Override
    public Class<? extends Variant> getReturnType() {
        //1
        return Variant.class;
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
        return null;
    }

    @Override
    @Nullable
    protected Variant[] get(Event event) {
        ModelClass modelClass1 = modelClass.getSingle(event);
        if (modelClass1 != null){

            modelClass1.getAllVariants();
        }


        return null;
    }
}

