package team.fcisquare;

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
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        Intent intent = new Intent(this, CheckIn.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void OnClickViewLastPosition(View view){
        //// TODO: 3/27/2016 to be removed later, this is just for testing
        HashMap<String, String> params = new HashMap<>();
        final TextView longText = (TextView)findViewById(R.id.lon);
        final TextView latText = (TextView)findViewById(R.id.lat);
        final TextView placeNameText = (TextView)findViewById(R.id.user_profile_place_name);
        params.put("id", user.getId().toString());
        Connection connection = new GetConnection(params, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    longText.setText(json.getString("long"));
                    latText.setText(json.getString("lat"));
                    placeNameText.setText(json.getString("name"));
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
