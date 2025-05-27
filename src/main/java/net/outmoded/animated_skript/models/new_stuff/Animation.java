package net.outmoded.animated_skript.models.new_stuff;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Animation {
    public final Map<Integer, Frame> frames = new HashMap<>();

    public UUID uuid;
    public String name;
    public String loopMode;

    public int duration;
    public int loopDelay;
    public int maxFrameTime = 0;

    public Animation(){}
}
