package me.tomify.historicalmarkersnearby;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private Double latitude;
    private Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Starting");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide Button
        // Request Location Stuff

        try {
            requestLocation();
        } catch(Exception e) {
            System.out.println("Erroring");
            System.out.println(e.getMessage());
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION }, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        }
    }

    private void requestLocation() {
        System.out.println("Requesting");
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestSingleUpdate(criteria, this, null);
    }

    public void nearbyButtonClick(View view) {
        System.out.println("Clicking");
        String url = "https://www.hmdb.org/map.asp?nearby=yes&Latitude=" + latitude + "&Longitude=" + longitude;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
   }

   @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        System.out.println("LOCATION");
        System.out.println(location);
        // Show Button
    }
    public void onProviderEnabled(String p) {}
    public void onProviderDisabled(String s) {}
    public void onStatusChanged(String s, int i, Bundle b) {}
}
