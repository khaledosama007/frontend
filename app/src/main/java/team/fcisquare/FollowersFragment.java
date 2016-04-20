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

import java.util.HashMap;
import java.util.List;

/**
 * Created by Andrew on 4/17/2016.
 */
public class FollowersFragment extends Fragment {
    private View view;
    private User user;
    private ListView listView;
    private String[] names;
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
        params = new HashMap<>();
        params.put("id", user.getId().toString());
        Log.e("555", user.getId().toString());
        Connection con = new GetConnection(params, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                if(result.equals("no followers")){
                        names = new String[0];
               //     Toast.makeText(getContext(), "heheheh", Toast.LENGTH_SHORT).show();
                    Log.e("555", "ya d");
                }else {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(result);

                        JSONArray array =  object.getJSONArray("followers");
                        //conerting json array to string
                        names = new String[array.length()];

                        for(int i = 0;i < array.length();i++)
                            names[i] = array.getString(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Log.e("555", result);
            }
        });
        con.execute(URIs.GET_FOLLOWERS);

        listView = (ListView)view.findViewById(R.id.followers_list_view);
    //    listView.setAdapter(new FollowersAdapter(getActivity(), names));

        return view;
    }
    class FollowersAdapter extends ArrayAdapter<String> {
        private String[] names;
        public FollowersAdapter(Context context, String[] names) {
            super(context, R.layout.followers_fragment);
            this.names = names;
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.followers_fragment, parent, false);
            }

            TextView textView = (TextView)view.findViewById(R.id.folowers_custom_view_name);
            textView.setText(names[position]);
            return convertView;
        }
    }


}
