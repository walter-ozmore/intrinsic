package com.solar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoTrash implements Listener {
    private HashMap<Player, ArrayList<Material>> store;
    ArrayList<Material> itemS = new ArrayList<>();

    public AutoTrash() {
        store = new HashMap<>();

        itemS.add( Material.DIAMOND );
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if( !store.containsKey(event.getPlayer()) ) return;

        ArrayList<Material> omitted = store.get( event.getPlayer() );
        Material itemType = event.getItem().getItemStack().getType();

        for(Material m:omitted)
            if (itemType == m)
                event.setCancelled(true);
    }

    public boolean command(CommandSender sender, Command command, String label, String[] args) {
        if ( !(sender instanceof Player) ) return false;

        // Player's only section
        Player player = (Player) sender;

        if (args.length != 1) return false;

        Material itemType = Material.getMaterial(args[0].toUpperCase());
        if (itemType != null) {
            ArrayList<Material> omitted = (store.containsKey(player))? store.get(player) : new ArrayList<Material>();
            String itemName = itemType.name().toLowerCase().replaceAll("_", " ");

            // Detect if the item name should have an S at the end
            if(itemS.contains(itemName)) itemName += "s";

            if(!omitted.contains(itemType)) {
                omitted.add(itemType);
                player.sendMessage("You will no longer pick up any "+ itemName +"s.");
            } else {
                omitted.remove(itemType);
                player.sendMessage("You will now pickup all "+ itemName +"s.");
            }

            store.put(player, omitted);
            return true;

        }

        player.sendMessage("Invalid item type, please enter the correct material name");
        return true;
    }
}
