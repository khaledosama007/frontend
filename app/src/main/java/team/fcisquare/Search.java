package team.fcisquare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 4/25/2016.
 */
public class Search extends AppCompatActivity {
    private Toolbar toolbar;
    private User user;
    private ArrayList<User> users;
    private ArrayList<Place> places;
    private ArrayList<Integer> followers;
    private ListView listView;
    private RadioGroup radioGroup;
    private Connection connection;
    private HashMap<String, String> params;
    private EditText searchFor;
    private ArrayList<Integer> myFollowersIDs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = (User) getIntent().getSerializableExtra("user");
        radioGroup = (RadioGroup)findViewById(R.id.radio_group_search);
        listView = (ListView)findViewById(R.id.search_list);
        searchFor = (EditText)findViewById(R.id.search_text);
        myFollowersIDs = new ArrayList<>();

        HashMap<String, String > tempParams = new HashMap<>();
        tempParams.put("id", user.getId().toString());
        Connection connection = new GetConnection(tempParams, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                try{
                    JSONArray array = new JSONArray(result);
                    for(int i = 0;i < array.length();i++){
                        JSONObject object = (JSONObject) array.get(i);
                        Integer ii = Integer.parseInt(object.getString("id"));
                        myFollowersIDs.add(ii);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        connection.execute(URIs.GET_USER_FOLLOWERS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickSearch(View view){
        if(radioGroup.getCheckedRadioButtonId() == R.id.places_radio_button){
            places = new ArrayList<>();
            params = new HashMap<>();
            params.put("query", searchFor.getText().toString());
            params.put("filter", String.valueOf(2));
            connection = new PostConnection(params, new ConnectionListener() {
                @Override
                public void getResult(String result) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = (JSONArray) object.getJSONArray("places");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            Place p = new Place();
                            p.setLat(Double.parseDouble(jsonObject.getString("placelat")));
                            p.setId(Integer.parseInt(jsonObject.getString("placeid")));
                            p.setDescription(jsonObject.getString("descrition"));
                            p.setLon(Double.parseDouble(jsonObject.getString("placelong")));
                            p.setName(jsonObject.getString("placename"));
                            places.add(p);
                        }
                        SearchPlaces searchPlaces = new SearchPlaces(getBaseContext(), places);
                        listView.setAdapter(searchPlaces);
                        searchPlaces.notifyDataSetChanged();
                        listView.refreshDrawableState();
                    }catch (Exception e){

                    }
                }
            });
            connection.execute(URIs.POST_SEARCH);
        }else if(radioGroup.getCheckedRadioButtonId() == R.id.users_radio_button){
            users = new ArrayList<>();
            params = new HashMap<>();
            params.put("query", searchFor.getText().toString());
            params.put("filter", String.valueOf(1));
            connection = new PostConnection(params, new ConnectionListener() {
                @Override
                public void getResult(String result) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = (JSONArray) object.getJSONArray("users");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            User u = new User();
                            u.setName(jsonObject.getString("username"));
                            u.setId(Integer.parseInt(jsonObject.getString("userid")));
                            users.add(u);
                        }
                        SearchUsers searchUsers = new SearchUsers(getBaseContext(), users, myFollowersIDs);
                        listView.setAdapter(searchUsers);
                        searchUsers.notifyDataSetChanged();
                        listView.refreshDrawableState();
                    }catch (Exception e){

                    }
                }
            });
            connection.execute(URIs.POST_SEARCH);
        }
    }

    class SearchPlaces extends ArrayAdapter<Place>{
        private ArrayList<Place> places;

        public SearchPlaces(Context context, ArrayList<Place> places) {
            super(context, R.layout.search);
            this.places = places;
        }

        @Override
        public int getCount() {
            return places.size();
        }

        @Override
        public Place getItem(int position) {
            return places.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.search_place, parent, false);
            TextView textView = (TextView) convertView.findViewById(R.id.search_place_name);
            Button button = (Button)convertView.findViewById(R.id.view_place);

            textView.setText(places.get(position).getName());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), PlacePop.class);
                    intent.putExtra("place", places.get(position));
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
    class SearchUsers extends ArrayAdapter<User>{
        private ArrayList<User> users;
        private ArrayList<Integer> myFollowersIDs;
        public SearchUsers(Context context,ArrayList<User> users, ArrayList<Integer> myFollowersIDs){
            super(context, R.layout.search_user);
            this.users = users;
            this.myFollowersIDs = myFollowersIDs;
        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public User getItem(int position) {
            return users.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.search_user, parent, false);
            TextView textView = (TextView)convertView.findViewById(R.id.search_user_name);
            final Button button = (Button)convertView.findViewById(R.id.follow_user);
            textView.setText(users.get(position).getName());
            if(myFollowersIDs.contains(users.get(position).getId())){
                button.setText("Unfollow");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, String> shit = new HashMap<String, String>();
                        shit.put("srcid", user.getId().toString());
                        shit.put("dstid", users.get(position).getId().toString());
                        Connection conn = new PostConnection(shit, new ConnectionListener() {
                            @Override
                            public void getResult(String result) {
                                try {
                                    JSONObject object = new JSONObject(result);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        conn.execute(URIs.POST_UN_FOLLOW);
                        button.setText("Follow");
                    }
                });
            }else{
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, String> shit = new HashMap<String, String>();
                        shit.put("srcid", user.getId().toString());
                        shit.put("dstid", users.get(position).getId().toString());
                        Connection conn = new PostConnection(shit, new ConnectionListener() {
                            @Override
                            public void getResult(String result) {

                            }
                        });
                        conn.execute(URIs.POST_FOLLOW);
                        button.setText("Unfollow");
                    }
                });
            }
            return convertView;
        }
    }
}
