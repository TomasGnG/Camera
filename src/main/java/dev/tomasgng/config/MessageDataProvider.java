package dev.tomasgng.config;

import dev.tomasgng.Camera;
import dev.tomasgng.utils.CameraInstance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.text.DecimalFormat;
import java.util.List;

import static dev.tomasgng.config.MessagePathProvider.*;

public class MessageDataProvider {

    private final MiniMessage mm = MiniMessage.miniMessage();
    private final MessageManager manager = Camera.getInstance().getMessageManager();
    private final DecimalFormat df = new DecimalFormat("#.##");

    public Component getCameraCreated(int id) {
        return replacePlaceholder(manager.getComponentValue(CAMERA_CREATED), "%id%", id);
    }

    public Component getCameraExisting(int id) {
        return replacePlaceholder(manager.getComponentValue(CAMERA_EXISTING), "%id%", id);
    }

    public Component getCameraNotExisting(int id) {
        return replacePlaceholder(manager.getComponentValue(CAMERA_NOT_EXISTING), "%id%", id);
    }

    public Component getCameraDeleted(int id) {
        return replacePlaceholder(manager.getComponentValue(CAMERA_DELETED), "%id%", id);
    }

    public Component getCameraTeleported(int id) {
        return replacePlaceholder(manager.getComponentValue(CAMERA_TELEPORTED), "%id%", id);
    }

    public Component getCameraToggledOn(String player) {
        return replacePlaceholder(manager.getComponentValue(CAMERA_TOGGLED_ON), "%player%", player);
    }

    public Component getCameraToggledOff(String player) {
        return replacePlaceholder(manager.getComponentValue(CAMERA_TOGGLED_OFF), "%player%", player);
    }
    public Component getCameraListEntryFormatted(CameraInstance camera) {
        var formatted = replacePlaceholder(manager.getComponentValue(CAMERA_LIST_FORMAT), "%id%", camera.id());

        formatted = replacePlaceholder(formatted, "%world%", camera.location().getWorld().getName());
        formatted = replacePlaceholder(formatted, "%x%", df.format(camera.location().getX()));
        formatted = replacePlaceholder(formatted, "%y%", df.format(camera.location().getY()));
        formatted = replacePlaceholder(formatted, "%z%", df.format(camera.location().getZ()));
        formatted = replacePlaceholder(formatted, "%title%", mm.serialize(camera.title()));

        return formatted;
    }

    public Component getCameraListEmpty() {
        return manager.getComponentValue(CAMERA_LIST_EMPTY);
    }

    public Component getCommandNoPermission() {
        return manager.getComponentValue(COMMAND_NO_PERMISSION);
    }

    public Component getCommandReload() {
        return manager.getComponentValue(COMMAND_RELOAD);
    }

    public Component getCommandInvalidId() {
        return manager.getComponentValue(COMMAND_INVALID_ID);
    }

    public Component getCommandPlayerOffline() {
        return manager.getComponentValue(COMMAND_PLAYER_OFFLINE);
    }

    public Component getCommandInvalidTitleFormat() {
        return manager.getComponentValue(COMMAND_INVALID_TITLE_FORMAT);
    }

    public Component getCommandHelp() {
        List<Component> components = manager.getStringListValue(COMMAND_HELP)
                                            .stream()
                                            .map(mm::deserialize)
                                            .toList();

        Component msg = Component.empty();

        for (Component component : components) {
            msg = msg.append(component).appendNewline();
        }

        return msg;
    }

    private Component replacePlaceholder(Component msg, String placeholder, Object value) {
        return mm.deserialize(mm.serialize(msg).replaceAll(placeholder, value.toString()));
    }

}
