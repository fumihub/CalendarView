package com.non_name_hero.calenderview.data;

import java.util.Calendar;

public class Holiday {
    private String mName;
    private String mNameInJP;
    private Calendar mDate;

    public Holiday(long date,String name,String nameInJP){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        mDate = c;
        mName = name;
        mNameInJP = nameInJP;
    }

}
