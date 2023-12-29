package com.android.circleoflife.database.models.additional;

import static org.junit.Assert.*;

import com.android.circleoflife.database.models.*;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class EntityStringParserTest {

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
    public void testCategoryParsing() {
        String str = EntityStringParser.categoryToString(category);
        Category parsed = EntityStringParser.categoryFromString(str);
        assertEquals(category, parsed);
        assertTrue(category.equalsAllParams(parsed));
    }

    @Test
    public void testCycleParsing() {
        String str = EntityStringParser.cycleToString(cycle);
        Cycle parsed = EntityStringParser.cycleFromString(str);
        assertEquals(cycle, parsed);
        assertTrue(cycle.equalsAllParams(parsed));
    }

    @Test
    public void testTodoParsing() {
        String str = EntityStringParser.todoToString(todo);
        Todo parsed = EntityStringParser.todoFromString(str);
        assertEquals(todo, parsed);
        assertTrue(todo.equalsAllParams(parsed));
    }

    @Test
    public void testAccomplishmentParsing() {
        String str = EntityStringParser.accomplishmentToString(accomplishment);
        Accomplishment parsed = EntityStringParser.accomplishmentFromString(str);
        assertEquals(accomplishment, parsed);
        assertTrue(accomplishment.equalsAllParams(parsed));
    }

}