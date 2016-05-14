package team.fcisquare;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrew on 4/17/2016.
 */
public class FollowersFragment extends Fragment {
    private View view;
    private User user;
    private ArrayList<User> followers;
    private ListView listView;
    private HashMap<String, String> params;

    public FollowersFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.followers_fragment, container, false);
        user = (User)getArguments().getSerializable("user");
        followers = new ArrayList<>();
        params = new HashMap<>();
        params.put("id", user.getId().toString());
        Connection con = new GetConnection(params, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                if(result == null){
                    Toast.makeText(getActivity(), "no followers", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        JSONArray array = new JSONArray(result);
                        for(int i = 0;i < array.length();i++){
                            JSONObject object = (JSONObject) array.get(i);
                            User follower = new User();
                            follower.setId(Integer.parseInt(object.getString("id")));
                            follower.setName(object.getString("name"));
                            followers.add(follower);
                        }
                        listView = (ListView)view.findViewById(R.id.followers_list_view);
                        listView.setAdapter(new FollowersAdapter(getActivity(), followers));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Log.e("333", result);
            }
        });
        con.execute(URIs.GET_FOLLOWERS);
        return view;
    }
    class FollowersAdapter extends ArrayAdapter<String> {
        private ArrayList<User> followers;

        public FollowersAdapter(Context context, ArrayList<User> followers) {
            super(context, R.layout.followers_fragment);
            this.followers = followers;
        }

        @Override
        public int getCount() {
            return followers.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.followers_custom_view, parent, false);
            }

            TextView textView = (TextView)convertView.findViewById(R.id.folowers_custom_view_name);
            textView.setText(followers.get(position).getName());
            return convertView;
        }
    }


}
