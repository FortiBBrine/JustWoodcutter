package me.fortibrine.woodcutter.utils;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {

    private Economy economy = null;

    public EconomyManager() {

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();

    }

    public boolean takeMoney(Player player, double amount) {
        if (!economy.has(player, amount)) {
            return false;
        }

        EconomyResponse economyReponse = economy.withdrawPlayer(player, amount);

        return economyReponse.transactionSuccess();
    }

    public boolean giveMoney(Player player, double amount) {
        EconomyResponse economyResponse = economy.depositPlayer(player, amount);

        return economyResponse.transactionSuccess();
    }
}