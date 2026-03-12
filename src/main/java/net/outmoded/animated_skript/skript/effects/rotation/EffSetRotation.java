package net.outmoded.animated_skript.skript.effects.rotation;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;
import org.joml.Quaternionf;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;

public class EffSetRotation extends Effect {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffSetRotation.class)
                        .addPatterns(
                                "[animated-skript] set %activemodel%('s|s) rotation [to] %quaternion%"
                        )
                        .supplier(EffSetRotation::new)
                        .build());

    }

    private Expression<ModelClass> activeModel;
    private Expression<Quaternionf> integerExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[0];
        integerExpression = (Expression<Quaternionf>) expressions[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);
        Quaternionf quaternionf = integerExpression.getSingle(event);

        if (modelClass != null && quaternionf != null){
            modelClass.setRotation(quaternionf);
        }


    }
}
