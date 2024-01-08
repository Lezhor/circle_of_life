package com.android.circleoflife.communication.protocols;

import static org.junit.Assert.*;

import com.android.circleoflife.application.App;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.logging.model.DBLog;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class SyncProtocolEngineTest {

    @Test
    public void testSync() {

        User user = new User(UUID.randomUUID(), "john_doe", "password", LocalDateTime.of(2023, 12, 12, 20, 30));
        Category category = new Category(UUID.randomUUID(), "University", user.getId(), null);
        DBLog<?>[] logs = new DBLog[]{new DBLog<>(category, DBLog.ChangeMode.INSERT)};
        List<DBLog<?>> outLogs = new LinkedList<>();

        SyncProtocol syncProtocol = App.getSyncProtocol();

        LocalDateTime result;
        result = syncProtocol.sync(user, user.getTimeOfCreation(), logs, outLogs);
        assertNotNull(result);
        assertEquals(0, outLogs.size());
    }
}