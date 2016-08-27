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
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private Double latitude;
    private Double longitude;

    public void nearbyButtonClick(View view) {
        String version = BuildConfig.VERSION_NAME;
        String url = "https://www.hmdb.org/map.asp?nearby=yes&latitude=" + latitude + "&longitude=" + longitude + "&nearest=10&source=app-android-" + version;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);

        setState("loading");
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
        } else {
            setState("Location Access Disabled");
        }
    }

   @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        setState("ready");
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

    private void setState(String state) {
        switch (state) {
            case "loading":
                findViewById(R.id.loadingIndicator).setVisibility(View.VISIBLE);
                findViewById(R.id.button).setVisibility(View.GONE);
                findViewById(R.id.errorMessage).setVisibility(View.GONE);
                break;
            case "ready":
                findViewById(R.id.loadingIndicator).setVisibility(View.GONE);
                findViewById(R.id.button).setVisibility(View.VISIBLE);
                findViewById(R.id.errorMessage).setVisibility(View.GONE);
                break;
            default:
                findViewById(R.id.loadingIndicator).setVisibility(View.GONE);
                findViewById(R.id.button).setVisibility(View.GONE);
                TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("Location Error: " + state);
        }
    }
}
