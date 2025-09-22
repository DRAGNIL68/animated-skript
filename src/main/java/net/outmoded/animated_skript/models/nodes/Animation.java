package net.outmoded.animated_skript.models.nodes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Animation {
    public final Map<Integer, Frame> frames = new HashMap<>();

    public UUID uuid;
    public String name;
    public String loopMode;

    public Integer duration;
    public Integer loopDelay;
    public Integer maxFrameTime = 0;



    // this is a hacky fix for a skript effect and SHOULD ONLY BE USE FOR THE SKRIPT EFFECTS
    public Boolean isPausedSkript = false;
    public Integer currentFrameTimeSkript = null;


    public Animation(){}

}
