package net.outmoded.modelengine.skript.expressions.event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.outmoded.modelengine.events.*;
import net.outmoded.modelengine.models.ModelClass;
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

        if (getParser().isCurrentEvent(OnModelSpawnedEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(OnModelRemovedEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(OnModelStartAnimationEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(OnModelEndAnimationEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(OnModelPauseAnimationEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(OnModelUnpauseAnimationEvent.class)){
            return true;
        }
        else if (getParser().isCurrentEvent(OnModelFrameSetAnimationEvent.class)){
            return true;
        }
        else{
            return false;
        }


    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return null;
    }

    @Override
    @Nullable
    protected ModelClass[] get(Event event) {
        ModelClass modelClass;

        if (event instanceof OnModelSpawnedEvent event1){
            modelClass = event1.getActiveModel();
            return null;

        }
        else if (event instanceof OnModelRemovedEvent event1){
            modelClass = event1.getActiveModel();

        }
        else if (event instanceof OnModelStartAnimationEvent event1){;
            modelClass = event1.getActiveModel();

        }
        else if (event instanceof OnModelEndAnimationEvent event1){
            modelClass = event1.getActiveModel();
        }
        else if (event instanceof OnModelUnpauseAnimationEvent event1){
            modelClass = event1.getActiveModel();

        }
        else if (event instanceof OnModelPauseAnimationEvent event1){
            modelClass = event1.getActiveModel();

        }
        else if (event instanceof OnModelFrameSetAnimationEvent event1){
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
