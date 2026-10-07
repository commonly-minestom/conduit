package net.minestom.conduit.placeholders;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;

public interface PlaceholderProvider {
    void register(PlaceholderResolver resolver);

    Component resolve(Player player, String text);
}
