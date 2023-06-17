package cn.konfan.clearentityr.task;

import cn.konfan.clearentityr.ClearEntityR;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClear implements Runnable {
    private final List<Chunk> chunks;

    public EntityClear() {
        chunks = getLoadChunksList();
    }

    public EntityClear(List<Chunk> chunks) {
        this.chunks = chunks;
    }

    @Override
    public void run() {
        clear(chunks,100);
    }

    public void clear(List<Chunk> clearList, int num) {
        for (int i = Math.min(clearList.size() - 1,num); i >= 0; i--) {
            for (Entity entity : clearList.get(i).getEntities()) {
                entity.remove();
            }
            clearList.remove(i);
        }

        if (clearList.size() != 0) {
            Bukkit.getScheduler().runTaskLater(ClearEntityR.getInstance(), new EntityClear(clearList), 2L);
        }
    }


    /**
     * 获取所有加载中的区块列表
     *
     * @return 区块列表
     */
    public List<Chunk> getLoadChunksList() {
        ArrayList<Chunk> list = new ArrayList<Chunk>();
        Bukkit.getWorlds().forEach(world -> {
            list.addAll(Arrays.asList(world.getLoadedChunks()));
        });
        return list;
    }

}
