package net.minestom.conduit.permissions;

import net.kyori.adventure.util.TriState;

import java.util.UUID;

public interface PermissionProvider {
    TriState hasPermission(UUID player, String node);
}
