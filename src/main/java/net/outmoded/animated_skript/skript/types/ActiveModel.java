package net.outmoded.animated_skript.skript.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;

import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import net.outmoded.animated_skript.models.ModelClass;

import javax.annotation.Nullable;

public class ActiveModel {

    static {
        Classes.registerClass(new ClassInfo<>(ModelClass.class, "activemodel")
                .user("activemodels?")
                .name("activemodel")
                .description("N/A")
                .examples("N/A")
                .defaultExpression(new EventValueExpression<>(ModelClass.class))
                .parser(new Parser<ModelClass>() {

                    @Override
                    @Nullable
                    public ModelClass parse(String input, ParseContext context) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toVariableNameString(ModelClass modelClass) {
                        return modelClass.getUuid().toString();
                    }


                    @Override
                    public String toString(ModelClass modelClass, int flags) {
                        return toVariableNameString(modelClass);
                    }




                }));
    }


}
