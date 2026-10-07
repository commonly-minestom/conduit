package net.minestom.conduit.placeholders;

import net.minestom.server.entity.Player;

/**
 * Resolves a placeholder for a player.
 */
@FunctionalInterface
public interface PlaceholderResolver {
    /**
     * Resolves the placeholder value.
     * @param player the player to resolve the placeholder for
     * @return the resolved value
     */
    String resolve(Player player);
}
