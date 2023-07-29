package me.fortibrine.woodcutter.utils;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class WoodcutterAxe {

    private int level;
    private double booster;
    private ItemStack item;
    private double cost;

    public WoodcutterAxe(int level, double booster, ItemStack item, double cost) {
        this.level = level;
        this.booster = booster;
        this.item = item;
        this.cost = cost;
    }
}
