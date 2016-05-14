package team.fcisquare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Khaled on 27/03/2016.
 */
public class Followers extends Activity {
    private HashMap<String, String> params;
    //ArrayAdapter ad;
    //JSONObject profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        // ListView list =(ListView)findViewById(R.id.followerList);
        Toast.makeText(Followers.this, "Success", Toast.LENGTH_SHORT).show();

        InitiateList();//ListView of Followers
    }
    void InitiateList(){
       params = new HashMap<String , String>();
        Intent intent = getIntent();
        //Integer i = intent.getIExtra("id");
       params.put("id" ,intent.getStringExtra("id"));
        Connection con = new GetConnection(params, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                TextView t =(TextView) findViewById(R.id.followerList);
                if(result.equals("no followers")){
                    t.setText("No Followers yet !");
                }else {
                    t.setText(result);
                }

            }
        });
        con.execute(URIs.GET_FOLLOWERS);
    }

}
