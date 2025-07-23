package net.outmoded.animated_skript.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffSetVisibility extends Effect {

    static {
        Skript.registerEffect(EffSetVisibility.class, "[animated-skript] set %activemodel%('s|s) visibility [to] %boolean% for %player%");
    }

    private Expression<ModelClass> activeModel;
    private Expression<Boolean> booleanExpression;
    private Expression<Player> playerExpression;

    private Boolean isPlay = false;
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[0];
        booleanExpression = (Expression<Boolean>) expressions[1];
        playerExpression = (Expression<Player>) expressions[2];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);
        Boolean b = booleanExpression.getSingle(event);
        Player player = playerExpression.getSingle(event);

        if (modelClass != null && b != null && player != null){
            modelClass.setVisibilityForPlayer(player, b);
        }


    }
}
