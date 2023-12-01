package com.solar;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MultiBedSpawn implements Listener {
    private HashMap<Player, ArrayList<Location>> bedLocations;

    public MultiBedSpawn() {
        bedLocations = new HashMap<>();
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        Location bedLocation = event.getBed().getLocation();

        ArrayList<Location> list = (bedLocations.containsKey(player))? bedLocations.get(player) : new ArrayList<>();
        list.add(bedLocation);
        bedLocations.put(player, list);

        player.setBedSpawnLocation(bedLocation, true);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.sendMessage( "Rrrrrrrespawn!!" );
        ArrayList<Location> list = bedLocations.get(player);
        for (int x = list.size() - 1; x >= 0; x--) {
            Location bedLocation = list.get(x);

            player.sendMessage( "Trying bed "+x+" at " + locStr(bedLocation) );

            if (bedLocation.getBlock().getType() != Material.RED_BED) {
                list.remove( x );
                player.sendMessage( "§4Your bed at " + locStr(bedLocation) + " was missing." );
                continue;
            }
            event.setRespawnLocation(bedLocation);
            break;
        }
    }

    public String locStr(Location loc) {
        return "x: "+ loc.getBlockX() +" y:"+ loc.getBlockY() +" z:"+ loc.getBlockZ();
    }

    public static boolean areLocationsEqual(Location loc1, Location loc2) {
        return loc1.getBlock().equals(loc2.getBlock());
    }
}