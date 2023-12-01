package com.solar;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public final class Intrinsic extends JavaPlugin implements Listener {
    public static FileConfiguration config;
    public static AutoTrash autoTrash;

    @Override
    public void onEnable() {
//        addDefaults();
//        config = getConfig();

        autoTrash = new AutoTrash();
        Bukkit.getPluginManager().registerEvents(autoTrash, this);

        Util.setArrays();
//        if(getConfig().getBoolean("Repair.enable"))
            Bukkit.getPluginManager().registerEvents(new Repair(), this);
//        if(getConfig().getBoolean("Flower.enable"))
            Bukkit.getPluginManager().registerEvents(new Flower(), this);
//        if(getConfig().getBoolean("Totem.enable"))
            Bukkit.getPluginManager().registerEvents(new Totem() , this);
//        if(getConfig().getBoolean("Trades.enable"))
            Bukkit.getPluginManager().registerEvents(new Trades(), this);

        Bukkit.getPluginManager().registerEvents(this, this);
        Recipies.onEnable();
    }

    // Creepers explosions will drop all blocks broken
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntityType().equals(EntityType.CREEPER)) {
            for(Block block : event.blockList()) {
//                ItemStack blockItem = new ItemStack(block.getType(), 1);
//                block.getWorld().dropItemNaturally(block.getLocation(), blockItem);
                ItemStack blockItem = new ItemStack(Material.DIAMOND_PICKAXE, 1);
                block.breakNaturally( blockItem );
            }
        }
    }

    // Teleport all dogs to player when the chunk unloads
//    @EventHandler
//    public void onChunkUnload(ChunkUnloadEvent event) {
//        String str = "";
//        for (Entity entity : event.getChunk().getEntities()) {
//            if(entity instanceof Wolf) str += "W";
//            if(entity instanceof Zombie) str += "Z";
//
//            if ( !(entity instanceof Wolf) ) continue;
//            Wolf wolf = (Wolf) entity;
//            if (!wolf.isTamed() || wolf.isSitting()) continue;
//
//            Player player = (Player) wolf.getOwner();
//            wolf.teleport( player );
//            player.sendMessage( "Wooosh" );
//        }
//        if(str.length() > 0)
//            Bukkit.broadcastMessage(str);
//    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("autotrash"))
            return autoTrash.command(sender, command, label, args);
        return false;
    }

    public void addDefaults() {
        File pluginFolder = getDataFolder();
        File configFile = new File(pluginFolder, "config.yml");

        if( configFile.exists() ) return;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
            writer.write("# Allow flower duplication of small flower using bone meal similar to the tall flower\n");
            writer.write("flower:\n");
            writer.write("  enable: false\n");

            writer.write("\n# Allows players to crouch and right click to spend their XP to repair an item in their\n");
            writer.write("# hand if it has mending.\n");
            writer.write("repair:\n");
            writer.write("  enable: false\n");
            writer.write("  cost-per-durability: 1\n");

            writer.write("\n# Enable custom recipes\n");
            writer.write("recipes:\n");
            writer.write("  enable: false\n");

            writer.write("\n# Enable custom wandering trader trades\n");
            writer.write("trades:\n");
            writer.write("  enable: false\n");
            writer.write("\n  # Removes the default trader from the wandering trader when set the false\n");
            writer.write("  include-default-trader: true\n");
            writer.write("\n  # Includes player heads and miniblocks from being included with wandering traders\n");
            writer.write("  # These can be edited further in trades.txt\n");
            writer.write("  include-miniblocks: true\n");
            writer.write("  include-playerheads: true\n");

            writer.write("\n# Allow players to protect mobs by giving them Totems Of Undying\n");
            writer.write("# Currently only tamed dogs, cats and parrots can be protected\n");
            writer.write("totem:\n");
            writer.write("  enable: false\n");

            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
