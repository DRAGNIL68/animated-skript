package net.outmoded.animated_skript.skript.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import net.outmoded.animated_skript.models.new_stuff.Node;

import javax.annotation.Nullable;

public class ActiveModelNode {

    static {
        Classes.registerClass(new ClassInfo<>(Node.class, "activemodelnode")
                .user("nodes?")
                .name("node")
                .description("N/A")
                .examples("N/A")
                .defaultExpression(new EventValueExpression<>(Node.class))
                .parser(new Parser<Node>() {

                    @Override
                    @Nullable
                    public Node parse(String input, ParseContext context) {
                        return null;
                    }

                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @Override
                    public String toVariableNameString(Node node) {
                        return node.uuid.toString();
                    }


                    @Override
                    public String toString(Node node, int flags) {
                        return toVariableNameString(node);
                    }




                }));
    }


}
