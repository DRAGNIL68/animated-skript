package net.outmoded.animated_skript.skript.expressions.event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.animated_skript.events.*;
import net.outmoded.animated_skript.models.ModelClass;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprEventGetEventModel extends SimpleExpression<ModelClass> {

    static {
        Skript.registerExpression(ExprEventGetEventModel.class, ModelClass.class, ExpressionType.COMBINED, "[animated-skript] event-model");
    }



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

        if (getParser().isCurrentEvent(ModelSpawnedEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(ModelRemovedEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(ModelStartAnimationEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(ModelEndAnimationEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(ModelPauseAnimationEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(ModelUnpauseAnimationEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(ModelFrameSetAnimationEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(ActiveModelHitboxAttack.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(ActiveModelHitboxInteract.class)){
            return true;
        }
        else{
            return false;
        }


    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "";
    }

    @Override
    @Nullable
    protected ModelClass[] get(Event event) {
        ModelClass modelClass;

        if (event instanceof ModelSpawnedEvent event1){
            modelClass = event1.getActiveModel();
            return null;

        }
        else if (event instanceof ModelRemovedEvent event1){
            modelClass = event1.getActiveModel();

        }
        else if (event instanceof ModelStartAnimationEvent event1){;
            modelClass = event1.getActiveModel();

        }
        else if (event instanceof ModelEndAnimationEvent event1){
            modelClass = event1.getActiveModel();
        }
        else if (event instanceof ModelUnpauseAnimationEvent event1){
            modelClass = event1.getActiveModel();

        }
        else if (event instanceof ModelPauseAnimationEvent event1){
            modelClass = event1.getActiveModel();

        }
        else if (event instanceof ModelFrameSetAnimationEvent event1){
            modelClass = event1.getActiveModel();

        }
        else {
            return null;
        }

        if (modelClass != null)
            return new ModelClass[] {modelClass};
        else
            return null;
    }

}
