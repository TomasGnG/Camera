package dev.tomasgng.utils;

import dev.tomasgng.Camera;
import dev.tomasgng.config.ConfigDataProvider;
import dev.tomasgng.config.DataConfigManager;
import dev.tomasgng.config.MessageDataProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class CameraManager {

    private final MessageDataProvider message = Camera.getInstance().getMessageDataProvider();
    private final ConfigDataProvider config = Camera.getInstance().getConfigDataProvider();
    private final DataConfigManager data = Camera.getInstance().getDataConfigManager();

    private final List<String> playersInCameraMode = new ArrayList<>();
    private final TreeSet<CameraInstance> cameras = new TreeSet<>(Comparator.comparing(CameraInstance::id));

    private int switchTime;
    private int currentIndex = 0;

    private boolean cameraTitleAlreadyShown;

    public CameraManager() {
        switchTime = config.getCameraSwitchTime();
        cameras.addAll(data.getCameras());

        startCameraSwitchTimer();
    }

    private void reload() {
        switchTime = config.getCameraSwitchTime();
        cameras.clear();
        cameras.addAll(data.getCameras());
        startCameraSwitchTimer();
    }

    private void startCameraSwitchTimer() {
        Bukkit.getAsyncScheduler().cancelTasks(Camera.getInstance());

        Bukkit.getAsyncScheduler().runAtFixedRate(Camera.getInstance(), scheduledTask -> {
            playersInCameraMode.removeIf(name -> Bukkit.getPlayer(name) == null);

            if(cameras.isEmpty())
                return;

            for (String playerName : playersInCameraMode) {
                Player player = Bukkit.getPlayer(playerName);
                CameraInstance camera = getNextCamera();

                if(cameras.size() == 1) {
                    if(!cameraTitleAlreadyShown) {
                        Bukkit.getScheduler().runTask(Camera.getInstance(), () -> player.teleport(camera.location()));
                        player.showTitle(Title.title(camera.title(), Component.empty()));
                        cameraTitleAlreadyShown = true;
                        continue;
                    }

                    continue;
                }

                Bukkit.getScheduler().runTask(Camera.getInstance(), () -> player.teleport(camera.location()));
                player.showTitle(Title.title(camera.title(), Component.empty()));
            }
        }, 1, switchTime, TimeUnit.SECONDS);
    }

    public void createCamera(Player player, int id, Component title) {
        if(cameraExists(id)) {
            player.sendMessage(message.getCameraExisting(id));
            return;
        }

        CameraInstance cameraInstance = new CameraInstance(id, player.getLocation(), title);

        data.createCamera(cameraInstance);
        reload();

        player.sendMessage(message.getCameraCreated(id));
    }

    public void deleteCamera(Player player, int id) {
        if(!cameraExists(id)) {
            player.sendMessage(message.getCameraNotExisting(id));
            return;
        }

        data.deleteCamera(id);
        cameras.removeIf(x -> x.id() == id);
        player.sendMessage(message.getCameraDeleted(id));
    }

    public void teleportCamera(Player player, int id) {
        if(!cameraExists(id)) {
            player.sendMessage(message.getCameraNotExisting(id));
            return;
        }

        CameraInstance cameraInstance = cameras.stream().filter(x -> x.id() == id).findFirst().orElse(null);

        player.teleport(cameraInstance.location());
        player.sendMessage(message.getCameraTeleported(id));
    }

    public void showCameras(Player player) {
        reload();

        if(cameras.isEmpty()) {
            player.sendMessage(message.getCameraListEmpty());
            return;
        }

        List<Component> formattedComponents = new ArrayList<>();

        cameras.forEach(camera -> formattedComponents.add(message.getCameraListEntryFormatted(camera)));

        Component msg = Component.text("");

        for (Component component : formattedComponents) {
            msg = msg.append(component).appendNewline();
        }

        player.sendMessage(msg);
    }

    public void toggleCamera(Player player, Player target) {
        if(cameras.isEmpty()) {
            player.sendMessage(message.getCameraListEmpty());
            return;
        }

        if(playersInCameraMode.contains(target.getName())) {
            target.setGameMode(GameMode.SURVIVAL);
            target.setInvulnerable(false);
            target.setInvulnerable(false);
            target.setFlying(false);
            playersInCameraMode.remove(target.getName());

            player.sendMessage(message.getCameraToggledOff(player.getName()));
            return;
        }

        target.setGameMode(GameMode.SPECTATOR);
        target.setInvulnerable(true);
        target.setInvulnerable(true);
        target.setFlying(true);
        playersInCameraMode.add(target.getName());

        CameraInstance camera = cameras.toArray(new CameraInstance[0])[0];

        player.teleport(camera.location());
        player.showTitle(Title.title(camera.title(), Component.empty()));
        player.sendMessage(message.getCameraToggledOn(player.getName()));
    }

    public List<Integer> getCameraIds() {
        return cameras.stream().map(CameraInstance::id).toList();
    }

    public void reload(Player player) {
        reload();
        player.sendMessage(message.getCommandReload());
    }

    private CameraInstance getNextCamera() {
        if(currentIndex > cameras.size() - 1) {
            currentIndex = 0;
            currentIndex++;
            return cameras.toArray(new CameraInstance[0])[0];
        }

        CameraInstance camera = cameras.toArray(new CameraInstance[0])[currentIndex];
        currentIndex++;

        return camera;
    }

    public boolean cameraExists(int id) {
        return data.cameraExists(id);
    }
}
