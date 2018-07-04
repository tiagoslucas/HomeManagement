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

    public Settings(){
        this.id = 0;
        type = Types.laundry;
        this.time = 0;
    }

    public Settings(int id, String type, int time){
        this.id = id;
        setType(type);
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        switch (type) {
            case laundry:
                return "laundry";
            case floor:
                return "floor";
            case dishes:
                return "dishes";
            case shopping:
                return "shopping";
            default:
                return null;
        }
    }

    public void setType(String type) {
        switch (type.toLowerCase()) {
            case "laundry":
                this.type = Types.laundry;
                break;
            case "floor":
                this.type = Types.floor;
                break;
            case "dishes":
                this.type = Types.dishes;
                break;
            case "shopping":
                this.type = Types.shopping;
                break;
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
