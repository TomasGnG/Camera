package dev.tomasgng.config;

import dev.tomasgng.Camera;
import dev.tomasgng.utils.CameraInstance;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataStoreManager {

    private final MiniMessage mm = MiniMessage.miniMessage();
    private final File folder = new File("plugins/Camera");
    private final File dataFile = new File("plugins/Camera/data.yml");
    private YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dataFile);

    public DataStoreManager() {
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

    public void deleteCamera(CameraInstance camera) {
        cfg.set("cameras." + camera.id(), null);
        save();
    }

    public List<CameraInstance> getCameras() {
        List<CameraInstance> cameras = new ArrayList<>();

        var cameraIds = cfg.getVa

        return cameras;
    }
}
