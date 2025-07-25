package net.outmoded.animated_skript.skript.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.Config;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.ModelManager;
import net.outmoded.animated_skript.models.new_stuff.Variant;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;


public class CondHasVariant extends Condition {

    static {
        Skript.registerCondition(CondHasVariant.class, "[animated-skript] %activemodel% has [the] variant %activemodelvariant%");
    }

    private Expression<Variant> variantExpression;
    private Expression<ModelClass> modelClassExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        modelClassExpression = (Expression<ModelClass>) expressions[0];
        variantExpression = (Expression<Variant>) expressions[1];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    public boolean check(Event event) {
        Variant variant = variantExpression.getSingle(event);
        ModelClass modelClass = modelClassExpression.getSingle(event);

        if (modelClass == null)
            return false;

        return modelClass.hasVariant(variant); // hasVariant() has null checking



    }



}

