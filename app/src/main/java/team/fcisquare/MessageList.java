package team.fcisquare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lenovo on 18/04/2016.
 */
public class MessageList extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] test;
    private final boolean [] read;
    private final String[] Senders;
    public MessageList(Activity context,
                       String[] test , boolean [] read , String[] Senders) {
        super(context, R.layout.message_list, test);
        this.context = context;
        this.test = test;
        this.read = read;
        this.Senders = Senders;
    }

    @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v=((Activity)getContext()).getLayoutInflater().inflate(R.layout.message_list,null);
           if(read[position] == true){
               v.setBackgroundResource(R.color.textColorPrimary);
           }
            else
            v.setBackgroundResource(R.color.MyBlue);

            TextView txt1 = (TextView) v.findViewById(R.id.messagein);
            txt1.setText(test[position]);
            TextView tv = (TextView) v.findViewById(R.id.messagesender);
            tv.setText(Senders[position]);
            return v;
    }
}
