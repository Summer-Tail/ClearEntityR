package cn.konfan.clearentityr.config;

import cn.konfan.clearentityr.ClearEntityR;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class LanguageConfig {
    public static YamlConfiguration langYaml;
    private static final File file = new File(ClearEntityR.getInstance().getDataFolder(), "messages.yml");

    static {
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    langYaml = YamlConfiguration.loadConfiguration(file);
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot createNewFile messages.yml: " + e.getMessage(), e);
            }
        }

    }

    public static void reload() {
        langYaml = YamlConfiguration.loadConfiguration(file);
    }

    public static String getString(String path) {
        String str;
        try {
            str = ClearEntityR.convertColor(Objects.requireNonNull(getLangYaml().getString(path)).replaceAll("%prefix%", ClearEntityR.convertColor(getLangYaml().getString("prefix"))));
        } catch (NullPointerException e) {
            throw new RuntimeException("Cannot find " + path + " field in message.yml error:" + e.getMessage());
        }
        return str;
    }

    public static YamlConfiguration getLangYaml() {
        return langYaml != null ? langYaml : YamlConfiguration.loadConfiguration(file);
    }
}
