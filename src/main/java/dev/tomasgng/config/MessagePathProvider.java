package dev.tomasgng.config;

import dev.tomasgng.config.utils.ConfigPair;

import java.util.List;

public class MessagePathProvider {
    
    public static ConfigPair CAMERA_CREATED = new ConfigPair("camera.created", "<green>You created a camera with the id %id%");
    public static ConfigPair CAMERA_EXISTING = new ConfigPair("camera.existing", "<red>Camera %id% is already existing.");
    public static ConfigPair CAMERA_NOT_EXISTING = new ConfigPair("camera.notExisting", "<red>Camera %id% is not existing.");
    public static ConfigPair CAMERA_DELETED = new ConfigPair("camera.deleted", "<green>You deleted the camera with the id %id%");
    public static ConfigPair CAMERA_TELEPORTED = new ConfigPair("camera.teleported", "<green>You have been teleported to the camera %id%");
    public static ConfigPair CAMERA_TOGGLED_ON = new ConfigPair("camera.toggleOn", "<green>The camera mode is now activated for %player%");
    public static ConfigPair CAMERA_TOGGLED_OFF = new ConfigPair("camera.toggleOff", "<green>The camera mode is now deactivated for %player%");
    public static ConfigPair CAMERA_LIST_FORMAT = new ConfigPair("camera.listFormat", "<gray>[<green>Camera %id%<gray>] %world%(X: %x%, Y: %y%, Z: %z%), Title: %title%");
    public static ConfigPair CAMERA_LIST_EMPTY = new ConfigPair("camera.listEmpty", "<red>There are no cameras yet.");
    public static ConfigPair COMMAND_NO_PERMISSION = new ConfigPair("command.noPermission", "<red>You don't have permission!");
    public static ConfigPair COMMAND_RELOAD = new ConfigPair("command.reload", "<green>Reloaded config.");
    public static ConfigPair COMMAND_INVALID_ID = new ConfigPair("command.invalidId", "<red>The id must be a positive number.");
    public static ConfigPair COMMAND_PLAYER_OFFLINE = new ConfigPair("command.playerOffline", "<red>This player is offline.");
    public static ConfigPair COMMAND_INVALID_TITLE_FORMAT = new ConfigPair("command.invalidTitleFormat", "<red>The title must be in MiniMessage format.");
    public static ConfigPair COMMAND_HELP = new ConfigPair("command.help", List.of(" ",
                                                                                   "<green>/camera create <id> <title> <gray>| Create cameras",
                                                                                   "<green>/camera delete <id> <gray>| Delete cameras",
                                                                                   "<green>/camera teleport <id> <gray>| Teleport to camera",
                                                                                   "<green>/camera toggle <player> <gray>| Toggles camera mode for player",
                                                                                   "<green>/camera list <gray>| Show all cameras",
                                                                                   "<green>/camera reload <gray>| Reload config",
                                                                                   " "));

}
