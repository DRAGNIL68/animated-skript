package net.outmoded.animated_skript.models.nodes;


public class ActiveAnimation {
    public Animation animationReference;

    public Integer currentFrameTime = 0;
    public boolean isPaused = false;

    public ActiveAnimation(Animation animation){
        animationReference = animation;


    }
    
}
