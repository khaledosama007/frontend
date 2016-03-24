package team.fcisquare;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Andrew on 3/22/2016.
 */
public class UserProfile extends Activity {
    private Connection connection;
    private TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        //testing connection
        t = (TextView)findViewById(R.id.userAccountUserName);
        HashMap<String, String> h = new HashMap<>();
   //     h.put("email", "mhmdsamir@gmail.comusers");
  //      h.put("pass", "123");
        h.put("id", "1");
        connection = new GetConnection(h, new ConnectionListener() {
            @Override
            public void getResult(String result) {
               try{
                   JSONObject json = new JSONObject(result);
                   t.setText(json.getString("lat"));
                   Toast.makeText(UserProfile.this, "here", Toast.LENGTH_SHORT).show();
               }catch(Exception e) {
                   Toast.makeText(UserProfile.this, "shit", Toast.LENGTH_SHORT).show();
               }
            }
        });
        connection.execute("http://swproject-404error.rhcloud.com/FCISquare/rest/lastposition");
    }
}
