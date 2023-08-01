package me.fortibrine.woodcutter.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoosterManager {

    private Map<UUID, Booster> localBoosters = new HashMap<>();

    public double getLocalBooster(UUID uuid) {

        if (!localBoosters.containsKey(uuid)) {
            return 1;
        }

        Booster infoAboutBooster = localBoosters.get(uuid);

        double booster = infoAboutBooster.getBooster();

        if (System.currentTimeMillis() < infoAboutBooster.getTime()) {
            return booster;
        }
        return 1;
    }

    public void setLocalBooster(UUID uuid, double booster, long seconds) {
        long time = System.currentTimeMillis() + 1000 * seconds;

        Booster infoAboutBooster = new Booster(time, booster);

        localBoosters.put(uuid, infoAboutBooster);
    }
}
