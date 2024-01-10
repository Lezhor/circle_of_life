package com.android.circleoflife.communication.protocols;

import static org.junit.Assert.*;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.logging.model.DBLog;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class SyncProtocolEngineTest {

    public static String USER_NAME = "super_complicated_test_username_nobody_should_be_able_to_guess_this_one_32432";
    public static String PASSWORD = "also_complicated_to_guess_cuz_its_long_123";

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

    @Test
    public void testSync() {
        Category category = new Category(UUID.randomUUID(), "University", user.getId(), null);
        DBLog<?>[] logs = new DBLog[]{new DBLog<>(category, DBLog.ChangeMode.INSERT)};
        List<DBLog<?>> outLogs = new LinkedList<>();

        SyncProtocol syncProtocol = App.getSyncProtocol();

        LocalDateTime result;
        result = syncProtocol.sync(user, logs[0].getTimestamp().minusNanos(1000), logs, outLogs);
        assertNotNull(result);
        assertEquals(0, outLogs.size());
    }
}