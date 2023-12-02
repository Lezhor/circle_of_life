package com.android.circleoflife.logging.model;
import com.android.circleoflife.auth.Authentication;
import com.android.circleoflife.auth.AuthenticationFailedException;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Log {

    private final Date date;
    private final String user;
    private final List<Entry> entryList;

    public Log(Authentication auth) throws AuthenticationFailedException {
        this(auth.getUserName(), new Date());
    }

    private Log(String username, Date date) {
        user = username;
        this.date = date;
        entryList = new LinkedList<>();
    }

    /**
     * Inits Log from logHeader
     * @param logHeader logHeader
     * @throws IllegalArgumentException if logHeader is wrong
     */
    public Log(String logHeader) throws IllegalArgumentException {
        // TODO: 02.12.2023 Create Log from Header
        this("", new Date());
    }

    public String getLogHeader() {
        // TODO: 02.12.2023 Generate Log Header
        return "Log|" + user + "|" + date.getTime();
    }


    /**
     * Is sorted by timestamp when the entry was made
     */
    public static class Entry implements Comparable<Entry> {

        // TODO: 02.12.2023 Implement Log.Entry
        private Date timeStamp;

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
