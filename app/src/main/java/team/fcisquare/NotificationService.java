package team.fcisquare;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by lenovo on 25/04/2016.
 */
public class NotificationService extends Service {
    private Notification notification;
    private HashMap<String , String> params;
    private JSONObject json;
    private JSONArray array;
    private Bundle bundle;
    private User user;
    ArrayList<Notification> notifications = new ArrayList<Notification>();

    public NotificationService() {
        /*super("Notification Service");*/
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent innerIntent = new Intent();
        //in.putExtra("test", "newNoti");
        innerIntent.setAction(NotificationFragment.CustomBroadcast.ACTION_RESP);
        innerIntent.addCategory(Intent.CATEGORY_DEFAULT);
        bundle = new Bundle(innerIntent.getExtras());

        user = new User();
        user = (User) bundle.getSerializable("user");
        params = new HashMap<String, String>();
        params.put("userid" , user.getId().toString());
        json = new JSONObject();
        Connection con = new PostConnection(params, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                try {
                    array = new JSONArray(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0 ;i < array.length();i++){
                    try {
                        json = array.getJSONObject(i);

                        notification = new Notification();
                        notification.setId(Integer.parseInt(json.getString("id")));
                        notification.setDescription(json.getString("description"));
                        notification.setType(Integer.parseInt(json.getString("type")));
                        String parsedate = json.getString("date");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date nDate = sdf.parse(parsedate);
                        notification.setDate(nDate);
                        notification.setTarget(Integer.parseInt(json.getString("target")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    notifications.add(notification);

                }
                //doInBackground();

            }
        });
        con.execute(URIs.NOTIFICATION);
        //parameters from excuting get notification
        innerIntent.putExtra("list" , notifications);
        sendBroadcast(innerIntent);

        //Toast.makeText(this, "On Handle And Broad", Toast.LENGTH_LONG).show();
        //Looper.loop();
        return START_STICKY;
    }
   /* public void onPause(){
        super.onPause();
    }*/
    public void onDestroy(){
        super.onDestroy();
    }

}
