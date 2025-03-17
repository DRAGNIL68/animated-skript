package net.outmoded.modelengine.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.modelengine.models.ModelClass;
import net.outmoded.modelengine.models.ModelManager;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

import static org.bukkit.Bukkit.getServer;
/*

   Expressions return info
   Effects don't return anything


   MODELMANAGER:
   [animated-skript] all [the] loaded-models: String[] - Expression
   [animated-skript] all [the] active-models: UUID[] - Expression

   [animated-skript] loaded-model "String" exits : Bool - Expression
   [animated-skript] active-model "String -> UUID" exits: Bool - Expression
   [animated-skript] [the] active-model "String -> UUID": ModelClass - Expression

   [animated-skript] spawn [the] loaded-model "String": null - Effect
   [animated-skript] remove [the] active model "String -> UUID": null - Effect

   [animated-skript] reload (models-config||active-models): null - Effect


   [animated-skript] %active-model%'s/s location: location - Expression
   [animated-skript] (teleport||tp) %active-model%: null - Effect



   MODELCLASS:

       NODES:
       [animated-skript] get [the||all] nodes of %active-model%: Display[] - Expression
       [animated-skript] get [the] node "String" of %active-model% "String -> UUID": Display - Expression

       ANIMATION:
       [animated-skript] get [all] %active-model%s / 's animations: String[] - Expression
       [animated-skript] active-model has animation: Bool - Expression

       [animated-skript] play animation "String" of [the] %active-model% "String -> UUID": null - Effect
       [animated-skript] stop all animations of [the] active-model "String -> UUID": null - Effect
       [animated-skript] stop animation %number% of [the] active-model "String -> UUID": null - Effect


 */


public class ExprGetAllActiveModels extends SimpleExpression<ModelClass> {

    static {
        Skript.registerExpression(ExprGetAllActiveModels.class, ModelClass.class, ExpressionType.COMBINED, "[animated-skript] all [the] active-models");
    }


    @Override
    public Class<? extends ModelClass> getReturnType() {
        //1
        return ModelClass.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return false;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return null;
    }

    @Override
    @Nullable
    protected ModelClass[] get(Event event) {

        return ModelManager.getAllActiveModels();

    }
}

