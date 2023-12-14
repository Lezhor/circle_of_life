package com.android.circleoflife.database.models.type_converters;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.UUID;

public class UUIDConverterTest {

    @Test
    public void testUUIDToStringAndBack() {
        UUID uuid = UUID.randomUUID();

        String str = UUIDConverter.uuidToString(uuid);

        UUID convertedUUID = UUIDConverter.uuidFromString(str);

        assertEquals(uuid, convertedUUID);
    }

    @Test
    public void testStringToUUIDAndBack() {
        String str = "8c8a5cad-bd3c-4239-bff4-01891b506f24";

        UUID uuid = UUIDConverter.uuidFromString(str);

        String convertedString = uuid.toString();

        assertEquals(str, convertedString);
    }

    @Test
    public void testNullStringToUUID() {
        assertNull(UUIDConverter.uuidFromString(null));
    }

    @Test
    public void testNullUUIDToString() {
        assertNull(UUIDConverter.uuidToString(null));
    }

}