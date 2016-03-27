package team.fcisquare;

import android.app.Activity;
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
      /*  t = (TextView)findViewById(R.id.user_profile_image);
        HashMap<String, String> h = new HashMap<>();
   //     h.put("email", "mhmdsamir@gmail.comusers");
  //      h.put("pass", "123");
        h.put("srcid", "1");
        h.put("dstid","2");
        connection = new PostConnection(h, new ConnectionListener() {
            @Override
            public void getResult(String result) {
               try{
                   JSONObject json = new JSONObject(result);
                   t.setText(json.getString("status"));
                   Toast.makeText(UserProfile.this, "here", Toast.LENGTH_SHORT).show();
               }catch(Exception e) {
                   Toast.makeText(UserProfile.this, "shit", Toast.LENGTH_SHORT).show();
               }
            }
        });
        connection.execute(URIs.POST_FOLLOW);*/
    }
}
