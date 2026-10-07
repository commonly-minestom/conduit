package net.minestom.conduit.services;

import net.minestom.conduit.permissions.PermissionManager;

import java.util.Optional;

/**
 * Contract for a service registry.
 */
public interface ServiceRegistry {
    /**
     * Registers an instance of a service (e.g. implementation of {@link PermissionManager}).
     * <p>
     *     This method replaces the previous instance of the service if it was already registered.
     * </p>
     * @param service the service class
     * @param instance an instance of the service
     * @param <T> the type of the service
     */
    <T> void register(Class<T> service, T instance);

    /**
     * Gets an instance of the requested service.
     * @param service the service class
     * @return an optional containing the service if present
     * @param <T> the type of the service
     */
    <T> Optional<T> get(Class<T> service);
}
