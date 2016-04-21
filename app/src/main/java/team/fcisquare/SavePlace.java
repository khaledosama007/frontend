package team.fcisquare;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by lenovo on 21/04/2016.
 */
public class SavePlace extends Activity {
    private Place place;
    private HashMap<String , String> params;
    private JSONObject json=null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_place);
        place=null;
        params =null;
    }
    public void onClickSavePlace(View v){
        place = new Place();
        params = new HashMap<String , String>();
        params.put("userid" , getIntent().getStringExtra("id"));
        params.put("placeid" , "2");
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
