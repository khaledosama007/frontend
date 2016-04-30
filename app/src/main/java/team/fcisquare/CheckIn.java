package team.fcisquare;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 4/21/2016.
 */
public class CheckIn extends AppCompatActivity {
    private Button search;
    private EditText searchFor;
    private EditText userComment;
    private Toolbar toolbar;
    private Connection connection;
    private User user;
    private ListView listView;
    private ArrayList<Place> places;
    private int lastSelected = -1;
    private View lastViewed = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Check in");

        user = (User) getIntent().getSerializableExtra("user");
        search = (Button) findViewById(R.id.search_place_button);
        searchFor = (EditText)findViewById(R.id.search_place_space);
        userComment = (EditText)findViewById(R.id.user_comment_check_in);
        listView = (ListView) findViewById(R.id.search_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastSelected = position;
                view.setBackgroundColor(R.color.blackShade);
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
        lastSelected = -1;
        if(searchFor.getText().toString().equals("")){
            searchFor.setError("Please insert a place name");
            return;
        }
        places = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>();
        params.put("query", searchFor.getText().toString());
        params.put("filter", String.valueOf(2));
        connection = new PostConnection(params, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("places");
                    for(int i = 0;i < array.length();i++){
                        JSONObject jsonObject = (JSONObject) array.get(i);
                        Place p = new Place();
                        p.setId(Integer.parseInt(jsonObject.getString("placeid")));
                        p.setName(jsonObject.getString("placename"));
                        p.setDescription(jsonObject.getString("descrition"));
                        places.add(p);
                    }
                    listView.setAdapter(new SearchAdapter(getBaseContext(), places));
                    listView.refreshDrawableState();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        connection.execute(URIs.POST_SEARCH);
    }
    public void OnClickCheckIn(View view){
        if(lastSelected != -1 && !userComment.getText().toString().equals("")){
            HashMap<String, String> params = new HashMap<>();
            params.put("userid", user.getId().toString());
            params.put("placeid", places.get(lastSelected).getId().toString());
            params.put("body", userComment.getText().toString());
            connection = new PostConnection(params, new ConnectionListener() {
                @Override
                public void getResult(String result) {
                    finish();
                }
            });
            connection.execute(URIs.POST_MAKE_POST);
        }
    }
    class SearchAdapter extends ArrayAdapter<Place> {
        private ArrayList<Place> places;
        public SearchAdapter(Context context, ArrayList<Place> places) {
            super(context, R.layout.check_in);
            this.places = places;
        }

        @Override
        public int getCount() {
            return places.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.check_in_place_finder, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.place_name_search);
            textView.setText(places.get(position).getName());
            TextView view = (TextView) convertView.findViewById(R.id.place_description);
            view.setText(places.get(position).getDescription());
            return convertView;
        }

        @Override
        public Place getItem(int position) {
            return places.get(position);
        }
    }
}
