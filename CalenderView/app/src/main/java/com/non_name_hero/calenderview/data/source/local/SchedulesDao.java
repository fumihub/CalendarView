package com.non_name_hero.calenderview.data.source.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SchedulesDao {
    @Query("SELECT * FROM schedule")
    List<User> getAll();

    @Query("SELECT * FROM schedule WHERE schedule_id IN (:scheduleIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM schedule WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}
