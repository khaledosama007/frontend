package team.fcisquare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Andrew on 4/17/2016.
 */
public class MessagesFragment extends Fragment {
    private View view;
    //Message String  , Sender Name , Read Or not ,to be extracted from objects retrieved
    //from DB
    String[] Messages = {"Message Test"}; // To be replaced by the list of messages
    String[] Senders = {"Sender Name"};
    boolean [] readness = new boolean[1]; // If the message is read -> 1 , else -> 0

    public MessagesFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        readness[0] = true;
        view = inflater.inflate(R.layout.message_fragment, container, false);
        ListView li = (ListView) view.findViewById(R.id.mlist);
        li.setAdapter(new MessageList(getActivity() , Messages , readness , Senders));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // MessageList adapter = new MessageList(MessagesFragment. , test);

    }
}
