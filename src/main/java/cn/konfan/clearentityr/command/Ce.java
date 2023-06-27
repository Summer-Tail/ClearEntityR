package cn.konfan.clearentityr.command;

import cn.konfan.clearentityr.ClearEntityR;
import cn.konfan.clearentityr.config.LanguageConfig;
import cn.konfan.clearentityr.task.ClearTimer;
import cn.konfan.clearentityr.task.EntityClear;
import cn.konfan.clearentityr.utils.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Ce implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        return commandParse(commandSender, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }

    private boolean commandParse(CommandSender commandSender, String[] args) {

        if (args.length == 0) {
            help(commandSender);
            return false;
        }
        StringBuilder command = new StringBuilder();
        for (String arg : args) {
            command.append(arg).append(" ");
        }
        command.deleteCharAt(command.length() - 1);

        if (command.toString().equalsIgnoreCase("help")) {
            help(commandSender);
            return true;
        }
        if (command.toString().equalsIgnoreCase("reload")) {
            reload(commandSender);
            return true;
        }
        if (command.toString().equalsIgnoreCase("clear")) {
            clear(false);
            return true;
        }
        if (command.toString().equalsIgnoreCase("clear true")) {
            clear(true);
            return true;
        }
        if (command.toString().equalsIgnoreCase("t")) {
            test(commandSender);
            return true;
        }

        return false;
    }

    private void help(CommandSender sender) {
        sender.sendMessage(LanguageConfig.getString("help"));
    }

    private void reload(CommandSender sender) {
        ClearEntityR.getInstance().reloadConfig();
        LanguageConfig.reload();
        sender.sendMessage(LanguageConfig.getString("reload"));
    }

    private void clear(boolean now) {
        if (now) {
            new EntityClear().run();
        } else {
            new ClearTimer().clearStart();
        }

    }

    private void test(CommandSender sender) {
        Player player = (Player) sender;
        player.getInventory().addItem(new ItemStackFactory(Material.DIAMOND_PICKAXE).setLore(Arrays.asList("不被清理", "测试物品")).toItemStack());
    }

}
