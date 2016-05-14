package team.fcisquare;

import android.support.v4.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Andy on 5/13/2016.
 */
public class HistoryFragment extends Fragment {
    private View view;
    private ListView listView;
    private Connection connection;
    private HashMap<String, String> parameters;
    private User user;
    private ArrayList<Pair<String, Long>> data;

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible)
            updateListView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.history_fragment, container, false);
        user = (User) getArguments().getSerializable("user");

        listView = (ListView) view.findViewById(R.id.history_items);
        parameters = new HashMap<>();
        parameters.put("id", user.getId().toString());
        updateListView();

        listView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateListView();
            }
        });
        return view;
    }

    private void updateListView() {
        data = new ArrayList<>();
        connection = new GetConnection(parameters, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                try {
                    JSONArray array = new JSONArray(result);
                    for(int i = 0;i < array.length();i++){
                        JSONObject object = array.getJSONObject(i);
                        data.add(new Pair(object.get("description"), Long.parseLong(object.getString("date"))));
                    }
                    HistoryAdapter adapter = new HistoryAdapter(getActivity(), data);
                    listView.setAdapter(adapter);
                 //   adapter.notifyDataSetChanged();
                  //  listView.setAdapter(new HistoryAdapter(getActivity(), data));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        connection.execute(URIs.GET_HISTORY);
    }

    class HistoryAdapter extends ArrayAdapter<ArrayList<Pair<String, Long>>>{
        private ArrayList<Pair<String, Long>> data;
        public HistoryAdapter(Context context, ArrayList<Pair<String, Long>> data){
            super(context, R.layout.history_fragment);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.history_item, null);
            TextView date = (TextView) convertView.findViewById(R.id.event_date);
            TextView description = (TextView) convertView.findViewById(R.id.event_description);

            description.setText(data.get(position).first);
            Date dateObject = new Date(data.get(position).second);
            date.setText(dateObject.getDay() + "/" + dateObject.getMonth() + "/" + dateObject.getYear());
            return convertView;
        }
    }
}
