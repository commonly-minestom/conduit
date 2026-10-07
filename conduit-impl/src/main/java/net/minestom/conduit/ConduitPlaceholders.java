package net.minestom.conduit;

import net.kyori.adventure.text.Component;
import net.minestom.conduit.placeholders.PlaceholderProvider;
import net.minestom.conduit.placeholders.PlaceholderResolver;
import net.minestom.conduit.placeholders.RelationalPlaceholderResolver;
import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * A default implementation of the Conduit placeholder provider.
 */
public final class ConduitPlaceholders implements PlaceholderProvider {

    public static final ConduitPlaceholders INSTANCE = new ConduitPlaceholders();

    private final Map<String, PlaceholderResolver> resolvers = new HashMap<>();
    private final Map<String, RelationalPlaceholderResolver> relationalResolvers = new HashMap<>();

    private ConduitPlaceholders() {
    }

    @Override
    public void register(String tag, PlaceholderResolver resolver) {
        if (resolvers.containsKey(tag) || relationalResolvers.containsKey(tag)) {
            throw new IllegalArgumentException("Placeholder " + tag + " is already registered");
        }

        resolvers.put(tag, resolver);
    }

    @Override
    public void register(String tag, RelationalPlaceholderResolver resolver) {
        if (resolvers.containsKey(tag) || relationalResolvers.containsKey(tag)) {
            throw new IllegalArgumentException("Placeholder " + tag + " is already registered");
        }

        relationalResolvers.put(tag, resolver);
    }

    @Override
    public void unregister(String tag) {
        resolvers.remove(tag);
        relationalResolvers.remove(tag);
    }

    @Override
    public boolean isRegistered(String tag) {
        return resolvers.containsKey(tag) || relationalResolvers.containsKey(tag);
    }

    @Override
    public Component resolve(Player player, String text) {
        return parse(player, null, false, text);
    }

    @Override
    public Component resolve(Player one, Player two, String text) {
        return parse(one, two, true, text);
    }

    private Component parse(Player one, Player two, boolean relational, String text) {
        if (text.indexOf('<') == -1) {
            return Texts.color(text); // fast-path: no placeholder
        }

        StringBuilder out = new StringBuilder(text.length());
        StringBuilder tag = null;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '<' && tag == null) {
                if (isEscaped(text, i)) {
                    // escaped: literal '<', MiniMessage unescapes "\<...>"
                    out.append(c);
                } else {
                    tag = new StringBuilder();
                }
            } else if (c == '>' && tag != null) {
                String replacement = resolveTag(one, two, relational, tag.toString());

                if (replacement != null) {
                    out.append(replacement);
                } else {
                    out.append('<').append(tag).append('>');
                }

                tag = null;
            } else if (tag != null) {
                tag.append(c);
            } else {
                out.append(c);
            }
        }

        if (tag != null) {
            out.append('<').append(tag); // unclosed '<'
        }

        return Texts.color(out.toString());
    }

    private String resolveTag(Player one, Player two, boolean relational, String id) {
        if (relational) {
            RelationalPlaceholderResolver relationalResolver = relationalResolvers.get(id);

            if (relationalResolver != null) {
                return relationalResolver.resolve(one, two);
            }
        }

        PlaceholderResolver resolver = resolvers.get(id);

        if (resolver != null) {
            return resolver.resolve(one);
        }

        return null;
    }

    private boolean isEscaped(String text, int index) {
        int backslashes = 0;
        for (int i = index - 1; i >= 0 && text.charAt(i) == '\\'; i--) {
            backslashes++;
        }
        return backslashes % 2 == 1;
    }
}
