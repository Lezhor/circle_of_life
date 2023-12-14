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

@Dao
public interface CycleDao extends BaseDao<Cycle> {

    @Query("SELECT * FROM cycles WHERE uid = :userID ORDER BY cycle_name")
    LiveData<List<Cycle>> getAllCycles(int userID);

    @Ignore
    default LiveData<List<Cycle>> getAllCycles(User user) {
        return getAllCycles(user.getId());
    }

    @Query("SELECT * FROM categories WHERE uid = :userID AND category_name LIKE :cycleName LIMIT 1")
    LiveData<Category> getCategory(int userID, String cycleName);

    @Ignore
    default LiveData<Category> getCategory(Cycle cycle) {
        return getCategory(cycle.getUserID(), cycle.getCategory());
    }

    @Query("SELECT * FROM accomplishments WHERE uid = :userID AND cycle LIKE :cycleName ORDER BY timestamp DESC")
    LiveData<List<Accomplishment>> getAccomplishments(int userID, String cycleName);

    @Ignore
    default LiveData<List<Accomplishment>> getAccomplishments(Cycle cycle) {
        return getAccomplishments(cycle.getUserID(), cycle.getName());
    }


}
