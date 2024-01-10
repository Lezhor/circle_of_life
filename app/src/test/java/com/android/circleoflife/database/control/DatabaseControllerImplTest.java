package com.android.circleoflife.database.control;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class DatabaseControllerImplTest {


    @Test
    public void clearDatabaseShouldNotBeSupported() {
        assertFalse(DatabaseControllerImpl.CLEARING_DATABASE_SUPPORTED);
    }
}