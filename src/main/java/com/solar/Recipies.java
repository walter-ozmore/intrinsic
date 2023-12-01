package com.solar;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class Recipies {
    static Material[] wools = new Material[16];
    static Material[] beds = new Material[16];
    static Material[] banners = new Material[16];
    static Material[] flowers = new Material[12];

    public void delArrays() {
        wools = null;
        beds = null;
        flowers = null;
    }

    public static void setArrays() {
        //Add items to list
        wools[ 0] = Material.WHITE_WOOL;
        wools[ 1] = Material.ORANGE_WOOL;
        wools[ 2] = Material.MAGENTA_WOOL;
        wools[ 3] = Material.LIGHT_BLUE_WOOL;
        wools[ 4] = Material.YELLOW_WOOL;
        wools[ 5] = Material.LIME_WOOL;
        wools[ 6] = Material.PINK_WOOL;
        wools[ 7] = Material.GRAY_WOOL;
        wools[ 8] = Material.LIGHT_GRAY_WOOL;
        wools[ 9] = Material.CYAN_WOOL;
        wools[10] = Material.PURPLE_WOOL;
        wools[11] = Material.BLUE_WOOL;
        wools[12] = Material.BROWN_WOOL;
        wools[13] = Material.GREEN_WOOL;
        wools[14] = Material.RED_WOOL;
        wools[15] = Material.BLACK_WOOL;

        beds[ 0] = Material.WHITE_BED;
        beds[ 1] = Material.ORANGE_BED;
        beds[ 2] = Material.MAGENTA_BED;
        beds[ 3] = Material.LIGHT_BLUE_BED;
        beds[ 4] = Material.YELLOW_BED;
        beds[ 5] = Material.LIME_BED;
        beds[ 6] = Material.PINK_BED;
        beds[ 7] = Material.GRAY_BED;
        beds[ 8] = Material.LIGHT_GRAY_BED;
        beds[ 9] = Material.CYAN_BED;
        beds[10] = Material.PURPLE_BED;
        beds[11] = Material.BLUE_BED;
        beds[12] = Material.BROWN_BED;
        beds[13] = Material.GREEN_BED;
        beds[14] = Material.RED_BED;
        beds[15] = Material.BLACK_BED;

        banners[ 0] = Material.WHITE_BANNER;
        banners[ 1] = Material.ORANGE_BANNER;
        banners[ 2] = Material.MAGENTA_BANNER;
        banners[ 3] = Material.LIGHT_BLUE_BANNER;
        banners[ 4] = Material.YELLOW_BANNER;
        banners[ 5] = Material.LIME_BANNER;
        banners[ 6] = Material.PINK_BANNER;
        banners[ 7] = Material.GRAY_BANNER;
        banners[ 8] = Material.LIGHT_GRAY_BANNER;
        banners[ 9] = Material.CYAN_BANNER;
        banners[10] = Material.PURPLE_BANNER;
        banners[11] = Material.BLUE_BANNER;
        banners[12] = Material.BROWN_BANNER;
        banners[13] = Material.GREEN_BANNER;
        banners[14] = Material.RED_BANNER;
        banners[15] = Material.BLACK_BANNER;

        flowers[ 0] = Material.DANDELION;
        flowers[ 1] = Material.POPPY;
        flowers[ 2] = Material.BLUE_ORCHID;
        flowers[ 3] = Material.ALLIUM;
        flowers[ 4] = Material.AZURE_BLUET;
        flowers[ 5] = Material.RED_TULIP;
        flowers[ 6] = Material.ORANGE_TULIP;
        flowers[ 7] = Material.WHITE_TULIP;
        flowers[ 8] = Material.PINK_TULIP;
        flowers[ 9] = Material.OXEYE_DAISY;
        flowers[10] = Material.CORNFLOWER;
        flowers[11] = Material.LILY_OF_THE_VALLEY;
    }

    public static void onEnable() {
        Server server = Bukkit.getServer();
        setArrays();

        ShapelessRecipe shapelessRecipe;
        ShapedRecipe shapedRecipe;

        shapelessRecipe = new ShapelessRecipe(
                new NamespacedKey(Intrinsic.getPlugin(Intrinsic.class), "ClayToClayBall"),
                new ItemStack(Material.CLAY_BALL, 4) );
        shapelessRecipe.addIngredient( Material.CLAY );
        server.addRecipe(shapelessRecipe);
    }
}