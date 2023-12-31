package com.android.circleoflife.logging.model;

import static org.junit.Assert.*;

import com.android.circleoflife.database.models.Accomplishment;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.Todo;
import com.android.circleoflife.database.models.User;
import com.android.circleoflife.database.models.additional.CycleFrequency;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class DBLogTest {

    User user;
    Category category;
    Cycle cycle;
    Todo todo;
    Accomplishment accomplishment;

    @Before
    public void setUp() {
        user = new User(UUID.randomUUID(), "john_doe", "SuPeRcOoL_pAsSwOrD", LocalDateTime.now());
        category = new Category(UUID.randomUUID(), "john_doe", user.getId(), null);
        cycle = new Cycle(UUID.randomUUID(), "Cycle", user.getId(), category.getId(), 0, CycleFrequency.fromBinaryString("10010001"));
        todo = new Todo(UUID.randomUUID(), "Cycle", user.getId(), category.getId(), 0, true, LocalDateTime.of(2024, 2, 20, 22, 0));
        accomplishment = new Accomplishment(UUID.randomUUID(), user.getId(), null, todo.getId(), "Did somthing!!", "this is a description of what has been done", -1, LocalDate.of(2023, 12, 31), LocalTime.of(23, 59, 59), 1000);
    }


    @Test
    public void testLogParsing() {
        DBLog<Category> log1 = new DBLog<>(category, DBLog.ChangeMode.UPDATE);
        DBLog<Todo> log2 = new DBLog<>(todo, DBLog.ChangeMode.INSERT);
        DBLog<User> log3 = new DBLog<>(user, DBLog.ChangeMode.DELETE);
        DBLog<Accomplishment> log4 = new DBLog<>(UUID.randomUUID(), user.getId(), accomplishment, DBLog.ChangeMode.DELETE, LocalDateTime.of(2023, 12, 28, 14, 43, 14, 426));
        assertEquals(log1, DBLog.fromString(DBLog.toString(log1)));
        assertEquals(log2, DBLog.fromString(DBLog.toString(log2)));
        assertEquals(log3, DBLog.fromString(DBLog.toString(log3)));
        assertEquals(log4, DBLog.fromString(DBLog.toString(log4)));
    }

}