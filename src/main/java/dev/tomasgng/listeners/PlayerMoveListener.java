package dev.tomasgng.listeners;

import dev.tomasgng.Camera;
import dev.tomasgng.utils.CameraManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final CameraManager cameraManager = Camera.getInstance().getCameraManager();

    @EventHandler
    public void on(PlayerMoveEvent event) {
        if(cameraManager.isPlayerInCameraMode(event.getPlayer()) || cameraManager.isPlayerViewingCamera(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }
    }

}