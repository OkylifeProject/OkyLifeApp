package data;

/**
 * Created by mordreth on 11/15/15.
 */
public class Location {
    private double longitude;
    private double latitude;
    private double time;

    public Location(double latitude, double longitude, double time) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
