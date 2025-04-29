package ruburgers.mobile;

public class Drink {
    private String name;
    private int imageResourceId;

    public Drink(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
