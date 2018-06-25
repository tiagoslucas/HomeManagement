package tiago.homemanagement;

enum Types {
    laundry,
    floor,
    dishes,
    shopping
}

public class Settings {
    private int id;
    private Types type;
    private int time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        if (type == Types.laundry)
            return "laundry";
        else if (type == Types.floor)
            return "floor";
        else if (type == Types.dishes)
            return "dishes";
        else if (type == Types.shopping)
            return "shopping";
        else
            return null;
    }

    public void setType(String type) {
        if (type.toLowerCase().equals("laundry"))
            this.type = Types.laundry;
        else if (type.toLowerCase().equals("floor"))
            this.type = Types.floor;
        else if (type.toLowerCase().equals("dishes"))
            this.type = Types.dishes;
        else if (type.toLowerCase().equals("shopping"))
            this.type = Types.shopping;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
