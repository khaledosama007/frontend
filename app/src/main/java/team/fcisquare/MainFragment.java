package team.fcisquare;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.view.View.OnKeyListener;
import android.widget.Toast;
import java.util.ArrayList;
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
    private ArrayList<String> checkInComments;

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
        checkInComments = new ArrayList<>();
        checkInComments.add("nice place :D");
        likes.add(15);
        userNames.add("andrew");
        places.add("el haram");
        ArrayList<Comment> tmp = new ArrayList<>();
        tmp.add(new Comment("dodo", "hiiiii"));
        tmp.add(new Comment("mina", "shit"));
        comments.put("andrew", tmp );

        checkInComments.add("hurray");
        likes.add(12);
        userNames.add("khaled");
        places.add("el maadi");
        ArrayList<Comment> tmp2 = new ArrayList<>();
        tmp2.add(new Comment("ds", "hiiiii"));
        tmp2.add(new Comment("erre", "shit"));
        comments.put("khaled", tmp2 );

        checkInComments.add("Maaaaaa2");
        likes.add(1);
        userNames.add("unknown");
        places.add("el haram");
        ArrayList<Comment> tmp3 = new ArrayList<>();
        tmp3.add(new Comment("dfdo", "hiiiii"));
        tmp3.add(new Comment("d", "shit"));
        comments.put("unknown", tmp3 );

        expandableListView = (ExpandableListView)view.findViewById(R.id.home_expandable_list);
        expandableListView.setAdapter(new MyExpandableAdapter(getActivity(), userNames, comments, likes, places, checkInComments));


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

        public MyExpandableAdapter(Context context, ArrayList<String> userNames, HashMap<String, ArrayList<Comment>> comments, ArrayList<Integer> likes, ArrayList<String> places, ArrayList<String> checkInComments){
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
            return comments.get(userNames.get(groupPosition)).size() + 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return 1; ///tmp
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if(childPosition == 0){
                return "";
            }
            return comments.get(userNames.get(groupPosition)).get(childPosition - 1);
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
        public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.check_in_header, parent, false);
            }
            final View tmoConvertView = convertView;
            TextView user = (TextView)convertView.findViewById(R.id.person_check_name);
            TextView place = (TextView)convertView.findViewById(R.id.place_check_in);
            TextView like = (TextView)convertView.findViewById(R.id.place_likes_check_in);
            TextView userComment = (TextView)convertView.findViewById(R.id.user_comment_check_in_header);

            user.setText(userNames.get(groupPosition));
            place.setText(places.get(groupPosition));
            like.setText(likes.get(groupPosition) + " likes");
            userComment.setText(checkInComments.get(groupPosition));


           // View vv = convertView.findViewById(R.id.check_in_header_id);
          //  vv.setBackgroundColor(R.color.colorPrimary);
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        //    if(convertView == null){
                convertView = (childPosition == 0 ? getActivity().getLayoutInflater().inflate(R.layout.add_comment, parent, false) :getActivity().getLayoutInflater().inflate(R.layout.check_in_item, parent, false));
       //     }
            if(childPosition != 0){
                final Comment c = comments.get(userNames.get(groupPosition)).get(childPosition -1);
                TextView user = (TextView)convertView.findViewById(R.id.user_name_check_in_item);
                TextView comm = (TextView)convertView.findViewById(R.id.comment_check_in_item);
                comm.setText(c.comment);
                user.setText(c.userName);
            }else{
                final EditText editText = (EditText)convertView.findViewById(R.id.add_comment_edittext);
                editText.setFocusableInTouchMode(true);
           /*     editText.setOnKeyListener(new OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if(event.getAction() == KeyEvent.ACTION_DOWN && EditorInfo.IME_ACTION_DONE){
                            Toast.makeText(getActivity(), "hjsdf", Toast.LENGTH_SHORT).show();
                            ArrayList<Comment> ccc = comments.get("andrew");
                            ccc.add(new Comment("4444","fffff"));
                            return true;
                        }
                        Toast.makeText(getActivity(), keyCode + "", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });*/
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if(actionId == EditorInfo.IME_ACTION_DONE) {
                            ArrayList<Comment> ccc = comments.get(userNames.get(groupPosition));
                            ccc.add(new Comment("ds",editText.getText().toString()));
                            editText.setText("");
                        }
                        return false;
                    }
                });
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
