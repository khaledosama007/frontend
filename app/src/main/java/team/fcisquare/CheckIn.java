package team.fcisquare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by Andrew on 4/21/2016.
 */
public class CheckIn extends AppCompatActivity {
    private Button search;
    private Button checkin;
    private TextView searchFor;
    private EditText userComment;
    private Toolbar toolbar;
    private Place availablePlace;
    private Connection connection;
    private TextView isFounded;
    private TextView at;
    private TextView chosenPlace;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Check in");

        availablePlace = new Place();
        search = (Button) findViewById(R.id.search_place_button);
        checkin = (Button)findViewById(R.id.check_in_button);
        searchFor = (TextView)findViewById(R.id.place_name_request);
        userComment = (EditText)findViewById(R.id.user_comment_check_in);
        at = (TextView)findViewById(R.id.at_text_view);
        chosenPlace = (TextView)findViewById(R.id.user_chosen_place_for_check_in);

        userComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(v.getText().toString().equals(""))
                    checkin.setEnabled(false);
                else
                    checkin.setEnabled(true);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnClickSearch(View view){
        if(searchFor.getText().toString().equals("")){
            searchFor.setError("Please insert a place name");
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("name", searchFor.getText().toString());
        connection = new PostConnection(params, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                if(result.equals("error")){
                    isFounded.setVisibility(View.VISIBLE);
                    checkin.setEnabled(false);
                    checkin.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    isFounded.setVisibility(View.VISIBLE);
                    at.setVisibility(View.INVISIBLE);
                    chosenPlace.setVisibility(View.INVISIBLE);
                    return;
                }
                try {
                    JSONObject object = new JSONObject(result);
                    availablePlace.setId(Integer.parseInt(object.getString("id")));
                    availablePlace.setDescription(object.getString("desc"));
                    availablePlace.setLat(Double.parseDouble(object.getString("lat")));
                    availablePlace.setName(object.getString("name"));
                    availablePlace.setLon(Double.parseDouble(object.getString("long")));

                    if(!userComment.getText().toString().equals("")) {
                        checkin.setEnabled(true);
                        checkin.setBackgroundColor(getResources().getColor(R.color.blackShade));
                    }
                    isFounded.setVisibility(View.INVISIBLE);
                    at.setVisibility(View.VISIBLE);
                    chosenPlace.setText(availablePlace.getName());
                    chosenPlace.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        connection.execute();
    }
    public void OnClickCheckIn(View view){
        HashMap<String, String> params = new HashMap<>();
        params.put("useris", user.getId().toString());
        params.put("placeid", availablePlace.getId().toString());
        params.put("body", userComment.getText().toString());
        connection = new PostConnection(params, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                //// TODO: 4/21/2016 hahbb eh bel return :3
            }
        });
        this.finish();
    }
}
