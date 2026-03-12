package net.outmoded.animated_skript.models;

public enum StablePlayerDisplayOffset {
    HEAD(0),

    RIGHT_ARM(1024),
    LEFT_ARM(2048),

    TORSO(3072),

    RIGHT_LEG(4096),
    LEFT_LEG(5120);

    public final int offset;

    private StablePlayerDisplayOffset(int label) {
        this.offset = label;
    }


}
