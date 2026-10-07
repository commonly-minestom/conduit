package net.minestom.conduit.permissions;

import net.kyori.adventure.util.TriState;

import java.util.UUID;

/**
 * Contract for managing permissions.
 */
public interface PermissionManager extends PermissionProvider {
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
