package net.minestom.conduit.placeholders;

import net.minestom.server.entity.Player;

/**
 * Resolves a placeholder for two related players.
 */
@FunctionalInterface
public interface RelationalPlaceholderResolver {
    /**
     * Resolves the placeholder value.
     * @param one the player one
     * @param two the player two
     * @return the resolved value
     */
    String resolve(Player one, Player two);
}
