package me.tomify.historicalmarkersnearby;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private Double latitude;
    private Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideButton();
        try {
            requestLocation();
        } catch(Exception e) {
            String[] permissions = new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION };
            ActivityCompat.requestPermissions(this, permissions, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } // else Permission Denied
    }

    public void nearbyButtonClick(View view) {
        String url = "https://www.hmdb.org/map.asp?nearby=yes&Latitude=" + latitude + "&Longitude=" + longitude;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
   }

   @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        showButton();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.removeUpdates(this);
   }
    public void onProviderEnabled(String p) {}
    public void onProviderDisabled(String s) {}
    public void onStatusChanged(String s, int i, Bundle b) {}

    private void requestLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    private void hideButton() {
        Button button = (Button) findViewById(R.id.button);
        button.setVisibility(View.GONE);
    }

    private void showButton() {
        Button button = (Button) findViewById(R.id.button);
        button.setVisibility(View.VISIBLE);
    }
}
