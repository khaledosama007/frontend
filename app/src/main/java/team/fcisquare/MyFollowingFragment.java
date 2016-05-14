package team.fcisquare;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andy on 5/13/2016.
 */
public class MyFollowingFragment extends Fragment {
    private View view;
    private ListView listView;
    private Connection connection;
    private HashMap<String, String> parameters;
    private User user;
    private FollowingAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.following, container, false);
        listView = (ListView) view.findViewById(R.id.following_list);
        user =(User) getArguments().getSerializable("user");

        parameters = new HashMap<>();
        parameters.put("id", user.getId().toString());
        updateList();

        return view;
    }

    private void updateList() {
        final ArrayList<User> users = new ArrayList<>();
        connection = new GetConnection(parameters, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                if(result == ""){
                    adapter = new FollowingAdapter(getActivity(), users, user);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    return;
                }
                try {
                    JSONArray array = new JSONArray(result);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        User tempUser = new User();
                        tempUser.setName(object.getString("name"));
                        tempUser.setId(object.getInt("id"));
                        users.add(tempUser);
                    }
                    adapter = new FollowingAdapter(getActivity(), users, user);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }

          //      adapter.notifyDataSetChanged();///
            }
        });
        connection.execute(URIs.GET_USER_FOLLOWERS);
    }

    class FollowingAdapter extends ArrayAdapter<ArrayList<User>>{
        private ArrayList<User> users;
        private User user;
        public FollowingAdapter(Context context, ArrayList<User> users, User user){
            super(context, R.layout.following);
            this.users = users;
            this.user = user;
        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.search_user, null);
            TextView name = (TextView) convertView.findViewById(R.id.search_user_name);
            name.setText(users.get(position).getName());
            Button unfollow = (Button) convertView.findViewById(R.id.follow_user);
            unfollow.setText("Unfollow");
            unfollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("srcid", user.getId().toString());
                    params.put("dstid", users.get(position).getId().toString());
                    Connection tempConnection = new PostConnection(params, new ConnectionListener() {
                        @Override
                        public void getResult(String result) {
                            updateList();
                        }
                    });
                    tempConnection.execute(URIs.POST_UN_FOLLOW);
                }
            });
            return convertView;
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible)
            updateList();
    }
}
