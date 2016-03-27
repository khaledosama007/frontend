package team.fcisquare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    /**
     * Just the instructor
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        //testing connection

    }
}
