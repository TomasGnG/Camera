package dev.tomasgng;

import dev.tomasgng.commands.CameraCommand;
import dev.tomasgng.config.*;
import dev.tomasgng.listeners.PlayerMoveListener;
import dev.tomasgng.listeners.PlayerToggleSneakListener;
import dev.tomasgng.utils.CameraManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Camera extends JavaPlugin {

    private static Camera instance;

    private BukkitAudiences adventure;

    private MessageManager messageManager;
    private ConfigManager configManager;
    private MessageDataProvider messageDataProvider;
    private DataConfigManager dataConfigManager;
    private ConfigDataProvider configDataProvider;

    private CameraManager cameraManager;

    @Override
    public void onEnable() {
        instance = this;
        adventure = BukkitAudiences.create(this);

        messageManager = new MessageManager();
        configManager = new ConfigManager();
        messageDataProvider = new MessageDataProvider();
        dataConfigManager = new DataConfigManager();
        configDataProvider = new ConfigDataProvider();
        cameraManager = new CameraManager();

        getCommand("camera").setExecutor(new CameraCommand());
        getCommand("camera").setTabCompleter(new CameraCommand());

        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new PlayerMoveListener(), this);
        pluginManager.registerEvents(new PlayerToggleSneakListener(), this);
    }

    @Override
    public void onDisable() {
        if(adventure != null) {
            adventure.close();
            adventure = null;
        }
    }

    public static Camera getInstance() {
        return instance;
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageDataProvider getMessageDataProvider() {
        return messageDataProvider;
    }

    public DataConfigManager getDataConfigManager() {
        return dataConfigManager;
    }

    public ConfigDataProvider getConfigDataProvider() {
        return configDataProvider;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }
}
