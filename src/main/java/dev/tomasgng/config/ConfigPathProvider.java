package dev.tomasgng.config;

import dev.tomasgng.config.utils.ConfigPair;
import org.bukkit.GameMode;

public class ConfigPathProvider {

    public static ConfigPair CAMERA_SWITCH_TIME = new ConfigPair("cameraSwitchTime", 5, "Switch time in seconds");
    public static ConfigPair COMMAND_PERMISSION = new ConfigPair("commandPermission", "camera.use");
    public static ConfigPair LANGUAGE_FILE = new ConfigPair("languageFile", "messages.yml");
    public static ConfigPair CAMERA_MODE_GAMEMODE = new ConfigPair("cameraModeGamemode", GameMode.SPECTATOR.name(), "The players gamemode when he's in the Camera mode");
    public static ConfigPair PLAYER_HEAD_DISPLAYNAME = new ConfigPair("playerHead.displayname", "<gold><b><u>Camera", "Players will get this player head equiped if they are in the camera mdde.");
    public static ConfigPair PLAYER_HEAD_TEXTURE = new ConfigPair("playerHead.texture", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ0MjJhODJjODk5YTljMTQ1NDM4NGQzMmNjNTRjNGFlN2ExYzRkNzI0MzBlNmU0NDZkNTNiOGIzODVlMzMwIn19fQ==", "Base64 value of heads");

}
