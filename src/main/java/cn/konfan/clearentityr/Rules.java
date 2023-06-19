package cn.konfan.clearentityr;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Rules {

    /**
     * 实体清理规则
     *
     * @param entity 要清理的实体理
     * @return 是否匹配
     */
    public static boolean getEntityRules(Entity entity) {

        // 判断该实体是否是玩家
        if (entity instanceof Player) {
            return false;
        }

        // 获取实体 注册ID
        String saveID = ClearEntityR.Nms.getSaveID(entity);

        // 判断注册ID 是否获取成功
        if (StringUtils.isBlank(saveID)) {
            return false;
        }

        // 判断实体上是否有玩家
        if (entity.getPassengers().size() > 0) {
            for (int i = 0; i < entity.getPassengers().size(); i++) {
                if (entity.getPassengers().get(0) instanceof Player) {
                    return false;
                }
            }
        }

        ConfigurationSection config = getConfig(entity);

        // 判断命名实体
        if (!config.getBoolean("Rules.name")) {
            if (saveID.startsWith("minecraft:") && StringUtils.isNotBlank(entity.getCustomName())) {
                return false;
            }
        }

        List<String> black = config.getStringList("black");
        List<String> white = config.getStringList("white");
        ClearEntityR.getInstance().getLogger().info(black.toString());

        if (white.contains(saveID.toLowerCase())) {
            return false;
        }

        if (white.contains("animals") && entity instanceof Animals) {
            return false;
        }

        if (white.contains("monster") && entity instanceof Monster) {
            return false;
        }

        if (black.contains(saveID.toLowerCase())) {
            return true;
        }

        if (black.contains("animals") && entity instanceof Animals) {
            return true;
        }

        return black.contains("monster") && entity instanceof Monster;
    }


    /**
     * 物品清理规则
     *
     * @param entity 要清理的物品
     * @return 是否匹配
     */
    public static boolean getItemRules(Entity entity) {

        ItemStack itemStack = null;
        if (entity instanceof Item) {
            itemStack = ((Item) entity).getItemStack();
        } else {
            return false;
        }


        // 是否开启掉落物清理
        if (!ClearEntityR.getInstance().getConfig().getBoolean("Rules.item.enable")) {
            return false;
        }

        //获取物品 注册ID
        String itemID = ClearEntityR.Nms.getItemID(itemStack);

        // 判断注册ID 是否获取成功
        if (StringUtils.isBlank(itemID)) {
            return false;
        }

        //读取配置文件
        ConfigurationSection config = getConfig(entity);







        return false;
    }


    /**
     * 获取自定义世界 Config 或 通用 Config
     *
     * @param entity 用于判断实体所在世界
     * @return ConfigurationSection
     */
    private static ConfigurationSection getConfig(Entity entity) {
        ConfigurationSection worldConfig = ClearEntityR.getInstance().getConfig().getConfigurationSection("Rules.custom." + entity.getWorld().getName());
        return worldConfig == null ? ClearEntityR.getInstance().getConfig().getConfigurationSection("Rules") : worldConfig;
    }


}
