package com.solar;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/* Allows flowers to be reproduced by a player right-clicking on any flower to produce more */
public class Flower implements Listener {
    Material[] flowers = new Material[12];

    public Flower() {
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

    @EventHandler
    public void onPlayerClicks(PlayerInteractEvent event) {
      Player player = event.getPlayer();
      Action action = event.getAction();
      ItemStack item = event.getItem();
      if ( item != null && item.getType() == Material.BONE_MEAL )
        if ( action.equals( Action.RIGHT_CLICK_BLOCK ) ) {
          for(Material flower:flowers)
            if( event.getClickedBlock().getType() == flower ) {
              for(int z=0;z<(int)(1);z++)
                event.getClickedBlock().getWorld().dropItem(Util.getCenter(event.getClickedBlock().getLocation()), new ItemStack(flower));
              if(player.getGameMode() != GameMode.CREATIVE) {
                ItemStack inv = player.getInventory().getItem( player.getInventory().getHeldItemSlot() );
                inv.setAmount(inv.getAmount()-1);
              }
            }
        }
    }
}