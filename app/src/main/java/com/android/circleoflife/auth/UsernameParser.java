package com.android.circleoflife.auth;

/**
 * Converts Username from its displayed version to its saved version and back
 */
public class UsernameParser {

    /**
     * Converts username to its displayed version<br>
     * The differences are that:<br>
     * 1) all underscores are replaced with spaces<br>
     * 2) every new word starts with a capital letter
     *
     * @param username username
     * @return displayed version of username
     */
    public static String usernameToDisplayedVersion(String username) {
        StringBuilder sb = new StringBuilder();
        String[] split = username.replaceAll(" ", "_").split("_");
        for (String word : split) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return sb.toString().trim();
    }

    /**
     * Converts displayedUsername to actual version<br>
     * The differences are that:<br>
     * 1) all spaces are replaced with underscores<br>
     * 2) everything is lowercase
     *
     * @param displayedUsername displayedUsername
     * @return actual version of username with underscores and lowercase letters
     */
    public static String displayedUsernameToActualVersion(String displayedUsername) {
        return displayedUsername.toLowerCase().replaceAll(" ", "_");
    }


}
