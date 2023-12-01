package com.solar;

import org.bukkit.*;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Trades implements Listener {

    class Head {
        String name, user;
        ItemStack cost;
    }

    ArrayList<Head> heads;

    File file = new File("plugins/Intrinsic/trades.txt");
    long fileLastChange = 0;

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof WanderingTrader)) return;

        WanderingTrader villager = (WanderingTrader) event.getEntity();
        List<MerchantRecipe> trades = new ArrayList<MerchantRecipe>();
        switch( (int)(Math.random() * 4 + 1) ) {
            case 1:  trades = getColorTrades();  break;
            case 2:  trades = getHorseTrades();  break;
            case 3:  trades = getFishMongerTrades();  break;
            case 4:  trades = getMinerTrades();  break;
        }
        trades.addAll( getHeadTrades() );
        villager.setRecipes( trades );

        Bukkit.broadcastMessage( "The wandering trader has arrived at "+villager.getLocation().getBlockX()+","+villager.getLocation().getBlockY()+"!" );
    }

    public ItemStack getHead(String playerName) {
        String name = playerName.replace("MHF_", "");
        name = name.replaceAll("_", " ");
        return getHead(playerName, name);
    }

    public ItemStack getHead(String playerName, String name) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = Bukkit.createPlayerProfile( UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"), playerName);
        meta.setOwnerProfile( profile );
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public List<MerchantRecipe> getHeads() {
        return null;
    }

    public List<MerchantRecipe> getHeadTrades() {
        // Update file if it has changed
        if(fileLastChange != file.lastModified())
            load();

        List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
        ArrayList<Head> random = getRandomHeads();
        for(Head head: random) {
            ItemStack cost1 = new ItemStack(Material.EMERALD, 1);
            ItemStack cost2 = null;
            ItemStack output = getHead(head.user, head.name);
            int limit = (int)(Math.random()*5+1);

            // Converting a block, create 8 and reduce the amount you can trade
            if(head.cost != null) {
                cost2 = head.cost;
                limit = 1;
                output.setAmount( 8 );
            }

            recipes.add(createRecipe(cost1, cost2, output, limit));

        }
        return recipes;
    }

    public ArrayList<Head> getRandomHeads() {
        int amount = 5;
        ArrayList<Head> random = new ArrayList<Head>();
        if(amount > heads.size())
            random = heads;
        else {
            while (random.size() < 5) {
                Head head = heads.get((int) (Math.random() * heads.size()));
                if (random.contains(head))
                    continue;

                random.add(head);
            }
        }

        // Sort heads
        for(int x=0;x<random.size();x++) {
            Head head = random.get(x);
            if(head.cost == null)
                random.add(0, random.remove(x));
        }

        return random;
    }

    public void load() {
        fileLastChange = file.lastModified();
        try {
            heads = new ArrayList<Head>();
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.startsWith("#") || line.length() == 0)
                    continue;

                Head head = new Head();

                String[] columns = line.split(", ");
                for(String s:columns) {
                    String key = s.substring(0, s.indexOf(":"));
                    String value = s.substring(s.indexOf(":") + 2);

                    if(key.equals("name")) head.name = value;
                    if(key.equals("user")) head.user = value;
                    if(key.equals("cost")) {
                        value = value.toUpperCase();

                        Material material = Material.getMaterial( value );

                        if(material != null) {
                            head.cost = new ItemStack(material, 1);
                        } else
                            Bukkit.broadcastMessage( value );
                    }
                }
                heads.add(head);

            }
            scanner.close();
        } catch(Exception e) {
            Bukkit.broadcastMessage( e.getMessage() );
        }
    }

    public List<MerchantRecipe> getColorTrades() {
        List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.WHITE_DYE,16),      8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.RED_DYE,16),        8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.ORANGE_DYE,16),     8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.PINK_DYE,16),       8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.YELLOW_DYE,16),     8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.LIME_DYE,16),       8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.GREEN_DYE,16),      8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.LIGHT_BLUE_DYE,16), 8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.CYAN_DYE,16),       8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.BLUE_DYE,16),       8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.MAGENTA_DYE,16),    8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.PURPLE_DYE,16),     8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.BROWN_DYE,16),      8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.GRAY_DYE,16),       8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.LIGHT_GRAY_DYE,16), 8 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.BLACK_DYE,16),      8 ));
        return recipes;
    }

    public List<MerchantRecipe> getHorseTrades() {
        List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,10), null, new ItemStack(Material.SADDLE,1), 5 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD, 5), null, new ItemStack(Material.LEAD,2), 12 ));

        //Always armor
        recipes.add( createRecipe( new ItemStack(Material.EMERALD, 15), null, new ItemStack(Material.LEATHER_HORSE_ARMOR,1), 5 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD, 25), new ItemStack(Material.IRON_INGOT, 7), new ItemStack(Material.IRON_HORSE_ARMOR,1), 5 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD, 35), new ItemStack(Material.GOLD_INGOT, 7), new ItemStack(Material.GOLDEN_HORSE_ARMOR,1), 5 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD, 55), new ItemStack(Material.DIAMOND, 7), new ItemStack(Material.DIAMOND_HORSE_ARMOR,1), 5 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD, 1),  null, new ItemStack(Material.WHEAT,10), 5 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD, 10), null, new ItemStack(Material.GOLDEN_CARROT,4), 5 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD, 40), null, new ItemStack(Material.NAME_TAG,1), 5 ));
        return recipes;
    }

    public List<MerchantRecipe> getFishMongerTrades() {
        List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
        recipes.add( createRecipe( new ItemStack(Material.COD,10), null, new ItemStack(Material.EMERALD,1), -1 ));
        recipes.add( createRecipe( new ItemStack(Material.SALMON,10), null, new ItemStack(Material.EMERALD,1), -1 ));
        recipes.add( createRecipe( new ItemStack(Material.PUFFERFISH,5), null, new ItemStack(Material.EMERALD,1), -1 ));
        recipes.add( createRecipe( new ItemStack(Material.TROPICAL_FISH,1), null, new ItemStack(Material.EMERALD,1), -1 ));

        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.COOKED_COD,4), 32 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,10), null, new ItemStack(Material.FISHING_ROD,1), 32 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,2), null, new ItemStack(Material.BUCKET,1), 32 ));
        return recipes;
    }

    public List<MerchantRecipe> getMinerTrades() {
        // Pick a random depth for the miner
        int depth = (int)(Math.random() * 128)-64+1;
        List<MerchantRecipe> recipes = new ArrayList<MerchantRecipe>();
        //Items to sell to player, change price due to depth of miner?
        recipes.add( createRecipe( new ItemStack(Material.COAL,15), null, new ItemStack(Material.EMERALD,1), -1 ));
        recipes.add( createRecipe( new ItemStack(Material.COPPER_ORE,10), null, new ItemStack(Material.EMERALD,1), -1 ));
        if(depth < 64)
            recipes.add( createRecipe( new ItemStack(Material.LAPIS_LAZULI,2), null, new ItemStack(Material.EMERALD,1), -1 ));
        else
            recipes.add( createRecipe( new ItemStack(Material.LAPIS_LAZULI,4), null, new ItemStack(Material.EMERALD,1), -1 ));
        recipes.add( createRecipe( new ItemStack(Material.IRON_ORE,4), null, new ItemStack(Material.EMERALD,1), -1 ));
        if(depth < 30)
            recipes.add( createRecipe( new ItemStack(Material.GOLD_ORE,5), null, new ItemStack(Material.EMERALD,1), -1 ));
        else
            recipes.add( createRecipe( new ItemStack(Material.GOLD_ORE,10), null, new ItemStack(Material.EMERALD,1), -1 ));
        if(depth < 16)
            recipes.add( createRecipe( new ItemStack(Material.DIAMOND,1), null, new ItemStack(Material.EMERALD,2), -1 ));
        else
            recipes.add( createRecipe( new ItemStack(Material.DIAMOND,1), null, new ItemStack(Material.EMERALD,4), -1 ));

        recipes.add( createRecipe( new ItemStack(Material.FLINT,30), null, new ItemStack(Material.EMERALD,20), -1 ));

        //Basic Materials
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.COBBLESTONE,16), -1 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.GRANITE,16), -1 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.DIORITE,16), -1 ));
        recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.ANDESITE,16), -1 ));

        if(depth < 16) {
            //Deep miner
            recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.DEEPSLATE,8), -1 ));
            recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.TUFF,4), -1 ));
            recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.CALCITE,2), -1 ));
            recipes.add( createRecipe( new ItemStack(Material.EMERALD,1), null, new ItemStack(Material.POINTED_DRIPSTONE,2), -1 ));
        }
        if( Math.random() < .1 ) recipes.add( createRecipe( new ItemStack(Material.EMERALD,10), null, new ItemStack(Material.AXOLOTL_BUCKET,2), 8 ));
        if( Math.random() < .1 ) recipes.add( createRecipe( new ItemStack(Material.EMERALD,4), null, new ItemStack(Material.BIG_DRIPLEAF,2), 8 ));
        if( Math.random() < .1 ) recipes.add( createRecipe( new ItemStack(Material.EMERALD,5), null, new ItemStack(Material.AMETHYST_SHARD,1), 8 ));

        return recipes;
    }

    public MerchantRecipe createRecipe(ItemStack item1, ItemStack item2, ItemStack reward, int tradeLimit) {
        if(tradeLimit == -1) tradeLimit = Integer.MAX_VALUE;
        MerchantRecipe trade = new MerchantRecipe(reward, tradeLimit);
        if(item1 != null) trade.addIngredient(item1);
        if(item2 != null) trade.addIngredient(item2);
        return trade;
    }
}
