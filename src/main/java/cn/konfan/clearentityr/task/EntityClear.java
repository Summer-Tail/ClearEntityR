package cn.konfan.clearentityr.task;

import cn.konfan.clearentityr.ClearEntityR;
import cn.konfan.clearentityr.Rules;
import cn.konfan.clearentityr.config.LanguageConfig;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClear implements Runnable {
    private final List<Chunk> chunks;
    private final ConfigurationSection config;
    private int clearEntityNum = 0;
    private int clearItemNum = 0;

    public EntityClear() {
        config = ClearEntityR.getInstance().getConfig();
        chunks = getLoadChunksList();
    }

    public EntityClear(List<Chunk> chunks, int clearEntityNum, int clearItemNum) {
        config = ClearEntityR.getInstance().getConfig();
        this.clearEntityNum = clearEntityNum;
        this.clearItemNum = clearItemNum;
        this.chunks = chunks;
    }

    @Override
    public void run() {
        clear(chunks, config.getInt("Limit.chunk", 100), clearEntityNum, clearItemNum);
    }

    /**
     * 清理实体任务
     *
     * @param clearList      清理的区块列表
     * @param num            清理的区块数量
     * @param clearEntityNum 已经清理的实体数量
     * @param clearItemNum   已经清理的掉落物数量
     */
    public void clear(List<Chunk> clearList, int num, int clearEntityNum, int clearItemNum) {

        for (int i = Math.min(clearList.size() - 1, num); i >= 0; i--) {
            for (Entity entity : clearList.get(i).getEntities()) {
                if (entity instanceof Item && Rules.getItemRules(entity)) {
                        entity.remove();
                        clearItemNum++;
                } else if (Rules.getEntityRules(entity)) {
                    entity.remove();
                    clearEntityNum++;
                }
            }
            clearList.remove(i);
        }
        if (!clearList.isEmpty()) {
            Bukkit.getScheduler().runTaskLater(ClearEntityR.getInstance(), new EntityClear(clearList, clearEntityNum, clearItemNum), 2L);
        } else {
            Bukkit.getServer().broadcastMessage(LanguageConfig.getString("Clear.complete")
                    .replaceAll("%ITEMCOUNT%", String.valueOf(clearItemNum)).replaceAll("%COUNT%", String.valueOf(clearEntityNum)));
        }
    }


    /**
     * 获取所有加载中的区块列表
     *
     * @return 区块列表
     */
    public List<Chunk> getLoadChunksList() {
        ArrayList<Chunk> list = new ArrayList<>();
        Bukkit.getWorlds().forEach(world -> list.addAll(Arrays.asList(world.getLoadedChunks())));
        return list;
    }

}
