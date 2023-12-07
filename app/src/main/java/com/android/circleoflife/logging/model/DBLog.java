package com.android.circleoflife.logging.model;
import com.android.circleoflife.auth.Authentication;
import com.android.circleoflife.auth.AuthenticationFailedException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DBLog {

    private final LocalDateTime timeOfCreation;
    private final String user; // TODO: 08.12.2023 Change to User Object
    private final List<Entry> entryList;

    public DBLog(Authentication auth) throws AuthenticationFailedException {
        this(auth.getUserName(), LocalDateTime.now());
    }

    private DBLog(String username, LocalDateTime timeOfCreation) {
        user = username;
        this.timeOfCreation = timeOfCreation;
        entryList = new LinkedList<>();
    }

    /**
     * Inits Log from logHeader
     * @param logHeader logHeader
     * @throws IllegalArgumentException if logHeader is wrong
     */
    public DBLog(String logHeader) throws IllegalArgumentException {
        // TODO: 02.12.2023 Create Log from Header
        this("", LocalDateTime.now());
    }

    public String getLogHeader() {
        // TODO: 02.12.2023 Generate Log Header
        return "Log|" + user + "|" + timeOfCreation.toString();
    }

    /**
     * returns time of creation
     * @return time of creation
     */
    public LocalDateTime getTimeOfCreation() {
        return timeOfCreation;
    }



    /**
     * Is sorted by timestamp when the entry was made
     */
    public static class Entry implements Comparable<Entry> {

        // TODO: 02.12.2023 Implement Log.Entry
        private LocalDateTime timeStamp;

        private String table;

        @Override
        public int compareTo(Entry that) {
            return this.timeStamp.compareTo(that.timeStamp);
        }

        public Entry invert() {
            // TODO: 02.12.2023 Implement Entry inversion
            return null;
        }

        public enum Mode {
            CREATE, UPDATE, DELETE
        }

    }

}
