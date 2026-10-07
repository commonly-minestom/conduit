package net.minestom.conduit.services;

import java.util.Optional;

public interface ServiceRegistry {
    <T> void register(Class<T> service, T instance);

    <T> Optional<T> get(Class<T> service);
}
