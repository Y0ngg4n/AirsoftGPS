package pro.oblivioncoding.yonggan.airsoftgps;


public class OwnMarkerData {
    private double latitude;
    private double longitude;
    private String title;

    public OwnMarkerData( String title, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
