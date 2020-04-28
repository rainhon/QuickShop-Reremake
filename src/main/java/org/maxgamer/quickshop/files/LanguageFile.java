package org.maxgamer.quickshop.files;

import io.github.portlek.configs.BukkitLinkedManaged;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.util.FileType;
import io.github.portlek.configs.util.MapEntry;
import org.jetbrains.annotations.NotNull;

@LinkedConfig(configs = {
    @Config(
        name = "tr",
        type = FileType.JSON,
        location = "%basedir%/QuickShop-Era",
        copyDefault = true,
        resourcePath = "lang/tr-TR"
    ),
    @Config(
        name = "en",
        type = FileType.JSON,
        location = "%basedir%/QuickShop-Era",
        copyDefault = true,
        resourcePath = "lang/en-US"
    )
})
public final class LanguageFile extends BukkitLinkedManaged {

    public LanguageFile(@NotNull final ConfigFile configFile) {
        super(configFile.plugin_language, MapEntry.from("config", configFile));
    }

    @NotNull
    public ConfigFile getConfigFile() {
        return (ConfigFile) pull("config").orElseThrow(() ->
            new IllegalStateException("LanguageFile class couldn't load correctly."));
    }

    @NotNull
    public String getPrefix() {
        return getConfigFile().plugin_prefix;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onLoad() {

    }

}
