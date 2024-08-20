package dev.tomasgng.config;

import dev.tomasgng.Camera;

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
        return manager.getStringValue(ConfigPathProvider.COMMAND_PERMISSION);
    }
}
