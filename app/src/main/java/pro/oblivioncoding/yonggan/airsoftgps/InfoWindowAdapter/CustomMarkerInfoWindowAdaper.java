package pro.oblivioncoding.yonggan.airsoftgps.InfoWindowAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

import pro.oblivioncoding.yonggan.airsoftgps.R;

public class CustomMarkerInfoWindowAdaper implements GoogleMap.InfoWindowAdapter {

    private View mWindow;
    private Context mContext;

    private double latitude;
    private double longitude;
    private String title, timestamp,teamname;
    private boolean alive, underfire, mission, support;

    public CustomMarkerInfoWindowAdaper(Context context, String title, double latitude, double longitude, String timestamp, String teamname, boolean alive, boolean underfire, boolean mission, boolean support) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_marker_info_window, null);
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

    private void renderWindowText(Marker marker, View view) {
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.latitude)).setText("Lat: " + String.valueOf(latitude));
        ((TextView) view.findViewById(R.id.longitude)).setText("Long: " + String.valueOf(longitude));
        ((TextView) view.findViewById(R.id.timestamp)).setText("Time: " + timestamp);
        ((TextView) view.findViewById(R.id.teamname)).setText("Team: " + teamname);
        ((TextView) view.findViewById(R.id.alive)).setText("Alive: " + alive);
        ((TextView) view.findViewById(R.id.underfire)).setText("Under Fire: " + underfire);
        ((TextView) view.findViewById(R.id.mission)).setText("Mission: " + underfire);
        ((TextView) view.findViewById(R.id.support)).setText("Support: " + underfire);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return mWindow;
    }
}
