package com.android.circleoflife.database.validators;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringValidatorTest {

    @Test
    public void testValidateUsername() {
    }

    @Test
    public void testStringContainsNumbers() {
        String[] contains = new String[] {
                "1",
                "3298321",
                "abc1",
                "4 dksja",
                "§$&)%7)/&",
        };
        String[] notContains = new String[] {
                null,
                "",
                "abc",
                "$ dksja",
                "§$&)%A)/&",
        };
        for (String s : contains) {
            assertTrue(StringValidator.stringContainsNumbers(s));
        }
        for (String s : notContains) {
            assertFalse(StringValidator.stringContainsNumbers(s));
        }
    }

    @Test
    public void testStringContainsLetters() {
        assertTrue(StringValidator.stringContainsLetters("a"));
        assertTrue(StringValidator.stringContainsLetters("12309b"));
        assertTrue(StringValidator.stringContainsLetters("Z332"));
        assertTrue(StringValidator.stringContainsLetters("Ö"));
        assertTrue(StringValidator.stringContainsLetters("é"));
        assertTrue(StringValidator.stringContainsLetters("Â"));
        assertTrue(StringValidator.stringContainsLetters("куку"));
        assertTrue(StringValidator.stringContainsLetters("こんいちは"));

        assertFalse(StringValidator.stringContainsLetters(null));
        assertFalse(StringValidator.stringContainsLetters(""));
        assertFalse(StringValidator.stringContainsLetters("3"));
        assertFalse(StringValidator.stringContainsLetters("§25&%.#^"));
    }

    @Test
    public void testStringConsistsOfLettersNumbersAndUnderscores() {
        assertTrue(StringValidator.stringConsistsOfLettersNumbersAndUnderscores(""));
        assertTrue(StringValidator.stringConsistsOfLettersNumbersAndUnderscores("TE_ii"));
        assertTrue(StringValidator.stringConsistsOfLettersNumbersAndUnderscores("lol_kj"));
        assertTrue(StringValidator.stringConsistsOfLettersNumbersAndUnderscores("HALLO"));
        assertTrue(StringValidator.stringConsistsOfLettersNumbersAndUnderscores("___"));
        assertTrue(StringValidator.stringConsistsOfLettersNumbersAndUnderscores("df3"));
        assertTrue(StringValidator.stringConsistsOfLettersNumbersAndUnderscores("42"));
        assertTrue(StringValidator.stringConsistsOfLettersNumbersAndUnderscores("ö"));
        assertTrue(StringValidator.stringConsistsOfLettersNumbersAndUnderscores("_é_"));
        assertTrue(StringValidator.stringConsistsOfLettersNumbersAndUnderscores("45碁"));

        assertFalse(StringValidator.stringConsistsOfLettersNumbersAndUnderscores("dF-b"));
        assertFalse(StringValidator.stringConsistsOfLettersNumbersAndUnderscores("._."));
        assertFalse(StringValidator.stringConsistsOfLettersNumbersAndUnderscores("/"));
        assertFalse(StringValidator.stringConsistsOfLettersNumbersAndUnderscores("\n"));
    }

    @Test
    public void testStringHasNoUppercaseLetters() {
        assertTrue(StringValidator.stringHasNoUppercaseLetters(""));
        assertTrue(StringValidator.stringHasNoUppercaseLetters("3jf _kj&%$%"));

        assertFalse(StringValidator.stringHasNoUppercaseLetters("J"));
        assertFalse(StringValidator.stringHasNoUppercaseLetters("dksjIkdj"));
        assertFalse(StringValidator.stringHasNoUppercaseLetters("_Z&/%)"));
    }
}