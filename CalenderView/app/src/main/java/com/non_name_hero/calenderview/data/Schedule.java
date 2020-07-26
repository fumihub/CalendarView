package com.non_name_hero.calenderview.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity(tableName = "schedule")
public class Schedule {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "schedule_id")
    private long mScheduleId;

    @Nullable
    @ColumnInfo(name = "title")
    private String mTitle;

    @Nullable
    @ColumnInfo(name = "description")
    private String mDescription;

    @NonNull
    @ColumnInfo(name = "start_at_datetime")
    private Calendar mStartAtDatetime;

    @Nullable
    @ColumnInfo(name = "end_at_datetime")
    private Calendar mEndAtDatetime;

    @Nullable
    @ColumnInfo(name = "payment_id")
    private int mPaymentId;

    @Nullable
    @ColumnInfo(name = "group_id")
    private int mGroupId;

    @ColumnInfo(name = "start_timestamp")
    private String mStartTimestamp;

    @ColumnInfo(name = "end_timestamp")
    private String mEndTimestamp;

    public Schedule(@NonNull long scheduleId,
                    @NonNull String title,
                    @NonNull String description,
                    @NonNull Calendar startAtDatetime,
                    @Nullable Calendar endAtDatetime,
                    @Nullable int groupId,
                    @Nullable int paymentId){
        mScheduleId = scheduleId;
        mTitle = title;
        mDescription = description;
        mStartAtDatetime = startAtDatetime;
        mEndAtDatetime = endAtDatetime;
        mPaymentId = paymentId;
        mGroupId = groupId;
    }
    @Ignore
    public Schedule(@NonNull String title,
                    @NonNull String description,
                    @NonNull Calendar startAtDatetime,
                    @Nullable Calendar endAtDatetime,
                    @Nullable int groupId,
                    @Nullable int paymentId){
        this(0, title, description, startAtDatetime, endAtDatetime, groupId, paymentId);
    }

//    /**
//     * Use this constructor to create a new active schedule.
//     *
//     * @param title       title of the task
//     * @param description description of the task
//     */
//    @Ignore
//    public Schedule(@Nullable String title, @Nullable String description) {
//        this(title, description, UUID.randomUUID().toString(), false);
//    }

    @NonNull
    public long getScheduleId() {
        return mScheduleId;
    }

    public void setScheduleId(@Nullable long scheduleId) { this.mScheduleId = scheduleId; }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(@Nullable String mDescription) {
        this.mDescription = mDescription;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@Nullable String mTitle) {
        this.mTitle = mTitle;
    }

    @Nullable
    public Calendar getStartAtDatetime() {
        return mStartAtDatetime;
    }

    public void setStartAtDatetime(@NonNull Calendar mStartAtDatetime) {
        this.mStartAtDatetime = mStartAtDatetime;
    }

    @Nullable
    public Calendar getEndAtDatetime() {
        return mEndAtDatetime;
    }

    public void setEndAtDatetime(@Nullable Calendar mEndAtDatetime) {
        this.mEndAtDatetime = mEndAtDatetime;
    }

    @Nullable
    public int getGroupId() {
        return mGroupId;
    }

    public void setmGroupId(int mGroupId) {
        this.mGroupId = mGroupId;
    }

    @Nullable
    public int getPaymentId() {
        return mPaymentId;
    }

    public void setPaymentId(int mPaymentId) {
        this.mPaymentId = mPaymentId;
    }

    public String getStartTimestamp(){ return mStartTimestamp; }

    public void setStartTimestamp(String startTimestamp){
        mStartTimestamp = startTimestamp;
    }

    public String getEndTimestamp() { return mEndTimestamp; }

    public void setEndTimestamp(String startTimestamp){
        mEndTimestamp = startTimestamp;
    }
}
