package cn.konfan.clearentityr.task;

import cn.konfan.clearentityr.ClearEntityR;
import cn.konfan.clearentityr.config.LanguageConfig;
import cn.konfan.clearentityr.utils.BossBarUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;

import java.util.Collections;
import java.util.List;

public class ClearTimer implements Runnable {
    private static int time;
    private static boolean maxTask = false;

    @Override
    public void run() {

        time++;

        FileConfiguration config = ClearEntityR.getInstance().getConfig();

        int entitySize = getEntitySize();


        // 达到配置上限，立即执行清理任务
        if (time > config.getInt("Time") && (entitySize >= config.getInt("Limit.min") || config.getInt("Limit.min") == -1)) {
            time = 0;
            maxTask = false;
            clearStart();
            return;
        }


        // 实体数量超过配置上限，立即执行清理任务
        if (entitySize >= config.getInt("Limit.max") && config.getInt("Limit.max") != -1 && !maxTask) {
            time = 0;
            maxTask = true;
            clearStart();
        }
    }

    private int getEntitySize() {
        int num = 0;
        for (World world : Bukkit.getServer().getWorlds()) {
            List<Entity> entities = world.getEntities();
            if (entities != null){
                num += entities.size();
            }
        }
        return num - Bukkit.getOnlinePlayers().size();
    }


    public void clearStart() {
        List<Integer> timeList = ClearEntityR.getInstance().getConfig().getIntegerList("Message.time");
        Collections.sort(timeList);
        int maxTime = timeList.get(timeList.size() - 1);

        Bukkit.getServer().broadcastMessage(LanguageConfig.getString("Clear.beforeInfo").replaceAll("%TIME%", String.valueOf(maxTime)));
        for (int i = timeList.size() - 2; i >= 0; i--) {
            int finalI = i;
            Bukkit.getScheduler().runTaskLater(ClearEntityR.getInstance(), () ->
                    Bukkit.getServer().broadcastMessage(LanguageConfig.getString("Clear.beforeInfo").
                            replaceAll("%TIME%", String.valueOf(timeList.get(finalI)))), (maxTime - timeList.get(i)) * 20L);
        }

        Bukkit.getScheduler().runTaskLater(ClearEntityR.getInstance(), new EntityClear(), maxTime * 20L);
        if (ClearEntityR.getInstance().getConfig().getBoolean("Message.bossBar")) {
            new BossBarUtils().sendBossBar("Title", BarColor.RED, BarStyle.SOLID, timeList.get(timeList.size() - 1) + 1);
        }
    }
}
