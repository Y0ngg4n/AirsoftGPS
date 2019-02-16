package pro.oblivioncoding.yonggan.airsoftgps.MarkerData;

public class MissionMarkerData {

    private double latitude, longitude;

    private String title, description, creator;
private int markerID;

    public MissionMarkerData(double latitude, double longitude, int markerID, String title, String description, String creator) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.markerID = markerID;
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

    public String getCreator() {
        return creator;
    }

    public int getMarkerID() {
        return markerID;
    }
}
