package net.minestom.conduit;

import net.minestom.conduit.placeholders.PlaceholderProvider;

/**
 * Bootstraps Conduit with the default implementations.
 * <p>
 *     Call {@link #init()} once at server startup, before any plugin uses
 *     {@link Conduit}. If you ship your own registry store, call
 *     {@link Conduit#init} directly instead and skip this class.
 * </p>
 */
public final class ConduitBootstrap {

    private ConduitBootstrap() {
    }

    /**
     * Installs the default service registry and registers the default services.
     *
     * @throws IllegalStateException if Conduit was already initialized
     */
    public static void init() {
        Conduit.init(ConduitServices.INSTANCE);
        Conduit.services().register(PlaceholderProvider.class, ConduitPlaceholders.INSTANCE);
    }
}
