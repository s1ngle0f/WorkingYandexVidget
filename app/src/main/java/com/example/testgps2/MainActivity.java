package com.example.testgps2;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LocListenerInterface {

    private static final String TAG = "PARSEDG";
    private LocationManager locationManager;
    private MylocationListener mylocationListener;
    private String x, y;
    private String urlwithsctx = "https://yandex.ru/maps/213/moscow/search/%D0%BE%D1%81%D1%82%D0%B0%D0%BD%D0%BE%D0%B2%D0%BA%D0%B0/?ll=" + y +"%2C" + x + "&sctx=ZAAAAAgBEAAaKAoSCacz4mT2qUJAEaE5C5us0UtAEhIJAAAYFblnYT8RAABUzT8HRz8oCkC7%2FwZIAVXNzMw%2BWABqAnJ1cACdAc3MTD2gAQCoAQC9AVthKHzCAZQBqL%2FE6MgGqKr%2Fi5MC7uX7io0Fxv2WnpAD1czLjPMBxYWQvM8Fit6Z9bwCk4%2Fj4%2FcDxsb6%2FcECisrk2o8FjZzatq0FgvrW3Y4C0Pm0ubgG4pmdmlKGrd2E6AOq7dONkQaU4vemjAbqo7OMwwaz4bC83QOlvpn5ygLxmPz5owaK%2FvO%2FMJyH1%2BLKA6fLncaEBYmcgK7DAQ%3D%3D&sll="+ y +"%2C" + x + "&sspn=0.001915%2C0.000633&z=21";
    private Parse parse = new Parse();
    private ArrayList<String> Busses = new ArrayList<>();
    private ArrayList<String> timeofBusses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mylocationListener = new MylocationListener();
        mylocationListener.setLocListenerInterface(this);
        checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 777 && grantResults[0] == RESULT_OK){
            checkPermissions();
        }else{
            Toast.makeText(this, "NOT GPS", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissions()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 777);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*2, 5, mylocationListener);
        }
    }

    @Override
    public void OnLocationChanged(Location loc) {
        x = String.valueOf(loc.getLatitude());
        y = String.valueOf(loc.getLongitude());
        urlwithsctx = "https://yandex.ru/maps/213/moscow/search/%D0%BE%D1%81%D1%82%D0%B0%D0%BD%D0%BE%D0%B2%D0%BA%D0%B0/?ll=" + y +"%2C" + x + "&sctx=ZAAAAAgBEAAaKAoSCacz4mT2qUJAEaE5C5us0UtAEhIJAAAYFblnYT8RAABUzT8HRz8oCkC7%2FwZIAVXNzMw%2BWABqAnJ1cACdAc3MTD2gAQCoAQC9AVthKHzCAZQBqL%2FE6MgGqKr%2Fi5MC7uX7io0Fxv2WnpAD1czLjPMBxYWQvM8Fit6Z9bwCk4%2Fj4%2FcDxsb6%2FcECisrk2o8FjZzatq0FgvrW3Y4C0Pm0ubgG4pmdmlKGrd2E6AOq7dONkQaU4vemjAbqo7OMwwaz4bC83QOlvpn5ygLxmPz5owaK%2FvO%2FMJyH1%2BLKA6fLncaEBYmcgK7DAQ%3D%3D&sll="+ y +"%2C" + x + "&sspn=0.001915%2C0.000633&z=21";
        Log.d(TAG, urlwithsctx);
        parse.parse(urlwithsctx);
        try{
            Thread.sleep(5000);
        }
        catch(InterruptedException e){
            Log.e(TAG, e.toString());
        }
        Busses = parse.getBusses();
        timeofBusses = parse.getTimeofBusses();
        Log.d(TAG,Busses.toString());
        Log.d(TAG,timeofBusses.toString());
        Toast.makeText(this, x + " " + y, Toast.LENGTH_SHORT).show();

    }
}