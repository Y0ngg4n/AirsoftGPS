package pro.oblivioncoding.yonggan.airsoftgps;

import android.content.Context;
import android.view.View;

public class MarkerData {

    public static double latitude;
    public static double longitude;
    public static String title, timestamp,teamname;
    public static boolean alive, underfire, mission, support;


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
}

