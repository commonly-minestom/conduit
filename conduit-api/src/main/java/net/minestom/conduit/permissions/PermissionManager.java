package net.minestom.conduit.permissions;

import net.kyori.adventure.util.TriState;
import net.minestom.server.command.CommandSender;

import java.util.UUID;

/**
 * Contract for checking and managing permissions.
 */
public interface PermissionManager {
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

    /**
     * Sets a permission for a player.
     * @param target the player to set the permission for
     * @param node the permission node
     * @param value the permission state
     */
    void setPermission(UUID target, String node, TriState value);

    /**
     * Removes a permission from a player.
     * @param target the player to remove the permission from
     * @param node the permission node
     */
    void unsetPermission(UUID target, String node);
}
