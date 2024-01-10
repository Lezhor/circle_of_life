package com.android.circleoflife.auth;

import static org.junit.Assert.*;

import org.junit.Test;

public class UsernameParserTest {

    @Test
    public void testToDisplayedVersion() {
        String username = "john_doe_the_second";
        assertEquals("John Doe The Second", UsernameParser.usernameToDisplayedVersion(username));
    }

    @Test
    public void testFromDisplayedVersion() {
        String displayed = "Captain Jack Sparrow 13";
        assertEquals("captain_jack_sparrow_13", UsernameParser.displayedUsernameToActualVersion(displayed));
    }

}