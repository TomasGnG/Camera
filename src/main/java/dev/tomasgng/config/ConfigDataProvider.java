package dev.tomasgng.config;

import dev.tomasgng.Camera;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;

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

    public GameMode getCameraModeGamemode() {
        String gamemodeStr = manager.getStringValue(CAMERA_MODE_GAMEMODE).toUpperCase();

        try {
            return GameMode.valueOf(gamemodeStr);
        } catch (IllegalArgumentException e) {
            Camera.getInstance().getLogger().severe(gamemodeStr + " is not a valid Gamemode! Using the default gamemode...");
            return GameMode.valueOf(CAMERA_MODE_GAMEMODE.getStringValue());
        }
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
