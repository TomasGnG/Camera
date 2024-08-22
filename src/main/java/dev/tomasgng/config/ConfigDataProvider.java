package dev.tomasgng.config;

import dev.tomasgng.Camera;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static dev.tomasgng.config.ConfigPathProvider.*;

public class ConfigDataProvider {

    private final ConfigManager manager = Camera.getInstance().getConfigManager();

    public int getCameraSwitchTime() {
        int switchTime = manager.getIntegerValue(ConfigPathProvider.CAMERA_SWITCH_TIME);

        if(switchTime < 1) {
            Camera.getInstance().getLogger().severe("The switch time cannot be less than 1! Using default value(5) for camera switch time...");
            return 5;
        }

        return switchTime;
    }

    public String getCommandPermission() {
        return manager.getStringValue(COMMAND_PERMISSION);
    }

    public String getLanguageFileName() {
        return manager.getStringValue(LANGUAGE_FILE);
    }

    public Component getPlayerHeadDisplayname() {
        final MiniMessage mm = MiniMessage.miniMessage();
        String raw = manager.getStringValue(PLAYER_HEAD_DISPLAYNAME);

        return mm.deserializeOr(raw, mm.deserialize(PLAYER_HEAD_DISPLAYNAME.getStringValue()));
    }

    public String getPlayerHeadTexture() {
        return manager.getStringValue(PLAYER_HEAD_TEXTURE);
    }
}
