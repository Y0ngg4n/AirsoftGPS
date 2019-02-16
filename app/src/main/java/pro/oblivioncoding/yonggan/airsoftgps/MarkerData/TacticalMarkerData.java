package pro.oblivioncoding.yonggan.airsoftgps.MarkerData;

public class TacticalMarkerData {

    private int id;
    private double latitude, longitude;

    private String teamname, title, description, creator;

    public TacticalMarkerData(double latitude, double longitude, int id, String title, String teamname, String description, String creator) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.teamname = teamname;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTeamname() {
        return teamname;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getCreator() {
        return creator;
    }
}
