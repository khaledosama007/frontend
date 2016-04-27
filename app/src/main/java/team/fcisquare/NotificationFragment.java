package team.fcisquare;

import android.app.*;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 4/17/2016.
 */
public class NotificationFragment extends Fragment {
    private View view;
    private ArrayList<String> desc;
    private ListView li;
    private NotificationList notificationList;
    private CustomBroadcast br;
    private Bundle bundle = new Bundle();
    private JSONObject json;
    private HashMap <String , String> params;
    private User user;
    private ArrayList<team.fcisquare.Notification> returned; //returned from service
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public NotificationFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //LocalBroadcastManager.getInstance(getActivity()).registerReceiver(br , new IntentFilter("FILTER"));
        bundle = getArguments();
        desc = new ArrayList<>();
        view = inflater.inflate(R.layout.notification_fragment,container,false);

         li = (ListView) view.findViewById(R.id.notification_list);
       //for testing
       /* desc.add("Notification 1");
        desc.add("Notification 2");
        desc.add("Notification 2");*/
        notificationList = new NotificationList(getActivity(), desc);
        li.setAdapter(notificationList);
        /////////////////////////////////////////////////////////////////////////
        li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent show = new Intent(getActivity() , ShowPost.class);
                show.putExtra("postid" , returned.get(position).toString());
                startActivity(show);
            }
        });
        IntentFilter intentFilter = new IntentFilter(CustomBroadcast.ACTION_RESP);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        br = new CustomBroadcast();
        getActivity().registerReceiver(br, intentFilter);
        Intent send = new Intent(getActivity().getApplicationContext(), NotificationService.class);
        send.putExtras(bundle);

        getActivity().startService(send);


        return view;
    }
    public class CustomBroadcast extends BroadcastReceiver {

        public static final String ACTION_RESP =
                "team.fcisquare.intent.action.MESSAGE_PROCESSED";
        public CustomBroadcast() {

        }
        @Override
        public void onReceive(Context context, Intent intent) {

            returned = new ArrayList<team.fcisquare.Notification>();
            returned =(ArrayList<team.fcisquare.Notification>)intent.getSerializableExtra("list");
            for(int i=0 ; i<returned.size() ; i++){
                desc.add(returned.get(i).getDescription());
            }

            notificationList.notifyDataSetChanged();
        }
        public void onPause(){
           // getActivity().unrgisterReciever(br);
        }

    }
    public void onDestroyView(){
        super.onDestroyView();
       // unregisterReciever(br);

    }



}
