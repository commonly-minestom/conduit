package net.minestom.conduit;

import net.minestom.conduit.economy.EconomyProvider;
import net.minestom.conduit.permissions.PermissionManager;
import net.minestom.conduit.placeholders.PlaceholderProvider;
import net.minestom.conduit.services.ServiceRegistry;

/**
 * Static entry point for Conduit.
 * <p>
 *     Lives in the API so plugins only depend on {@code conduit-api}.
 * </p>
 * <h2>Setup</h2>
 * <p>
 *     Before any plugin uses Conduit, the backing registry must be installed
 *     once at server startup. {@code conduit-impl} provides the defaults via
 *     {@code ConduitBootstrap}:
 * </p>
 * <pre>{@code
 * ConduitBootstrap.init(); // Conduit.init(new ConduitServices()) + default service registrations
 * }</pre>
 * <h2>Providing your own implementations</h2>
 * <p>
 *     Implement an API contract (e.g. {@link PermissionManager} or
 *     {@link PlaceholderProvider}) and register it into the registry.
 *     Consumers then resolve it with {@link #services()} or a shortcut like
 *     {@link #permissions()} or {@link #placeholders()}. No bootstrapping or
 *     descriptor files needed:
 * </p>
 * <pre>{@code
 * Conduit.services().register(PermissionManager.class, new MyPermissions());
 * }</pre>
 * <p>
 *     To replace the registry store itself, call {@link #init} once at startup
 *     <i>instead of</i> {@code ConduitBootstrap.init()} with your own
 *     implementation.
 * </p>
 */
public final class Conduit {

    private static volatile ServiceRegistry services;

    private Conduit() {
    }

    /**
     * Installs the backing registry. Must be called once at server
     * startup before any plugin touches Conduit.
     *
     * @param services the service registry store
     * @throws IllegalStateException if Conduit was already initialized
     */
    public static synchronized void init(ServiceRegistry services) {
        if (Conduit.services != null) {
            throw new IllegalStateException("Conduit is already initialized");
        }

        Conduit.services = services;
    }

    /**
     * Gets the service registry.
     * @return the service registry instance
     * @throws IllegalStateException if {@link #init} was not called yet
     */
    public static ServiceRegistry services() {
        ServiceRegistry result = services;

        if (result == null) {
            throw new IllegalStateException(
                    "Conduit is not initialized. Call ConduitBootstrap.init() (conduit-impl) "
                    + "or Conduit.init(...) with your own implementation at server startup.");
        }

        return result;
    }

    /**
     * Gets the placeholder provider.
     * <p>
     * Shortcut for {@code services().get(PlaceholderProvider.class)}.
     *
     * @return the placeholder provider instance
     * @throws IllegalStateException if Conduit was not initialized or no
     * {@link PlaceholderProvider} was registered
     */
    public static PlaceholderProvider placeholders() {
        return services().get(PlaceholderProvider.class).orElseThrow(() ->
                new IllegalStateException("No PlaceholderProvider registered in the Conduit service registry."));
    }

    /**
     * Gets the permission manager.
     * <p>
     * Shortcut for {@code services().get(PermissionManager.class)}.
     *
     * @return the permission manager instance
     * @throws IllegalStateException if Conduit was not initialized or no
     * {@link PermissionManager} was registered
     */
    public static PermissionManager permissions() {
        return services().get(PermissionManager.class).orElseThrow(() ->
            new IllegalStateException("No PermissionManager registered in the Conduit service registry."));
    }

    /**
     * Gets the economy provider.
     * <p>
     * Shortcut for {@code services().get(EconomyProvider.class)}.
     *
     * @return the economy provider instance
     * @throws IllegalStateException if Conduit was not initialized or no
     * {@link EconomyProvider} was registered
     */
    public static EconomyProvider economy() {
        return services().get(EconomyProvider.class).orElseThrow(() ->
            new IllegalStateException("No EconomyProvider registered in the Conduit service registry."));
    }
}
