package net.minestom.conduit.permissions;

import net.kyori.adventure.util.TriState;
import net.minestom.server.command.CommandSender;

import java.util.UUID;

/**
 * Contract for checking permissions.
 */
public interface PermissionProvider {
    /**
     * Checks if a sender has a permission.
     * @param sender the sender to check
     * @param node the permission node
     * @return the permission state
     */
    TriState hasPermission(CommandSender sender, String node);

    /**
     * Checks if a player has a permission.
     * @param target the player to check
     * @param node the permission node
     * @return the permission state
     */
    TriState hasPermission(UUID target, String node);
}
