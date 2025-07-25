package net.outmoded.animated_skript.skript.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import net.outmoded.animated_skript.models.new_stuff.Animation;

import javax.annotation.Nullable;

public class ActiveModelAnimation {

    static {
        Classes.registerClass(new ClassInfo<>(Animation.class, "activemodelanimation")
                .user("animations?")
                .name("animation")
                .description("N/A")
                .examples("N/A")
                .defaultExpression(new EventValueExpression<>(Animation.class))
                .parser(new Parser<Animation>() {

                    @Override
                    @Nullable
                    public Animation parse(String input, ParseContext context) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toVariableNameString(Animation animation) {
                        return animation.name;
                    }


                    @Override
                    public String toString(Animation animation, int flags) {
                        return toVariableNameString(animation);
                    }




                }));
    }


}
