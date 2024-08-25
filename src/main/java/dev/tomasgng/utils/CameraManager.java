package dev.tomasgng.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.tomasgng.Camera;
import dev.tomasgng.config.ConfigDataProvider;
import dev.tomasgng.config.DataConfigManager;
import dev.tomasgng.config.MessageDataProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class CameraManager {

    private final MessageDataProvider message = Camera.getInstance().getMessageDataProvider();
    private final ConfigDataProvider config = Camera.getInstance().getConfigDataProvider();
    private final DataConfigManager data = Camera.getInstance().getDataConfigManager();
    private final BukkitAudiences adventure = Camera.getInstance().getAdventure();

    private final List<String> playersInCameraMode = new ArrayList<>();
    private final TreeSet<CameraInstance> cameras = new TreeSet<>(Comparator.comparing(CameraInstance::id));
    private final List<PlayerCameraViewing> playersViewingCamera = new ArrayList<>();

    private int switchTime;
    private int currentIndex = 0;

    private boolean cameraTitleAlreadyShown;

    public CameraManager() {
        reload();
    }

    private void reload() {
        message.setLanguageFileName(config.getLanguageFileName());
        switchTime = config.getCameraSwitchTime();
        cameras.clear();
        cameras.addAll(data.getCameras());
        startCameraSwitchTimer();
    }

    private void startCameraSwitchTimer() {
        Bukkit.getScheduler().cancelTasks(Camera.getInstance());

        Bukkit.getScheduler().runTaskTimer(Camera.getInstance(), scheduledTask -> {
            playersInCameraMode.removeIf(name -> Bukkit.getPlayer(name) == null);

            if(cameras.isEmpty())
                return;

            for (String playerName : playersInCameraMode) {
                Player player = Bukkit.getPlayer(playerName);
                CameraInstance camera = getNextCamera();

                if(cameras.size() == 1) {
                    if(!cameraTitleAlreadyShown) {
                        Bukkit.getScheduler().runTask(Camera.getInstance(), () -> player.teleport(camera.location()));
                        adventure.player(player).showTitle(Title.title(camera.title(), Component.empty()));
                        cameraTitleAlreadyShown = true;
                        continue;
                    }

                    continue;
                }

                Bukkit.getScheduler().runTask(Camera.getInstance(), () -> player.teleport(camera.location()));
                adventure.player(player).showTitle(Title.title(camera.title(), Component.empty()));
            }
        }, 20, switchTime * 20);
    }

    public void createCamera(Player player, int id, Component title) {
        if(cameraExists(id)) {
            adventure.player(player).sendMessage(message.getCameraExisting(id));
            return;
        }

        CameraInstance cameraInstance = new CameraInstance(id, player.getLocation(), title);

        data.createCamera(cameraInstance);
        reload();

        adventure.player(player).sendMessage(message.getCameraCreated(id));
    }

    public void deleteCamera(Player player, int id) {
        if(!cameraExists(id)) {
            adventure.player(player).sendMessage(message.getCameraNotExisting(id));
            return;
        }

        data.deleteCamera(id);
        cameras.removeIf(x -> x.id() == id);
        adventure.player(player).sendMessage(message.getCameraDeleted(id));
    }

    public void teleportCamera(Player player, int id) {
        if(!cameraExists(id)) {
            adventure.player(player).sendMessage(message.getCameraNotExisting(id));
            return;
        }

        CameraInstance cameraInstance = cameras.stream().filter(x -> x.id() == id).findFirst().orElse(null);

        player.teleport(cameraInstance.location());
        adventure.player(player).sendMessage(message.getCameraTeleported(id));
    }

    public void listAllCameras(Player player) {
        reload();

        if(cameras.isEmpty()) {
            adventure.player(player).sendMessage(message.getCameraListEmpty());
            return;
        }

        List<Component> formattedComponents = new ArrayList<>();

        cameras.forEach(camera -> formattedComponents.add(message.getCameraListEntryFormatted(camera)));

        Component msg = Component.text("");

        for (Component component : formattedComponents) {
            msg = msg.append(component).appendNewline();
        }

        adventure.player(player).sendMessage(msg);
    }

    public void toggleCamera(Player player, Player target) {
        if(cameras.isEmpty()) {
            adventure.player(player).sendMessage(message.getCameraListEmpty());
            return;
        }

        if(playersInCameraMode.contains(target.getName())) {
            target.setGameMode(GameMode.SURVIVAL);
            target.setInvisible(false);
            target.setInvulnerable(false);
            target.setFlying(false);
            target.getEquipment().setHelmet(null, true);

            playersInCameraMode.remove(target.getName());

            adventure.player(player).sendMessage(message.getCameraToggledOff(player.getName()));
            return;
        }

        target.setGameMode(config.getCameraModeGamemode());
        target.setInvisible(true);
        target.setInvulnerable(true);
        target.setFlying(true);
        target.getEquipment().setHelmet(getHead(), true);

        playersInCameraMode.add(target.getName());

        CameraInstance camera = cameras.toArray(new CameraInstance[0])[0];

        player.teleport(camera.location());
        adventure.player(player).showTitle(Title.title(camera.title(), Component.empty()));
        adventure.player(player).sendMessage(message.getCameraToggledOn(player.getName()));
    }

    private ItemStack getHead() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");

        profile.getProperties().put("textures", new Property("textures", config.getPlayerHeadTexture()));

        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        meta.setDisplayName(LegacyComponentSerializer.legacyAmpersand().serialize(config.getPlayerHeadDisplayname()));

        item.setItemMeta(meta);

        return item;
    }

    public void toggleViewCamera(Player player, int id) {
        if(isPlayerViewingCamera(player)) {
            PlayerCameraViewing entry = playersViewingCamera.stream()
                                                            .filter(x -> x.player().getName().equals(player.getName()))
                                                            .findFirst()
                                                            .get();

            entry.rollback();
            playersViewingCamera.remove(entry);
            player.setGameMode(GameMode.SURVIVAL);
            player.setInvisible(false);
            player.setInvulnerable(false);
            player.setFlying(false);

            adventure.player(player).sendMessage(message.getCameraViewModeToggledOff(entry.cameraId()));
            return;
        }

        if(!cameraExists(id)) {
            adventure.player(player).sendMessage(message.getCameraNotExisting(id));
            return;
        }

        PlayerCameraViewing cameraViewing = new PlayerCameraViewing(player, player.getLocation(), player.getInventory(), id);
        playersViewingCamera.add(cameraViewing);

        player.teleport(cameras.stream().filter(x -> x.id() == id).findFirst().get().location());
        player.setGameMode(config.getCameraModeGamemode());
        player.setInvisible(true);
        player.setInvulnerable(true);
        player.setFlying(true);

        adventure.player(player).sendMessage(message.getCameraViewModeToggledOn(id));
    }

    public List<Integer> getCameraIds() {
        return cameras.stream().map(CameraInstance::id).toList();
    }

    public void reload(Player player) {
        reload();
        adventure.player(player).sendMessage(message.getCommandReload());
    }

    public boolean cameraExists(int id) {
        return data.cameraExists(id);
    }

    public boolean isPlayerViewingCamera(Player player) {
        return playersViewingCamera.stream().anyMatch(x -> x.player().getName().equals(player.getName()));
    }

    public boolean isPlayerInCameraMode(Player player) {
        return playersInCameraMode.contains(player.getName());
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
}
