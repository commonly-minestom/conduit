package net.minestom.conduit;

import net.minestom.conduit.placeholders.PlaceholderProvider;
import net.minestom.conduit.services.ServiceRegistry;

/**
 * Singleton for Conduit.
 */
public final class Conduit {

    private Conduit() {
    }

    /**
     * Gets the service registry.
     * @return the service registry instance
     */
    public static ServiceRegistry services() {
        return ConduitServices.INSTANCE;
    }

    /**
     * Gets the placeholder provider.
     * @return the placeholder provider instance
     */
    public static PlaceholderProvider placeholders() {
        return ConduitPlaceholders.INSTANCE;
    }
}
