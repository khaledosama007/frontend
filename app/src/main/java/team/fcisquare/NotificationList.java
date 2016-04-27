package team.fcisquare;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lenovo on 25/04/2016.
 */
public class NotificationList extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> description;

    public NotificationList(Activity context , ArrayList<String> desc){
        super(context , R.layout.notification_list , desc);
        this.context=context;
        description=desc;

    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=((Activity)getContext()).getLayoutInflater().inflate(R.layout.notification_list,null);
        TextView r = (TextView)v.findViewById(R.id.ndesc);
        r.setText(description.get(position));

        return v;
    }

    public void add(String newNotification){
        description.add(newNotification);
    }

}
