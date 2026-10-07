package net.minestom.conduit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaceholderTest {

    private final ConduitPlaceholders placeholders = ConduitPlaceholders.INSTANCE;
    private final List<String> registered = new ArrayList<>();

    @AfterEach
    void tearDown() {
        registered.forEach(placeholders::unregister);
        registered.clear();
    }

    private void register(String id) {
        placeholders.register(id, player -> "value-of-" + id);
        registered.add(id);
    }

    private static String plain(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    @Test
    void resolvesRegisteredPlaceholder() {
        register("name");

        Component result = placeholders.resolve(null, "Hello <name>!");

        assertEquals("Hello value-of-name!", plain(result));
    }

    @Test
    void resolvesMultiplePlaceholders() {
        register("first");
        register("second");

        Component result = placeholders.resolve(null, "<first> and <second>");

        assertEquals("value-of-first and value-of-second", plain(result));
    }

    @Test
    void resolvesSamePlaceholderTwice() {
        register("name");

        Component result = placeholders.resolve(null, "<name> meets <name>");

        assertEquals("value-of-name meets value-of-name", plain(result));
    }

    @Test
    void leavesUnknownPlaceholderUntouched() {
        Component result = placeholders.resolve(null, "Hello <unknown>!");

        assertEquals("Hello <unknown>!", plain(result));
    }

    @Test
    void leavesTextWithoutPlaceholdersUntouched() {
        Component result = placeholders.resolve(null, "Just plain text");

        assertEquals("Just plain text", plain(result));
    }

    @Test
    void unregisterRemovesPlaceholder() {
        register("temp");
        placeholders.unregister("temp");
        registered.remove("temp");

        Component result = placeholders.resolve(null, "Hello <temp>!");

        assertEquals("Hello <temp>!", plain(result));
    }

    @Test
    void duplicateRegisterThrows() {
        register("dupe");

        assertThrows(IllegalArgumentException.class,
                () -> placeholders.register("dupe", player -> "other"));
    }

    @Test
    void plainThenRelationalWithSameTagThrows() {
        register("mixed");

        assertThrows(IllegalArgumentException.class,
                () -> placeholders.register("mixed", (one, two) -> "R"));
    }

    @Test
    void relationalThenPlainWithSameTagThrows() {
        placeholders.register("mixed", (one, two) -> "R");
        registered.add("mixed");

        assertThrows(IllegalArgumentException.class,
                () -> placeholders.register("mixed", player -> "other"));
    }

    @Test
    void unregisterUnknownIdDoesNotThrow() {
        placeholders.unregister("never-registered");
        assertTrue(true);
    }

    @Test
    void escapedPlaceholderStaysLiteral() {
        register("name");

        Component result = placeholders.resolve(null, "Hello \\<name>!");

        assertEquals("Hello <name>!", plain(result));
    }

    @Test
    void doubleBackslashStillResolves() {
        register("name");

        Component result = placeholders.resolve(null, "Hello \\\\<name>!");

        assertEquals("Hello \\value-of-name!", plain(result));
    }

    @Test
    void escapedMiniMessageTagStaysLiteral() {
        Component result = placeholders.resolve(null, "Hello \\<red>!");

        assertEquals("Hello <red>!", plain(result));
    }

    @Test
    void resolvesRelationalPlaceholder() {
        placeholders.register("rel", (one, two) -> "one-and-two");
        registered.add("rel");

        Component result = placeholders.resolve(null, null, "Hello <rel>!");

        assertEquals("Hello one-and-two!", plain(result));
    }

    @Test
    void relationalResolveFallsBackToPlainResolver() {
        register("name");
        placeholders.register("rel", (one, two) -> "R");
        registered.add("rel");

        Component result = placeholders.resolve(null, null, "<name> and <rel>");

        assertEquals("value-of-name and R", plain(result));
    }

    @Test
    void relationalResolveLeavesUnknownUntouched() {
        Component result = placeholders.resolve(null, null, "Hello <unknown>!");

        assertEquals("Hello <unknown>!", plain(result));
    }

    @Test
    void relationalResolveRespectsEscape() {
        placeholders.register("rel", (one, two) -> "R");
        registered.add("rel");

        Component result = placeholders.resolve(null, null, "Hello \\<rel>!");

        assertEquals("Hello <rel>!", plain(result));
    }

    @Test
    void preservesMiniMessageTags() {        register("name");

        Component result = placeholders.resolve(null, "<green>Hello <name></green>");

        assertEquals("Hello value-of-name", plain(result));
    }

    @Test
    void resolverReceivesPlayer() {
        AtomicReference<Object> received = new AtomicReference<>(new Object());
        placeholders.register("capture", player -> {
            received.set(player);
            return "x";
        });
        registered.add("capture");

        placeholders.resolve(null, "<capture>");

        assertEquals(null, received.get());
    }
}
