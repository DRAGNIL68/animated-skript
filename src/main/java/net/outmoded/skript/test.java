package net.outmoded.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;


import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

import javax.annotation.Nullable;
/*
    [the] name of %loaded-model%
    [the] uuid of %active-model
    location of model












public class test extends SimpleExpression<String> {

    static {
        Skript.registerExpression(test.class, String.class, ExpressionType.COMBINED, "[the] name of %player%");
    }

    private Expression<Player> player;
    // [the] name of %player%
    @Override
    public Class<? extends String> getReturnType() {
        //1
        return String.class;
    }

    @Override
    public boolean isSingle() {
        //2
        return true;
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        player = (Expression<Player>) exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        //4
        return player.toString(event, debug);
    }

    @Override
    @Nullable
    protected String[] get(Event event) {
        Player p = player.getSingle(event);
        if (p != null) {
            return new String[] {p.getDisplayName()};
        }
        return null;
    }
}
*/

