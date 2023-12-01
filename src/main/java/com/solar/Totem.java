package com.solar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Totem implements Listener {
    // List of entities that are protected by this plugin
    List<UUID> protectedEntities = new ArrayList<UUID>();
    EntityType[] protectable = {EntityType.WOLF, EntityType.CAT, EntityType.PARROT};

    // Saving stuff
    File file = new File("plugins/Intrinsic/totem.yml");
    FileConfiguration config;

    public Totem() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
            List<String> list = config.getStringList("protected");
            for(String s:list)
                protectedEntities.add( UUID.fromString(s) );

        } catch(Exception e) {
            Bukkit.broadcastMessage( e.getMessage() );
        }
    }

    public void save() {
        String[] list = new String[protectedEntities.size()];
        for(int x=0;x<list.length;x++) {
            list[x] = protectedEntities.get(x).toString();
        }

        config.set("protected", list);

        try {
            config.save(file);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the PlayerInteractEntityEvent by checking if the player has right-clicked an entity that is in the
     * protectable array (such as a wolf, cat, or parrot) and, if so, calling the protect() function.
     *
     * @param event the PlayerInteractEntityEvent to be handled
     */
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        for( EntityType type: protectable )
            if( event.getRightClicked().getType() == type) {
                if( protect(event.getRightClicked(), player) )
                    event.setCancelled(true);
            }
    }

    /**
     * If the player owns the tamed animal and right clicks on them with a Totem of Undying in their hand it will add
     * the entity to the protectedEntities arraylist. If the player sneaks and right clicks with a free hand and the
     * entity is in the protectedEntities arraylist, then the entity will be removed from the list and the player will
     * be returned the Totem of Undying.
     *
     * @param e The entity that is the subject of interaction, must be a tameable mob
     * @param player The player that is interacting with the entity
     */
    public boolean protect(Entity e, Player player) {
        boolean playerHoldingTotem = player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING;

        if(e instanceof Tameable && playerHoldingTotem) {
            Tameable t = (Tameable)e;
            // Prevent non-tamed animals from being protected
            if( !t.isTamed() ) {
                player.sendMessage("You can only protect animals that you have tamed.");
                return true;
            }

            // Prevent protecting of other players animals
            if( t.getOwner() != player) {
                player.sendMessage("You do not own this animal.");
                return true;
            }
        }

        // Check if the animal is protected
        if(protectedEntities.indexOf(e.getUniqueId()) == -1) { // Animal is not protected
            if(playerHoldingTotem) {
                protectedEntities.add(e.getUniqueId());

                // If player is not in creative, then remove the Totem of Undying from the player's hand
                if(player.getGameMode() != GameMode.CREATIVE)
                    player.getInventory().setItemInMainHand(null);

                player.sendMessage(e.getName() + " is now wearing the " + ChatColor.YELLOW + "Totem of Undying.");

                for(UUID u:protectedEntities)
                    player.sendMessage(u);
                save();
                return true;
            }
        } else {
            // Check if the player has an empty hand
            if(player.getInventory().getItemInMainHand().getType() != Material.AIR)
                return false;

            // Check if the player is sneaking
            if(!player.isSneaking())
                return false;

            // Remove from the list of protected entities
            protectedEntities.remove(e.getUniqueId());

            // If the player is not in creative, place the Totem of Undying in to the player's hand
            if (player.getGameMode() != GameMode.CREATIVE)
                player.getInventory().setItemInMainHand(new ItemStack(Material.TOTEM_OF_UNDYING));

            player.sendMessage(e.getName() + " is no longer wearing the " + ChatColor.YELLOW + "Totem of Undying.");
            save();
            return true;
        }

        return false;
    }

    /**
     * Prevents the death of a living entity by canceling a damage event, playing effects and sounds, and
     * teleporting the entity to a safe location. If the entity is a tameable creature (e.g. a wolf or cat),
     * it will be teleported to its owner's bed, if possible. Otherwise, it will be teleported to the world
     * spawn point.
     *
     * @param event the damage event that would normally cause the entity to die
     * @param entity the living entity to be saved
     */
    public void saveEntity(EntityDamageEvent event, Entity entity) {
        boolean moved = false;

        if(!(entity instanceof LivingEntity)) return;
        LivingEntity e = (LivingEntity)entity;
        if(e.getHealth() - event.getDamage() > 0) return;

        // Check if entity is worth saving, ie has the totem
        if(!protectedEntities.contains(e.getUniqueId()))
            return;

        // Prevent death
        event.setDamage(0);
        event.setCancelled(true);

        // Play totem sounds and effects
        e.getWorld().playSound(e.getLocation(), Sound.ITEM_TOTEM_USE, 6F, 1F);
        e.getWorld().playEffect(e.getLocation(), Effect.BOW_FIRE, 5);

        if(e instanceof Tameable) {
            Tameable t = (Tameable)e;
            if(t.getOwner() == null) return;

            // Notify owner
            Player owner = (Player) t.getOwner();
            owner.sendMessage(e.getName()+" was saved!");

            // Teleport to bed
            if(owner.getBedSpawnLocation() != null) {
                e.teleport(owner.getBedSpawnLocation());
                moved = true;
            }
        }

        // If the entity has not been moved send entity to world spawn
        if(!moved) {
            Server server = Bukkit.getServer();
            World world = server.getWorld("world");
            Location loc = world.getSpawnLocation();
            e.teleport(Util.getCenter(loc));
        }

        // Set sitting if able
        if(e instanceof Wolf) ((Wolf)e).setSitting(true);
        if(e instanceof Cat) ((Cat)e).setSitting(true);
        if(e instanceof Parrot) ((Parrot)e).setSitting(true);
    }

    @EventHandler
    public void onDamageEvent(EntityDamageEvent event) {
        Entity e = event.getEntity();
        for(EntityType p:protectable)
            if (e.getType() == p) {
                saveEntity(event, e);
                return;
            }
    }
}
