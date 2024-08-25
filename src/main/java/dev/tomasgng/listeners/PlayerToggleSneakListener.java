package dev.tomasgng.listeners;

import dev.tomasgng.Camera;
import dev.tomasgng.utils.CameraManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerToggleSneakListener implements Listener {

    private final CameraManager cameraManager = Camera.getInstance().getCameraManager();

    @EventHandler
    public void on(PlayerToggleSneakEvent event) {
        if(!cameraManager.isPlayerViewingCamera(event.getPlayer()))
            return;

        cameraManager.toggleViewCamera(event.getPlayer(), -1);
    }

}