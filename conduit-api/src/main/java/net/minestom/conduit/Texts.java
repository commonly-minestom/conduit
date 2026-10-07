package net.minestom.conduit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for coloring text and converting it to small caps.
 */
public class Texts {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([0-9a-fA-F]{6})");
    private static final Pattern LEGACY_PATTERN = Pattern.compile("&([0-9a-fk-or])", Pattern.CASE_INSENSITIVE);
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String SMALL_CAPS = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘꞯʀꜱᴛᴜᴠᴡxʏᴢ";
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Map<String, String> LEGACY_TO_MINIMESSAGE = Map.ofEntries(
            Map.entry("0", "black"), Map.entry("1", "dark_blue"),
            Map.entry("2", "dark_green"), Map.entry("3", "dark_aqua"),
            Map.entry("4", "dark_red"), Map.entry("5", "dark_purple"),
            Map.entry("6", "gold"), Map.entry("7", "gray"),
            Map.entry("8", "dark_gray"), Map.entry("9", "blue"),
            Map.entry("a", "green"), Map.entry("b", "aqua"),
            Map.entry("c", "red"), Map.entry("d", "light_purple"),
            Map.entry("e", "yellow"), Map.entry("f", "white"),
            Map.entry("k", "obfuscated"), Map.entry("l", "bold"),
            Map.entry("m", "strikethrough"), Map.entry("n", "underlined"),
            Map.entry("o", "italic")
    );

    /**
     * Colors the given text, parsing both legacy codes and MiniMessage tags.
     * <p>
     *     Legacy codes ({@code &a}, {@code &l}, {@code &r}) and hex colors
     *     ({@code &#rrggbb}) are converted to MiniMessage and parsed together
     *     with native tags (e.g. {@code <red>}, {@code <bold>}).
     * </p>
     * @param text the text to color
     * @return the colored component, or the plain text if parsing fails
     */
    public static Component color(String text) {
        try {
            return MINI_MESSAGE.deserialize(toMiniMessage(text));
        } catch (Exception e) {
            return Component.text(text);
        }
    }

    /**
     * Converts the given text to small caps (e.g. {@code hello} to {@code ʜᴇʟʟᴏ}).
     * <p>
     *     Legacy codes (e.g. {@code &a}) and MiniMessage tags (e.g. {@code <red>})
     *     are preserved as-is so formatting survives the conversion.
     * </p>
     * @param text the text to minify
     * @return the text in small caps
     */
    public static String minify(String text) {
        StringBuilder out = new StringBuilder(text.length());

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '&' && i + 1 < text.length()) {
                out.append(c).append(text.charAt(++i));
            } else if (c == '<') {
                int end = text.indexOf('>', i);
                if (end == -1) {
                    out.append(mapSmallCap(c));
                } else {
                    out.append(text, i, end + 1);
                    i = end;
                }
            } else {
                out.append(mapSmallCap(c));
            }
        }

        return out.toString();
    }

    private static char mapSmallCap(char c) {
        int index = LOWERCASE.indexOf(Character.toLowerCase(c));
        return index == -1 ? c : SMALL_CAPS.charAt(index);
    }

    private static String toMiniMessage(String text) {
        // Hex: &#rrggbb -> <#rrggbb>
        String result = HEX_PATTERN.matcher(text).replaceAll("<color:#$1>");
        // Standard: &a -> <green>, &r -> <reset>
        Matcher matcher = LEGACY_PATTERN.matcher(result);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String code = matcher.group(1).toLowerCase(Locale.ROOT);
            String replacement = code.equals("r")
                    ? "<reset>"
                    : "<" + LEGACY_TO_MINIMESSAGE.get(code) + ">";
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
