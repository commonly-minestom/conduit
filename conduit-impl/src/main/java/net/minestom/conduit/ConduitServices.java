package net.minestom.conduit;

import net.minestom.conduit.services.ServiceRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A default implementation of the Conduit service registry.
 */
public final class ConduitServices implements ServiceRegistry {

    public static final ConduitServices INSTANCE = new ConduitServices();

    private final Map<Class<?>, Object> services = new HashMap<>();

    private ConduitServices() {
    }

    @Override
    public <T> void register(Class<T> service, T instance) {
        services.put(service, instance);
    }

    @Override
    public <T> Optional<T> get(Class<T> service) {
        Object instance = services.get(service);

        if (instance == null) {
            return Optional.empty();
        }

        if (!service.isInstance(instance)) {
            throw new IllegalArgumentException("Service " + instance + " is not of type " + service);
        }

        return Optional.of(service.cast(instance));
    }
}
