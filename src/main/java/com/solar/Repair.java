package com.solar;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class Repair implements Listener {
    /**
     *  Allows the player to hold shift and right click to repair a tool using their exp
     */
    @EventHandler
    public void onPlayerClicks(PlayerInteractEvent event) {
        int costPerPoint = 1;

        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();

        // Check if item is valid
        if( item != null || !item.containsEnchantment(Enchantment.MENDING) || !(item instanceof Damageable)) return;
        Damageable meta = (Damageable)item.getItemMeta();
        if(meta.getDamage() <= 0) return;

        if(!player.isSneaking()) return;
        if(getTotalExperience(player) < costPerPoint) return;

        // Check action
        if ( !action.equals( Action.RIGHT_CLICK_AIR ) ) return;


        // Amount changed per event
        short delta = 10;

        // If delta is greater than total XP use what is left as delta
        if( getTotalExperience(player) < delta )
            delta = (short) getTotalExperience(player);

        // Apply repair to item and remove player's XP
        meta.setDamage( meta.getDamage() - delta );
        item.setItemMeta( (ItemMeta)meta );
        player.giveExp( -delta );
    }

    /**
     * Returns the total amount of XP for the given player
     * @param player
     * @return
     */
    public static int getTotalExperience(Player player) {
        return Math.round(player.getExp() * player.getExpToLevel()) + getTotalExperience(player.getLevel());
    }

    /**
     * Converts the given level to total amount of XP
     * @param level
     * @return
     */
    public static int getTotalExperience(int level) {
        int xp = 0;

        if (level >= 0 && level <= 15) {
            xp = (int) Math.round(Math.pow(level, 2) + 6 * level);
        } else if (level > 15 && level <= 30) {
            xp = (int) Math.round((2.5 * Math.pow(level, 2) - 40.5 * level + 360));
        } else if (level > 30) {
            xp = (int) Math.round(((4.5 * Math.pow(level, 2) - 162.5 * level + 2220)));
        }
        return xp;
    }
}
