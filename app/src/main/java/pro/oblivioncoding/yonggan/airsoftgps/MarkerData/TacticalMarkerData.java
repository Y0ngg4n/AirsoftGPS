package pro.oblivioncoding.yonggan.airsoftgps.MarkerData;

public class TacticalMarkerData {

    private double latitude, longitude;

    private String teamname, title, description, username;

    public TacticalMarkerData(double latitude, double longitude, String title, String teamname, String description, String username) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.username = username;
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

    public String getUsername() {
        return username;
    }
}
