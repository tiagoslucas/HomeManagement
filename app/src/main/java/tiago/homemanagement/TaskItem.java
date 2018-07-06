package tiago.homemanagement;

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

    public long getTime(){ return Long.parseLong(date); }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDate(long time) { this.date = String.valueOf(time); }

    public int getSettid() {
        return settid;
    }

    public void setSettid(int settid) {
        this.settid = settid;
    }
}