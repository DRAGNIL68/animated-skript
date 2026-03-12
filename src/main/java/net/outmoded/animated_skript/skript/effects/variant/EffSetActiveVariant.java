package net.outmoded.animated_skript.skript.effects.variant;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

import javax.annotation.Nullable;

public class EffSetActiveVariant extends Effect {

    public static void register(SyntaxRegistry registry) {
        registry.register(
                SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffSetActiveVariant.class)
                        .addPatterns(
                                "[animated-skript] set %activemodel%('s|s) active variant [to] %string%"
                        )
                        .supplier(EffSetActiveVariant::new)
                        .build());

    }

    private Expression<ModelClass> activeModel;
    private Expression<String> stringExpression;

    
    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        activeModel = (Expression<ModelClass>) expressions[0];
        stringExpression = (Expression<String>) expressions[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    protected void execute(Event event) {
        ModelClass modelClass = activeModel.getSingle(event);
        String string = stringExpression.getSingle(event);
        if (modelClass != null && string != null){
            modelClass.setActiveVariant(string);

        }



    }
}
