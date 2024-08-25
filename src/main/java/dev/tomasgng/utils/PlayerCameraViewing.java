package dev.tomasgng.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public record PlayerCameraViewing(Player player, Location location, Inventory inventory, int cameraId) {

    public void rollback() {
        player.teleport(location);
    }

}
