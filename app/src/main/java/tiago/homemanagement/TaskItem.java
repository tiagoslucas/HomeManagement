package tiago.homemanagement;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskItem {
    private int id;
    private String name;
    private boolean done;
    private String date;
    private int settid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = (done == 1);
    }

    public String getDate() {
        return date;
    }

    public long getTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        long time = System.currentTimeMillis();
        try {
            Date dateType = dateFormat.parse(this.date);
            time = dateType.getTime() / 1000L;
        } catch (ParseException e) { }
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDate(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time * 1000L);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        this.date = date;
    }

    public int getSettid() {
        return settid;
    }

    public void setSettid(int settid) {
        this.settid = settid;
    }
}