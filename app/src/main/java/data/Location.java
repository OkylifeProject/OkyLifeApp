package data;

/**
 * Created by mordreth on 11/15/15.
 */
public class Location {
    private android.location.Location location;
    private double time;

    public Location(android.location.Location location, double time) {
        this.location = location;
        this.time = time;
    }

    public android.location.Location getLocation() {
        return location;
    }

    public void setLocation(android.location.Location location) {
        this.location = location;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
