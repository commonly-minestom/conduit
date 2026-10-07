package net.minestom.conduit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextsTest {

    @Test
    void minifyConvertsToSmallCaps() {
        assertEquals("ʜᴇʟʟᴏ", Texts.minify("hello"));
    }

    @Test
    void minifyLowercasesUppercase() {
        assertEquals("ᴀʙᴄ", Texts.minify("ABC"));
    }

    @Test
    void minifyLeavesNonLettersUntouched() {
        assertEquals("123 !?", Texts.minify("123 !?"));
    }

    @Test
    void minifyPreservesLegacyCodes() {
        assertEquals("&aʜᴇʟʟᴏ", Texts.minify("&ahello"));
    }

    @Test
    void minifyPreservesMiniMessageTags() {
        assertEquals("<red>ʜᴇʟʟᴏ</red>", Texts.minify("<red>hello</red>"));
    }
}
