package pro.oblivioncoding.yonggan.airsoftgps.MarkerData;

public class HQMakerData {

    private double latitude, longitude;

    private String title, description, username;

    private boolean own;

    public HQMakerData(double latitude, double longitude, String title, String description, String username, boolean own) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
        this.username = username;
        this.own = own;
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

    public boolean isOwn() {
        return own;
    }
}
