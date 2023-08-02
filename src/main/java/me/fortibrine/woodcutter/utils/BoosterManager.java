package me.fortibrine.woodcutter.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoosterManager {

    private Map<UUID, Booster> localBoosters = new HashMap<>();
    private Booster globalBooster;

    public double getGlobalBooster() {
        if (globalBooster == null || globalBooster.getTime() < System.currentTimeMillis()) {
           return 0;
        }
        System.out.println(this.globalBooster.getBooster()+" "+ this.globalBooster.getTime());
        return this.globalBooster.getBooster();
    }

    public boolean setGlobalBooster(double booster, long seconds) {

        if (globalBooster != null && System.currentTimeMillis() < globalBooster.getTime()) {
            return false;
        }

        globalBooster = new Booster(/*uuid,*/ System.currentTimeMillis() + seconds * 1000, booster,true);

        return true;
    }

    public double getLocalBooster(UUID uuid) {

        if (!localBoosters.containsKey(uuid)) {
            return 0;
        }

        Booster infoAboutBooster = localBoosters.get(uuid);

        double booster = infoAboutBooster.getBooster();

        if (System.currentTimeMillis() < infoAboutBooster.getTime()) {
            return booster;
        }
        return 0;
    }

    public boolean setLocalBooster(UUID uuid, double booster, long seconds) {

        if (localBoosters.containsKey(uuid) && System.currentTimeMillis() < localBoosters.get(uuid).getTime()) {
            return false;
        }

        long time = System.currentTimeMillis() + 1000 * seconds;

        Booster infoAboutBooster = new Booster(/*uuid,*/ time, booster, false);

        localBoosters.put(uuid, infoAboutBooster);

        return true;
    }
}
