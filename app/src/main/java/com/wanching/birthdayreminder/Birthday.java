package com.wanching.birthdayreminder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by WanChing on 4/8/2017.
 */

public class Birthday implements Serializable{
    private long id;
    private String name;
    private String email;
    private Date date;
    private boolean notify;

//    public Birthday(long aLong, String string, String cursorString, long cursorLong){
//        this(0, "", "", Calendar.getInstance(), false);
//    }

    public Birthday(long id, String name, String email, Date date, boolean notify){
        this.id = id;
        this.name = name;
        this.email = email;
        this.date = date;
        this.notify = notify;
    }

    public Birthday(long id, String name, String email, Calendar calendar, boolean notify){
        this.id=id;
        this.name = name;
        this.email = email;
        this.notify = notify;
    }

    public long getId(){return id;}

    public String getName(){return  name;}

    public String getEmail(){return email;}

    public Date getDate(){return date;}

    public Calendar getDateAsCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        return calendar;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name){this.name = name;}

    public void setEmail(String email){this.email = email;}

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(Calendar calendar){
        calendar.set(Calendar.MILLISECOND, 0);
        this.date = new Date(calendar.getTimeInMillis());
    }

    public class Countdown {
        private long durationInMillis;
        private long days;
        private long hours;
        private long minutes;
        private long seconds;

        public long getDurationInMillis() {return durationInMillis;}
        public long getDays() {return Math.abs(days);}
        public long getHours() {return Math.abs(hours);}
        public long getMinutes() {return Math.abs(minutes);}
        public long getSeconds() {return Math.abs(seconds);}
    }

    public Countdown getCountdown() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.MILLISECOND, 0);

        long duration = date.getTime() - today.getTimeInMillis();

        Countdown countdown = new Countdown();
        countdown.durationInMillis = duration;
        countdown.days = TimeUnit.MILLISECONDS.toDays(duration);
        countdown.hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        countdown.minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        countdown.seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));

        return countdown;
    }
}
