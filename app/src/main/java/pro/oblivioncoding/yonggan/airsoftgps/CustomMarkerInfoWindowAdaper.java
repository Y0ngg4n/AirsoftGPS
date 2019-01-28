package pro.oblivioncoding.yonggan.airsoftgps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

public class CustomMarkerInfoWindowAdaper implements GoogleMap.InfoWindowAdapter {

    private View mWindow;
    private Context mContext;

    private double latitude;
    private double longitude;
    private String title;
    private String timestamp;
    private String teamname;
    private boolean alive;
    private int status;

    public CustomMarkerInfoWindowAdaper(Context context, String title, double latitude, double longitude, String timestamp, String teamname, boolean alive, int status) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_marker_info_window, null);
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.title = title;
        this.teamname = teamname;
        this.alive = alive;
        this.status = status;
    }

    private void renderWindowText(Marker marker, View view) {
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.latitude)).setText("Lat: " + String.valueOf(latitude));
        ((TextView) view.findViewById(R.id.longitude)).setText("Long: " + String.valueOf(longitude));
        ((TextView) view.findViewById(R.id.timestamp)).setText("Time: " + timestamp);
        ((TextView) view.findViewById(R.id.teamname)).setText("Team: " + teamname);
        ((TextView) view.findViewById(R.id.alive)).setText("Alive: " + alive);
        ((TextView) view.findViewById(R.id.status)).setText("Status: " + status);
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
