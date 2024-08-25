package dev.tomasgng.commands;

import dev.tomasgng.Camera;
import dev.tomasgng.config.ConfigDataProvider;
import dev.tomasgng.config.MessageDataProvider;
import dev.tomasgng.utils.CameraManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CameraCommand implements CommandExecutor, TabCompleter {
    private final CameraManager cameraManager = Camera.getInstance().getCameraManager();
    private final ConfigDataProvider config = Camera.getInstance().getConfigDataProvider();
    private final MessageDataProvider message = Camera.getInstance().getMessageDataProvider();

    private final BukkitAudiences adventure = Camera.getInstance().getAdventure();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only command.");
            return false;
        }

        if(!player.hasPermission(config.getCommandPermission())) {
            adventure.player(player).sendMessage(message.getCommandNoPermission());
            return false;
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("reload")) {
                cameraManager.reload(player);
                return false;
            }

            if(args[0].equalsIgnoreCase("list")) {
                cameraManager.listAllCameras(player);
                return false;
            }
        }

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("delete")) {
                if(!isValidId(args[1])) {
                    adventure.player(player).sendMessage(message.getCommandInvalidId());
                    return false;
                }

                cameraManager.deleteCamera(player, Integer.parseInt(args[1]));
                return false;
            }

            if(args[0].equalsIgnoreCase("teleport")) {
                if(!isValidId(args[1])) {
                    adventure.player(player).sendMessage(message.getCommandInvalidId());
                    return false;
                }

                cameraManager.teleportCamera(player, Integer.parseInt(args[1]));
                return false;
            }

            if(args[0].equalsIgnoreCase("toggle")) {
                Player target = Bukkit.getPlayer(args[1]);

                if(target == null) {
                    adventure.player(player).sendMessage(message.getCommandPlayerOffline());
                    return false;
                }

                cameraManager.toggleCamera(player, target);
                return false;
            }

            if(args[0].equalsIgnoreCase("view")) {
                if(!isValidId(args[1])) {
                    adventure.player(player).sendMessage(message.getCommandInvalidId());
                    return false;
                }

                cameraManager.toggleViewCamera(player, Integer.parseInt(args[1]));
                return false;
            }
        }

        if(args.length > 2) {
            if(args[0].equalsIgnoreCase("create")) {
                if(!isValidId(args[1])) {
                    adventure.player(player).sendMessage(message.getCommandInvalidId());
                    return false;
                }

                int id = Integer.parseInt(args[1]);
                StringBuilder rawTitle = new StringBuilder();
                Component title;

                for (int i = 2; i < args.length; i++) {
                    rawTitle.append(args[i]).append(" ");
                }

                try {
                    title = MiniMessage.miniMessage().deserialize(rawTitle.toString().trim());
                } catch (Exception e) {
                    adventure.player(player).sendMessage(message.getCommandInvalidTitleFormat());
                    return false;
                }

                cameraManager.createCamera(player, id, title);
                return false;
            }
        }

        adventure.player(player).sendMessage(message.getCommandHelp());
        return false;
    }

    private boolean isValidId(String id) {
        try {
            int i = Integer.parseInt(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1)
            return List.of("create", "delete", "list", "teleport", "toggle", "reload", "view");

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("view"))
                return cameraManager.getCameraIds().stream().map(String::valueOf).toList();

            if(args[0].equalsIgnoreCase("toggle"))
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }

        return List.of();
    }
}
