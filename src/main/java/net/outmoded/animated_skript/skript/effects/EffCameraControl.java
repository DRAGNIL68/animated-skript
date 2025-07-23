package net.outmoded.animated_skript.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.AnimatedSkript;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.ModelManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffCameraControl extends Effect {

    static {
        Skript.registerEffect(EffCameraControl.class, "[animated-skript] make %player% (:start|stop) spectating %activemodel%('s|s) camera %string%");
    }

    private Expression<ModelClass> activeModel;
    private Expression<String> string;
    private Expression<Player> playerExpression;

    private Boolean isStart = false; // true = start , false = stop
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[1];
        string = (Expression<String>) expressions[2];
        playerExpression = (Expression<Player>) expressions[0];
        isStart = parser.hasTag("start");
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);
        String string1 = string.getSingle(event);
        Player player = playerExpression.getSingle(event);
        AnimatedSkript.getInstance().getLogger().warning("s12");

        if (modelClass != null && string1 != null && player != null){
            AnimatedSkript.getInstance().getLogger().warning("s122");
            if (modelClass.hasActiveCamera(string1)){
                AnimatedSkript.getInstance().getLogger().warning("s123");
                if (isStart){
                    boolean bb = modelClass.spectateNode(player, modelClass.getUuidFromActiveCamera(string1));
                    AnimatedSkript.getInstance().getLogger().warning("spectating camera "+bb);
                }
                else{
                    ModelManager.getInstance().stopSpectatingNode(player);

                }
            }
        }
    }
}
