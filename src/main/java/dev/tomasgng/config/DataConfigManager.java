package dev.tomasgng.config;

import dev.tomasgng.Camera;
import dev.tomasgng.utils.CameraInstance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataConfigManager {

    private final MiniMessage mm = MiniMessage.miniMessage();
    private final File folder = new File("plugins/Camera");
    private final File dataFile = new File("plugins/Camera/data.yml");
    private YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dataFile);

    public DataConfigManager() {
        createFiles();
    }

    private void createFiles() {
        folder.mkdirs();

        try {
            dataFile.createNewFile();
        } catch (IOException e) {
            Camera.getInstance().getLogger().severe("Error while trying to create data.yml file:");
            Camera.getInstance().getLogger().severe(e.getMessage());
        }
    }

    private void save() {
        try {
            cfg.save(dataFile);
            reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void reload() {
        cfg = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void createCamera(CameraInstance camera) {
        cfg.set("cameras." + camera.id() + ".title", mm.serialize(camera.title()));
        cfg.set("cameras." + camera.id() + ".location", camera.location());
        save();
    }

    public void deleteCamera(int id) {
        cfg.set("cameras." + id, null);
        save();
    }

    public List<CameraInstance> getCameras() {
        List<CameraInstance> cameras = new ArrayList<>();

        if(cfg.getConfigurationSection("cameras") == null)
            return cameras;

        Set<String> cameraIds = cfg.getConfigurationSection("cameras").getKeys(false);

        for (String rawId : cameraIds) {
            int id = Integer.parseInt(rawId);
            Location location = cfg.getLocation("cameras." + id + ".location");
            Component title = mm.deserialize(cfg.getString("cameras." + id + ".title", ""));

            cameras.add(new CameraInstance(id, location, title));
        }

        return cameras;
    }

    public boolean cameraExists(int id) {
        reload();
        return cfg.isSet("cameras." + id);
    }
}
