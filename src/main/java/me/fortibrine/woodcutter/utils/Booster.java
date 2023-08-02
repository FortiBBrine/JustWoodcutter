package me.fortibrine.woodcutter.utils;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Booster {

//    private UUID owner;
    private long time;
    private double booster;
    private boolean global;

    public Booster(/*UUID owner,*/ long time, double booster, boolean global) {
//        this.owner = owner;
        this.time = time;
        this.booster = booster;
        this.global = global;
    }

}
