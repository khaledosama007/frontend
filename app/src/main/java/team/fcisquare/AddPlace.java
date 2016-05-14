package team.fcisquare;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by Andrew on 4/19/2016.
 */
public class AddPlace extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button getPosition;
    private Button addPlace;
    private boolean error;
    private EditText description;
    private EditText placeName;
    private EditText latitude;
    private EditText longitude;
    private Toolbar toolbar;
    private Place place;
    private HashMap<String, String> params;
    private JSONObject json;
    private final int REQUEST_CODE = 10;
    private final int TIME_IN_MSECOND = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_place);

        getPosition = (Button) findViewById(R.id.get_position_add_place);
        addPlace = (Button) findViewById(R.id.add_place_button);
        description = (EditText) findViewById(R.id.place_description_add_place);
        placeName = (EditText) findViewById(R.id.place_name_add_place);
        latitude = (EditText) findViewById(R.id.latitude_add_place);
        longitude = (EditText) findViewById(R.id.longitude_add_place);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        toolbar = (Toolbar)findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add place");
    }

    public void onClickAddPlace(View view) {
        error = false;

        checkErrors();
        params = new HashMap<String , String>();
        if (!error) {
            place = new Place();
            params.put("name" , placeName.getText().toString());
            params.put("desc", description.getText().toString());
            params.put("lat", latitude.getText().toString());
            params.put("lon" , longitude.getText().toString());

            Connection con = new PostConnection(params, new ConnectionListener() {
                @Override

                public void getResult(String result) {
                    Log.i("result" , result);
                    try {
                        json = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        place.setName(json.getString("name"));
                        place.setDescription(json.getString("desc"));
                        place.setLon(Double.parseDouble(json.getString("lon")));
                        place.setLat(Double.parseDouble(json.getString("lat")));
                        Log.i("String", place.getName());
                        Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            con.execute(URIs.ADD_PLACE);
            finish();
        }
    }

    private void checkErrors() {
        if (description.getText().toString().equals("")) {
            description.setError("Description must be included");
            error = true;
        }

        if (placeName.getText().toString().equals("")) {
            placeName.setError("Name must be included");
            error = true;
        } else if (!placeName.getText().toString().matches("^[a-zA-Z]+$")) {
            placeName.setError("Invalid name");
            error = true;
        }

        if (latitude.getText().toString().equals("")) {
            latitude.setError("Latitude can't be empty");
            error = true;
        } else if (!isNumeric(latitude.getText().toString())) {
            latitude.setError("Invalid position");
            error = true;
        }

        if (longitude.getText().toString().equals("")) {
            longitude.setError("Longitude can't be empty");
            error = true;
        } else if (!isNumeric(longitude.getText().toString())) {
            longitude.setError("Invalid position");
            error = true;
        }
    }

    public void onClickGetPosition(View view) {
        Log.e("333", "-_-");
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude.setText(location.getLatitude() + "");
                longitude.setText((location.getLongitude() + ""));
                android.util.Log.e("333", location.getLatitude() + "");
                sayBye();//stop the listener

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) { //called when GPS is off
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); // references to GPS settings
            }
        };
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (permissionNeeded()) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
                return;
            } else {
                getPositionGPS();
            }
        }else{
            getPositionGPS();
        }
    }

    private boolean permissionNeeded() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }


    private void sayBye() {
        try {
            locationManager.removeUpdates(locationListener);
        }catch(SecurityException e){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            getPositionGPS();
    }

    private void getPositionGPS() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_IN_MSECOND, 0, locationListener);
        }catch (SecurityException e){
            Log.e("333","shit");
        }
    }
    public static boolean isNumeric(String str) {
        try {
            double num = Double.parseDouble(str);
        }
        catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
