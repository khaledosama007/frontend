package team.fcisquare;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by lenovo on 24/04/2016.
 */
public class PlacePop extends Activity {
    private TextView name;
    private TextView desc;
    private Place place;
    private User user;
    private Button save;
    private HashMap<String,String> params;
    private JSONObject json;
    private final double WINDOW_SIZE = 0.8;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_pop);
        place = (Place) getIntent().getSerializableExtra("place");
        user = (User) getIntent().getSerializableExtra("user");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        getWindow().setLayout((int)(widthPixels*WINDOW_SIZE) , (int)(heightPixels*WINDOW_SIZE));

        //Initiate Place First
        name =(TextView) findViewById(R.id.nameLabel);
        name.setText(place.getName());
        desc = (TextView)findViewById(R.id.placeDesc);
        desc.setText(place.getDescription());
        save = (Button)findViewById(R.id.savebutton);
    }
    public void onClickSavePlace(View v){
        place = new Place();
        params = new HashMap<String , String>();
        params.put("userid" , getIntent().getStringExtra("id"));
        params.put("placeid", place.getId().toString());
        Connection con = new PostConnection(params, new ConnectionListener() {
            @Override

            public void getResult(String result) {
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
                    if(place.getName() != null) {
                        Toast.makeText(getApplicationContext(), place.getName() + "Saved !", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Place is Already Saved , Or somthing went wrong !", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        con.execute(URIs.SAVE_PLACE);
    }

}
