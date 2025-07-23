package net.outmoded.animated_skript.models.new_stuff;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Variant {
    public final Map<UUID, UUID> textureMap = new HashMap<>();

    public UUID uuid;
    public String name;
    public String displayName;

    public Variant(){}
}
