package team.fcisquare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * This activity handles the profile for a user
 *
 * @author ANdrew ALbert
 * @since 23/3/2016
 * @version 1.0
 */
public class UserProfile extends Activity {
    private Connection connection;
    private TextView t;
    private User user;
    private Bundle bundle;
    private TextView userName;
    /**
     * Just the instructor
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
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
       /* Intent intent = new Intent(getBaseContext(), );
        intent.putExtra("id", user.getId());
        startActivity(intent);*/
    }
    
    public void OnClickViewProfileDetails(View view){
        // // TODO: 3/26/2016 to be added in next phases
    }
}
