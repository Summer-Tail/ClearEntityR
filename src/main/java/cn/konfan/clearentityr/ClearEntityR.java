package cn.konfan.clearentityr;

import cn.konfan.clearentityr.command.Ce;
import cn.konfan.clearentityr.nms.Nms;
import cn.konfan.clearentityr.utils.ItemStackFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class ClearEntityR extends JavaPlugin {

    private static ClearEntityR Instance;
    public static Nms Nms = null;

    private void nmsInit() {
        World world = Bukkit.getWorlds().get(0);
        Entity tempEntity = world.spawnEntity(world.getSpawnLocation(), EntityType.EGG);
        String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        try {
            Nms = (Nms) Class.forName("cn.konfan.clearentityr.nms." + version).newInstance();
            Nms.getSaveID(tempEntity);
            Nms.getItemID(new ItemStackFactory(Material.EGG).toItemStack());
            getLogger().info("Nms Version: " + version);
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Nms load fail you Version: " + version);
            e.printStackTrace();
        } finally {
            tempEntity.remove();
        }

    }


    @Override
    public void onEnable() {
        Instance = this;
        // Plugin startup logic
        nmsInit();

        getCommand("clearentity").setExecutor(new Ce());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ClearEntityR getInstance() {
        return Instance;
    }

    public static String convertColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
