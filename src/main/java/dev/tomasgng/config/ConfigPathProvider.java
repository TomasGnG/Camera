package dev.tomasgng.config;

import dev.tomasgng.config.utils.ConfigPair;

public class ConfigPathProvider {

    public static ConfigPair CAMERA_SWITCH_TIME = new ConfigPair("cameraSwitchTime", 5, "Switch time in seconds");
    public static ConfigPair COMMAND_PERMISSION = new ConfigPair("commandPermission", "camera.use");

}
