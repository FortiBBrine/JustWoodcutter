package me.fortibrine.woodcutter.utils;

import lombok.Getter;

@Getter
public class Booster {

    private long time;
    private double booster;

    public Booster(long time, double booster) {
        this.time = time;
        this.booster = booster;
    }

}
