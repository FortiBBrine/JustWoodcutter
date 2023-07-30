package me.fortibrine.woodcutter.utils;

import it.unimi.dsi.fastutil.doubles.DoubleLongPair;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoosterManager {

    private Map<UUID, DoubleLongPair> localBoosters = new HashMap<>();

    public double getLocalBooster(UUID uuid) {

        DoubleLongPair infoAboutBooster = localBoosters.get(uuid);

        double booster = infoAboutBooster.firstDouble();

        if (infoAboutBooster.secondLong() < System.currentTimeMillis()) {
            return booster;
        }
        return 1;
    }

    public void setLocalBooster(UUID uuid, double booster, long seconds) {
        long time = System.currentTimeMillis() + 1000 * seconds;

        DoubleLongPair infoAboutBooster = DoubleLongPair.of(booster, time);

        localBoosters.put(uuid, infoAboutBooster);
    }
}
