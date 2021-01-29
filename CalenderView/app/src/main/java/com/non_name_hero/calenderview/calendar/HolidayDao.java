package com.non_name_hero.calenderview.calendar;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class HolidayDao {
    @Query("select date from holidays order by date desc limit 1")
    public abstract String getHolidayName();

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    abstract void insert(User user);
//
//    @Query("delete from user where id = :id")
//    abstract void delete(long id);
}
