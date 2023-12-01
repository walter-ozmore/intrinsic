package com.solar;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class RePlant implements Listener {
    @EventHandler
    public void onPlayerClicks(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        Block block = event.getClickedBlock();

        if ( action.equals(Action.RIGHT_CLICK_BLOCK) && item != null && (item.getType() == Material.WOODEN_HOE || item.getType() == Material.STONE_HOE || item.getType() == Material.GOLDEN_HOE || item.getType() == Material.DIAMOND_HOE || item.getType() == Material.NETHERITE_HOE) ) {
            if(block.getType() == Material.WHEAT) {
                block.breakNaturally(item);
                if(player.getInventory().contains(Material.WHEAT)) {
                    player.getInventory().remove(new ItemStack(Material.WHEAT, 1));
                    block.getLocation().getBlock().setType(Material.WHEAT);
                }
            }
            if(block.getType() == Material.CARROTS) {
                block.breakNaturally(item);
                if(player.getInventory().contains(Material.CARROT)) {
                    player.getInventory().remove(new ItemStack(Material.CARROT, 1));
                    block.getLocation().getBlock().setType(Material.CARROTS);
                }
            }
            if(block.getType() == Material.POTATOES) {
                block.breakNaturally(item);
                if(player.getInventory().contains(Material.POTATO)) {
                    player.getInventory().remove(new ItemStack(Material.POTATO, 1));
                    block.getLocation().getBlock().setType(Material.POTATOES);
                }
            }
            if(block.getType() == Material.BEETROOTS) {
                block.breakNaturally(item);
                if(player.getInventory().contains(Material.BEETROOT)) {
                    player.getInventory().remove(new ItemStack(Material.BEETROOT, 1));
                    block.getLocation().getBlock().setType(Material.BEETROOTS);
                }
            }
            //Assess the damage
            int level = 0;
            Map<Enchantment, Integer> enchantments = item.getEnchantments();
            if (enchantments.containsKey(Enchantment.DURABILITY))
                level = enchantments.get(Enchantment.DURABILITY);
            if(Math.random()*100 < 100 / (level+1)) {
                Damageable pdmg = (Damageable)item.getItemMeta();
                int health = pdmg.getDamage()+1;

                if(health <= 0) {
                    player.getInventory().remove( item );
                }

                pdmg.setDamage( health );
                item.setItemMeta((ItemMeta) pdmg);
            }
        }
    }
}