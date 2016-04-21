package team.fcisquare;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Andrew on 4/16/2016.
 */
public class MainFragment extends Fragment {
    private View view;
    private ExpandableListView expandableListView;
    private BaseExpandableListAdapter expandableAdapter;
    private ArrayList<Integer> likes;
    private ArrayList<String> userNames;
    private ArrayList<String> places;
    private HashMap<String, ArrayList<Comment>> comments; //to be altered
    private Button commentButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        likes = new ArrayList<>();
        userNames = new ArrayList<>();
        places = new ArrayList<>();
        comments = new HashMap<>();
        likes.add(15);
        userNames.add("andrew");
        places.add("el haram");
        ArrayList<Comment> tmp = new ArrayList<>();
        tmp.add(new Comment("dodo", "hiiiii"));
        tmp.add(new Comment("mina", "shit"));
        comments.put("andrew", tmp );
        expandableListView = (ExpandableListView)view.findViewById(R.id.home_expandable_list);
        expandableListView.setAdapter(new MyExpandableAdapter(getActivity(), userNames, comments, likes, places));

        commentButton = (Button)view.findViewById(R.id.comment_check_in);
    }

    class Comment{
        public String comment;
        public String userName;
        public Comment(String userName, String comment) {
            this.userName = userName;
            this.comment = comment;
        }
    }

    class MyExpandableAdapter extends BaseExpandableListAdapter{
        private Context context;
        private ArrayList<String> userNames;
        HashMap<String, ArrayList<Comment>> comments;
        ArrayList<Integer> likes;
        ArrayList<String> places;

        public MyExpandableAdapter(Context context, ArrayList<String> userNames, HashMap<String, ArrayList<Comment>> comments, ArrayList<Integer> likes, ArrayList<String> places){
            this.context = context;
            this.userNames = userNames;
            this.comments = comments;
            this.likes = likes;
            this.places = places;
        }
        @Override
        public int getGroupCount() {
            return userNames.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return comments.get(userNames.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return 1; ///tmp
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return comments.get(userNames.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true; ////////
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.check_in_header, parent, false);
            }
            TextView user = (TextView)convertView.findViewById(R.id.person_check_name);
            TextView place = (TextView)convertView.findViewById(R.id.place_check_in);
            final TextView like = (TextView)convertView.findViewById(R.id.place_likes_check_in);
            Button commentButton = (Button)convertView.findViewById(R.id.comment_check_in);

            user.setText(userNames.get(groupPosition));
            place.setText(places.get(groupPosition));
            like.setText(likes.get(groupPosition) + " likes");

            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Comment> c = comments.get(userNames.get(groupPosition));
                    c.add(new Comment("ahmed", "noooo"));
                    c.add(new Comment("ahmed", "fffff"));

                }
            });
           // View vv = convertView.findViewById(R.id.check_in_header_id);
          //  vv.setBackgroundColor(R.color.colorPrimary);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.check_in_item, parent, false);
            }
            final Comment c = comments.get(userNames.get(groupPosition)).get(childPosition);
            TextView user = (TextView)convertView.findViewById(R.id.user_name_check_in_item);
            TextView comm = (TextView)convertView.findViewById(R.id.comment_check_in_item);
            comm.setText(c.comment);
            user.setText(c.userName);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
