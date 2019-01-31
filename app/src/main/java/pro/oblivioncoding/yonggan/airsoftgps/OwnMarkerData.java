package pro.oblivioncoding.yonggan.airsoftgps;

import android.content.Context;
import android.view.View;

public class OwnMarkerData {
    public static double latitude;
    public static double longitude;
    public static String title;

    public OwnMarkerData(Context context, String title, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
    }
}
