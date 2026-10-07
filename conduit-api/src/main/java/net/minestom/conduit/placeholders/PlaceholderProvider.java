package net.minestom.conduit.placeholders;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;

/**
 * Contract for a placeholder provider.
 */
public interface PlaceholderProvider {
    /**
     * Registers a placeholder resolver for a specific placeholder.
     * @param tag the placeholder tag (e.g. {@code name} for {@code <name>})
     * @param resolver the resolver of that placeholder
     */
    void register(String tag, PlaceholderResolver resolver);

    /**
     * Registers a relation placeholder resolver for a specific placeholder.
     * <p>
     *     Relational placeholders are used to show placeholders differently based on who sees it.
     * </p>
     * @param tag the placeholder tag (e.g. {@code name} for {@code <name>})
     * @param resolver the resolver of that placeholder
     */
    void register(String tag, RelationalPlaceholderResolver resolver);

    /**
     * Unregisters a placeholder resolver from it's tag.
     * @param tag the placeholder tag
     */
    void unregister(String tag);

    /**
     * Checks if a placeholder is registered.
     * @param tag the placeholder tag
     * @return true if the placeholder is registered, false otherwise
     */
    boolean isRegistered(String tag);

    /**
     * Resolves all registered placeholders in the given text.
     * <p>
     *     Placeholders are in the form of {@code <tag>}. A tag preceded by a
     *     backslash ({@code \<tag>}) is escaped and left as literal text.
     * </p>
     * @param player the player to resolve placeholders for
     * @param text the text containing placeholders
     * @return the text with placeholders resolved
     */
    Component resolve(Player player, String text);

    /**
     * Resolves all registered placeholders in the given text for a viewer
     * {@code one} seeing {@code two}.
     * <p>
     *     Relational placeholders are resolved with both players. Plain
     *     placeholders fall back to {@code one} (the viewer).
     * </p>
     * @param one the viewer seeing the text
     * @param two the player the text is about
     * @param text the text containing placeholders
     * @return the text with placeholders resolved
     */
    Component resolve(Player one, Player two, String text);
}
