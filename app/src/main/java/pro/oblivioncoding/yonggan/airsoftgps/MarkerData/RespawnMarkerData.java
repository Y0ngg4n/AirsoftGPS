package pro.oblivioncoding.yonggan.airsoftgps.MarkerData;

public class RespawnMarkerData {

    private double latitude, longitude;

    private String title, description, username;

    public RespawnMarkerData(double latitude, double longitude, String title, String description, String username) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
        this.username = username;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }
}
