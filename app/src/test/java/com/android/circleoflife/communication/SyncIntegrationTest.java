package com.android.circleoflife.communication;


import static org.junit.Assert.*;
import com.android.circleoflife.application.App;
import com.android.circleoflife.communication.models.SyncResult;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.logging.model.DBLog;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class SyncIntegrationTest {

    /**
     * There will be an entry with this username in the server-database. Nobody should take this user.
     */
    public static String USER_NAME = "a_test_user_for_integration_test_325z42";

    /**
     * This password should be pretty hard to guess ... hopefully
     */
    public static String PASSWORD = "a_super_complicated_password_which_nobody_should_be_able_to_guess_324384248509";

    private User user;

    @Before
    public void setUp() {
        try {
            user = App.getLoginProtocol().login(USER_NAME, PASSWORD);
            if (user == null) {
                user = App.getSignUpProtocol().signUp(USER_NAME, PASSWORD);
            }
        } catch (IOException e) {
            fail(e.getLocalizedMessage());
        }
        if (user == null) {
            fail("Neither succeeded to login nor to signUp! - Check if server is up and running!");
        }
    }

    /**
     * Tests following case:<br>
     * On timestamp 0 a category has been made
     */
    @Test
    public void testSimpleSync() {
        Category c1 = new Category(UUID.randomUUID(), "Test Category", user.getId(), null);
        Category c1Alter = c1.copy();
        c1Alter.setName("Other category");
        Category c2 = new Category(UUID.randomUUID(), "Category 2", user.getId(), c1.getId());

        Todo todo = new Todo(UUID.randomUUID(), "Test Todo", user.getId(), c1.getId(), 0, false, LocalDateTime.now(App.SERVER_TIMEZONE).plusHours(5));

        DBLog<?>[] logsBefore = new DBLog[]{
                new DBLog<>(UUID.randomUUID(), user.getId(), c1, DBLog.ChangeMode.INSERT, LocalDateTime.now(App.SERVER_TIMEZONE).minusNanos(1))
        };
        App.getSyncProtocol().sync(user, LocalDateTime.now(App.SERVER_TIMEZONE).minusNanos(1000), logsBefore);

        LocalDateTime[] timestamp = new LocalDateTime[10];
        for (int i = 0; i < timestamp.length; i++) {
            timestamp[i] = LocalDateTime.now(App.SERVER_TIMEZONE).minusNanos(timestamp.length - i);
        }

        DBLog<?>[] logsA = new DBLog[]{
                new DBLog<>(UUID.randomUUID(), user.getId(), c1Alter, DBLog.ChangeMode.UPDATE, timestamp[4]),
                new DBLog<>(UUID.randomUUID(), user.getId(), todo, DBLog.ChangeMode.INSERT, timestamp[5])
        };
        DBLog<?>[] logsB = new DBLog[]{
                new DBLog<>(UUID.randomUUID(), user.getId(), c2, DBLog.ChangeMode.INSERT, timestamp[8])
        };

        // A syncs
        SyncResult sync = App.getSyncProtocol().sync(user, timestamp[2], logsA);
        assertNotNull(sync.getNewLastSyncDate());
        assertEquals(0, sync.getOutLogs().size());

        LocalDateTime lastSyncA = sync.getNewLastSyncDate();

        // B syncs
        sync = App.getSyncProtocol().sync(user, timestamp[3], logsB);
        assertNotNull(sync.getNewLastSyncDate());
        assertArrayEquals(logsA, sync.getOutLogs().toArray(DBLog[]::new));

        // A syncs again
        sync = App.getSyncProtocol().sync(user, lastSyncA, new DBLog[0]);
        assertNotNull(sync.getNewLastSyncDate());
        assertArrayEquals(logsB, sync.getOutLogs().toArray(DBLog[]::new));
    }

    /**
     * Test method {@link #testSimpleSync()} 10 times. Even if executing it once succeeds, doing it multiple times could fail depending on the sync-implementation, that's why the test is for.
     */
    @Test
    public void testSimpleSync10Times() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            testSimpleSync();
            Thread.sleep(100);
        }
    }

}
