package pro.oblivioncoding.yonggan.airsoftgps;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import netty.client.NetworkHandler;
import pro.oblivioncoding.yonggan.airsoftgps.Fragments.MapFragment;

public class GoogleService extends Service implements LocationListener {
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "AirsoftGPS";

    Intent notificationIntent;
    public GoogleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground();
        requestLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    public void stopService(){
        PendingIntent.getService(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private void startForeground() {
        notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);


        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager =
                    (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("default",
                    NOTIF_CHANNEL_ID,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel description");
            notificationManager.createNotificationChannel(channel);
        }

        startForeground(NOTIF_ID, new NotificationCompat.Builder(getApplicationContext(),
                "default") // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("LocationBla", "LocationChanged to " + location);

        if (MainActivity.getMapFragment() != null) {
            MainActivity.getMapFragment().setOwnMarker(location);
        }

        if (MainActivity.nettyClient != null && NetworkHandler.loggedIN) {
            MainActivity.nettyClient.sendClientPositionOUTPackage(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            MainActivity.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, MainActivity.locationListener);
        }
    }
}
