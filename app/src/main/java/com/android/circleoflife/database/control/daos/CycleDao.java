package com.android.circleoflife.database.control.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Query;

import com.android.circleoflife.database.models.Accomplishment;
import com.android.circleoflife.database.models.Category;
import com.android.circleoflife.database.models.Cycle;
import com.android.circleoflife.database.models.User;

import java.util.List;
import java.util.UUID;

@Dao
public interface CycleDao extends BaseDao<Cycle> {



    @Query("SELECT * FROM cycles WHERE userID = :userID ORDER BY cycle_name")
    LiveData<List<Cycle>> getAllCycles(UUID userID);

    @Ignore
    default LiveData<List<Cycle>> getAllCycles(User user) {
        return getAllCycles(user.getId());
    }

    @Query("SELECT * FROM categories WHERE userID = :userID AND ID = :categoryID LIMIT 1")
    LiveData<Category> getCategory(UUID userID, UUID categoryID);

    @Ignore
    default LiveData<Category> getCategory(Cycle cycle) {
        return getCategory(cycle.getUserID(), cycle.getCategoryID());
    }

    @Query("SELECT * FROM accomplishments WHERE userID = :userID AND accomplishments.cycleID = :cycleID ORDER BY timestamp DESC")
    LiveData<List<Accomplishment>> getAccomplishments(UUID userID, UUID cycleID);

    @Ignore
    default LiveData<List<Accomplishment>> getAccomplishments(Cycle cycle) {
        return getAccomplishments(cycle.getUserID(), cycle.getId());
    }

    /**
     * Deletes EVERYTHING from cycles table - use with caution
     */
    @Query("DELETE FROM cycles")
    void deleteEverythingFromTable();

}
