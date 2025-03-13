package net.outmoded.modelengine.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.modelengine.events.OnModelSpawnedEvent;
import net.outmoded.modelengine.models.ModelClass;
import net.outmoded.modelengine.models.ModelManager;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

import java.util.UUID;

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


public class ExprGetActiveModel extends SimpleExpression<ModelClass> {

    static {
        Skript.registerExpression(ExprGetActiveModel.class, ModelClass.class, ExpressionType.COMBINED, "[animated-skript] [get] [the] active-model %-string%");
    }

    private Expression<String> text; // if true = loaded-models | if false = active-models

    @Override
    public Class<? extends ModelClass> getReturnType() {
        //1
        return ModelClass.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        text = (Expression<String>) exprs[0];

        if (!getParser().isCurrentEvent(OnModelSpawnedEvent.class)){ // TODO: dont forget this is here you fucking retatrd
            return false;
        }




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
        String uuidAsString = text.toString().replace("\"", "");

        UUID uuid = null;

        try {
            uuid = UUID.fromString(uuidAsString);

        }catch (IllegalArgumentException e){
            return null;

        }

        if (ModelManager.activeModelExists(uuid))
            return new ModelClass[] {ModelManager.getActiveModel(uuid)};

        return null;
    }
}

