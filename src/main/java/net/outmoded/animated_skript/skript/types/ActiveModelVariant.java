package net.outmoded.animated_skript.skript.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import net.outmoded.animated_skript.models.new_stuff.Variant;


import javax.annotation.Nullable;

public class ActiveModelVariant {

    static {
        Classes.registerClass(new ClassInfo<>(Variant.class, "activemodelvariant")
                .user("variants?")
                .name("variant")
                .description("N/A")
                .examples("N/A")
                .defaultExpression(new EventValueExpression<>(Variant.class))
                .parser(new Parser<Variant>() {

                    @Override
                    @Nullable
                    public Variant parse(String input, ParseContext context) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toVariableNameString(Variant variant) {
                        return variant.name;
                    }


                    @Override
                    public String toString(Variant variant, int flags) {
                        return toVariableNameString(variant);
                    }




                }));
    }


}
