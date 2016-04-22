package team.fcisquare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * This activity handles the profile for a user
 *
 * @author ANdrew ALbert
 * @since 23/3/2016
 * @version 1.0
 */
public class UserProfile extends AppCompatActivity {
    private Connection connection;
    private TextView t;
    private User user;
    private Bundle bundle;
    private TextView userName;
    private Toolbar toolbar;
    /**
     * Just the instructor
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        userName = (TextView)findViewById(R.id.user_profile_name);
        userName.setText(user.getName());

    }
    public void OnClickCheckIn(View view){
        //// TODO: 3/26/2016 taha's code here
    }

    public void OnClickViewFollowers(View view){
        //// TODO: 3/26/2016 khaled function here
        Intent intent = new Intent(getBaseContext(),Followers.class);
        intent.putExtra("id", user.getId().toString());
        startActivity(intent);

    }
    public void OnClickFollow(View view){
        HashMap  <String , String> data = new HashMap<String , String>();
        data.put("srcid", user.getId().toString());
        data.put("dstid", "5"); // Temporary user ID for testing till we implement search service
        Connection con = new PostConnection(data, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                try {
                    JSONObject get = new JSONObject(result);
                    Log.i("Json Follow", get.getString("status"));
                    if(get.getString("status").equals("1")){
                        Toast.makeText(UserProfile.this , "Followed Successfully !" , Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(UserProfile.this , "You Already Following This User !" , Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        con.execute(URIs.POST_FOLLOW);
    }
    public void OnClickUnFollow(View v){
        HashMap  <String , String> data = new HashMap<String , String>();
        data.put("srcid" ,user.getId().toString() );
        data.put("dstid" , "5"); // Temporary user ID for testing and till we implement search service
        Connection con = new PostConnection(data, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                try {
                    JSONObject get =new JSONObject(result);
                    Log.i("Json UnF" , get.getString("status"));
                    if(get.getString("status").equals("1")){
                        Toast.makeText(UserProfile.this , "UnFollowed Successfully !" , Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(UserProfile.this , "You are not Following This User !" , Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        con.execute(URIs.POST_UN_FOLLOW);
    }

    public void OnClickViewLastPosition(View view){
        //// TODO: 3/27/2016 to be removed later, this is just for testing
        HashMap<String, String> params = new HashMap<>();
        final TextView longText = (TextView)findViewById(R.id.lon);
        final TextView latText = (TextView)findViewById(R.id.lat);
        params.put("id", user.getId().toString());
        Connection connection = new GetConnection(params, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    longText.setText(json.getString("long"));
                    latText.setText(json.getString("lat"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        connection.execute(URIs.GET_LAST_POSITION);
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
