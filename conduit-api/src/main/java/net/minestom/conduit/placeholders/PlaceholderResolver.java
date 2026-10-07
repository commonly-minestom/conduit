package net.minestom.conduit.placeholders;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;

public interface PlaceholderResolver {
    String resolve(Player player);

    default Component resolveComponent(Player player) {
        return Component.text(resolve(player));
    }
}
