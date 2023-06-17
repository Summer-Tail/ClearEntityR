package cn.konfan.clearentityr.config;

import cn.konfan.clearentityr.ClearEntityR;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class LanguageConfig {
    public static YamlConfiguration langYaml;
    private static File file = new File(ClearEntityR.getInstance().getDataFolder(), "messages.yml");

    static {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Cannot createNewFile messages.yml: " + e.getMessage(), e);
            }
        }
        langYaml = YamlConfiguration.loadConfiguration(file);
    }

    public static void reload() {
        langYaml = YamlConfiguration.loadConfiguration(file);
    }

    public static String getString(String path) {
        String str = "";
        try {
            str = ClearEntityR.convertColor(Objects.requireNonNull(getLangYaml().getString(path)).replaceAll("%prefix%", ClearEntityR.convertColor(getLangYaml().getString("Other.prefix"))));
        } catch (NullPointerException e) {
            throw new RuntimeException("Cannot find " + path + " field in message.yml error:" + e.getMessage());
        }
        return str;
    }

    public static YamlConfiguration getLangYaml() {
        return langYaml != null ? langYaml : YamlConfiguration.loadConfiguration(file);
    }
}
