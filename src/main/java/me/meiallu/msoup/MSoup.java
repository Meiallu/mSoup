package me.meiallu.msoup;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;

public final class MSoup extends JavaPlugin implements Listener {
    private ProtocolManager manager;

    @Override
    public void onEnable() {
        manager = ProtocolLibrary.getProtocolManager();
        getServer().getPluginManager().registerEvents(this, this);
        createRecipe(new ItemStack(Material.CACTUS));
        ItemStack i = new ItemStack(Material.INK_SACK);
        i.setDurability((short) 3);
        createRecipe(i);
        createRecipe(new ItemStack(Material.NETHER_STALK));
        createRecipe(new ItemStack(Material.MELON_SEEDS));
        createRecipe(new ItemStack(Material.PUMPKIN_SEEDS));
    }

    @EventHandler
    public void onClickSoup(PlayerInteractEvent e) {
        if (e.getPlayer().getItemInHand().getType() == Material.MUSHROOM_SOUP) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getPlayer().getHealth() < e.getPlayer().getMaxHealth()) {
                    double dif = e.getPlayer().getMaxHealth() - e.getPlayer().getHealth();
                    e.setCancelled(true);
                    e.getPlayer().setItemInHand(new ItemStack(Material.BOWL));

                    if (dif > 7.0d) {
                        e.getPlayer().setHealth(e.getPlayer().getHealth() + 7.0d);
                        setBar(e.getPlayer(), 3.5);
                    } else {
                        e.getPlayer().setHealth(e.getPlayer().getMaxHealth());
                        setBar(e.getPlayer(), dif / 2);
                    }
                } if (e.getPlayer().getFoodLevel() < 20) {
                    double dif = 20 - e.getPlayer().getFoodLevel();
                    e.setCancelled(true);
                    e.getPlayer().setItemInHand(new ItemStack(Material.BOWL));

                    if (dif > 7) {
                        e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel() + 7);
                    } else {
                        e.getPlayer().setFoodLevel(20);
                    }
                }
            }
        }
    }

    public void setBar(Player p, double v) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.CHAT);
        packet.getBytes().write(0, (byte) 2);
        DecimalFormat df = new DecimalFormat("#.0");
        packet.getChatComponents().write(0, WrappedChatComponent.fromJson( ChatColor.RED + df.format(v) ) );
        manager.sendServerPacket(p, packet);
    }

    void createRecipe(ItemStack i) {
        ItemStack Stack = new ItemStack(Material.MUSHROOM_SOUP, 1);
        ShapelessRecipe Recipe = new ShapelessRecipe(Stack);
        Recipe.addIngredient(1, i.getData());
        Recipe.addIngredient(1, Material.BOWL);
        getServer().addRecipe(Recipe);
    }
}