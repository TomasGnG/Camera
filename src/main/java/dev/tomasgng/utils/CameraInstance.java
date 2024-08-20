package dev.tomasgng.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;

public record CameraInstance(int id, Location location, Component title) {
}
