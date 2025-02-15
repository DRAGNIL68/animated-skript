package net.outmoded.modelengine.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
/*


   %active-model% = skript type for ModelClass

   Expressions return info
   Effects don't return anything


   MODELMANAGER:
        EXPRESSIONS:
            [animated-skript] all [the] (loaded-models|active-models): String[] - Expression
            [animated-skript] (loaded-model|active-model) %string% exits : Bool - Expression

            [animated-skript] [get] [the] active-model "String -> UUID": ModelClass - Expression
            [animated-skript] [get] %active-model%'s/s location: location - Expression

        EFFECTS:
            [animated-skript] spawn [the] loaded-model %string%: null - Effect
            [animated-skript] remove [the] active model "String -> UUID": null - Effect

            [animated-skript] reload (model-configs||active-models): null - Effect
            [animated-skript] (teleport||tp) %active-model%: null - Effect

        CONDITIONS:
            [animated-skript] active-model %string% (exits|dose not exits|doesn't exits)


   MODELCLASS:
        EXPRESSIONS:
            [animated-skript] get model type: string - Expression
            [animated-skript] get uuid: Uuid - Expression
            [animated-skript] get active variant: string - Expression

            [animated-skript] get all [the] variants: string list - Expression

        EFFECTS:
            [animated-skript] set [the] active variant %string%
            [animated-skript] reset active variant



       NODES:
            EXPRESSIONS:
                    [animated-skript] [get] [the||all] nodes of %active-model%: Display[] - Expression
                    [animated-skript] [get] [the] node %string% of %active-model% "String -> UUID": Display - Expression

       ANIMATION:
            EXPRESSIONS:
                [animated-skript] [get] [all] %active-model%s / 's animations: String[] - Expression
                [animated-skript] active-model has animation: Bool - Expression

            EFFECTS:
                [animated-skript] (play|stop) animation %string% of [the] %active-model% "String -> UUID": null - Effect
                [animated-skript] stop all animations of [the] active-model "String -> UUID": null - Effect


       UNIMPLEMENTED:
       [animated-skript] pause animation %number% of [the] active-model "String -> UUID": null - Effect
       [animated-skript] resume animation %number% of [the] active-model "String -> UUID": null - Effect
       [animated-skript] %active-model%'s/s scale: Vector - Expression



   EVENTS:
   [animated-skript] on model spawn: Event
   [animated-skript] on model remove: Event
   [animated-skript] on animation start: Event
   [animated-skript] on animation stop: Event
   [animated-skript] on reload (model-configs||active-models) : Event
 */











public class Test extends SimpleExpression<String> {

    static {
        Skript.registerExpression(Test.class, String.class, ExpressionType.COMBINED, "[the] name of %player%");
    }

    private Expression<Player> player;
    // [the] name of %player%
    @Override
    public Class<? extends String> getReturnType() {
        //1
        return String.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return false;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        player = (Expression<Player>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return player.toString(event, debug);
    }

    @Override
    @Nullable
    protected String[] get(Event event) {
        Player p = player.getSingle(event);
        String[] strings = {"frog", "2forgs"};
        if (p != null) {
            return strings;
        }
        return null;
    }
}

