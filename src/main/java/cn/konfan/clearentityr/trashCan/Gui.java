package cn.konfan.clearentityr.trashCan;

import cn.konfan.clearentityr.config.LanguageConfig;
import cn.konfan.clearentityr.utils.ItemStackFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class Gui {

    ArrayList<Inventory> gui = new ArrayList<>();



    private void createTrashCan(int page){
        for (int i = 0; i < page; i++) {
            Inventory inventory = Bukkit.createInventory
                    (null, 54, LanguageConfig.getString
                            ("TrashCan.title").replace("%PAGE%", page + ""));
            inventory.addItem(new ItemStackFactory(Material.PAPER).setDisplayName("").toItemStack());
            gui.add(inventory);
        }




    }

}
