package cn.konfan.clearentityr;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

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
        if (!entity.getPassengers().isEmpty()) {
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

        if (white.contains(saveID.toLowerCase())) {
            return false;
        }

        if (black.contains(saveID.toLowerCase())) {
            return true;
        }


        // 模组实体不按照以下处理
        if (config.getBoolean("mode") && !saveID.startsWith("minecraft:")) {
            return false;
        }

        // 通用规则匹配
        if (white.contains("monster") && entity instanceof Monster) {
            return false;
        }

        if (white.contains("animals") && entity instanceof Animals) {
            return false;
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

        ItemStack itemStack;
        if (entity instanceof Item) {
            itemStack = ((Item) entity).getItemStack();
        } else {
            return false;
        }


        //读取配置文件
        ConfigurationSection config = getConfig(entity);

        // 是否开启掉落物清理
        if (!config.getBoolean("item.enable")) {
            return false;
        }

        // 获取物品 注册ID
        String itemID = ClearEntityR.Nms.getItemID(itemStack);

        // 判断注册ID 是否获取成功
        if (StringUtils.isBlank(itemID)) {
            return false;
        }

        // 获取item节点下全部内容 排除enable
        Set<String> item = config.getConfigurationSection("item").getKeys(false);
        item.remove("enable");


        // 循环遍历全部节点
        for (String param : item) {

            ConfigurationSection node = config.getConfigurationSection("item." + param);
            Set<String> keys = node.getKeys(false);


            for (String key : keys) {

                if (key.equals("id") && !itemID.equals(node.getString("id"))) {
                    return true;
                }

                if (key.equals("name") && !itemRulesEquals(node.getString("name"), itemStack.getItemMeta().getDisplayName())) {
                    return true;
                }

                if (!key.equals("lore")) {
                    continue;
                }

                List<String> rulesLore = node.getStringList("lore");
                List<String> itemLore = itemStack.getItemMeta().getLore();
                if (itemLore == null) {
                    return true;
                }

                for (String lore : rulesLore) {

                    for (int i = 0; i < itemLore.size(); i++) {

                        if (itemRulesEquals(lore, itemLore.get(i))) {
                            break;
                        }
                        if (itemLore.size() - 1 == i) {
                            return true;
                        }

                    }

                }


            }


            return false;
        }


        return true;
    }

    private static boolean itemRulesEquals(String name, String value) {
        if (name.startsWith("*")) {
            return value.contains(name.replace("*", ""));
        } else {
            return name.equals(value);
        }

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
