package net.outmoded.animated_skript.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import net.outmoded.animated_skript.models.ModelManager;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

import java.util.UUID;

import static net.outmoded.animated_skript.Config.debugMode;
import static org.bukkit.Bukkit.getServer;


public class ExprGetActiveModel extends SimpleExpression<ModelClass> {

    static {
        Skript.registerExpression(ExprGetActiveModel.class, ModelClass.class, ExpressionType.COMBINED, "[animated-skript] [get] [the] active-model %string%");
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

        return text != null;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return "";
    }

    @Override
    @Nullable
    protected ModelClass[] get(Event event) {
        String uuidAsString = text.getSingle(event);

        UUID uuid = null;

        try {
            if (uuidAsString == null)
                return new ModelClass[] {};

            uuid = UUID.fromString(uuidAsString);

        }catch (IllegalArgumentException e){
            if (debugMode())
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN+" model uuid is broken for some reason");
            return new ModelClass[] {};

        }

        if (ModelManager.getInstance().activeModelExists(uuid))
            return new ModelClass[] {ModelManager.getInstance().getActiveModel(uuid)};

        return new ModelClass[] {};
    }
}

