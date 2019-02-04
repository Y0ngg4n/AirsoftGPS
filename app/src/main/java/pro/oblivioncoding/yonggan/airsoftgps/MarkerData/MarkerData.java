package pro.oblivioncoding.yonggan.airsoftgps.MarkerData;


public class MarkerData {

    private double latitude;
    private double longitude;
    private String title, timestamp,teamname;
    private boolean alive, underfire, mission, support;


    public MarkerData(String title, double latitude, double longitude, String timestamp, String teamname, boolean alive, boolean underfire, boolean mission, boolean support){
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.title = title;
        this.teamname = teamname;
        this.alive = alive;
        this.underfire = underfire;
        this.mission = mission;
        this.support = support;
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

    public String getTimestamp() {
        return timestamp;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isMission() {
        return mission;
    }

    public boolean isSupport() {
        return support;
    }

    public boolean isUnderfire() {
        return underfire;
    }
}


