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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew on 4/16/2016.
 */
public class MainFragment extends Fragment {
    private View view;
    private ExpandableListView expandableListView;
    private BaseExpandableListAdapter expandableAdapter;
    private Connection connection;
    HashMap<String, String> params;
    private User user;
    private ArrayList<Post> posts;

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

        posts = new ArrayList<>();
        user = (User) getActivity().getIntent().getSerializableExtra("user");
        params = new HashMap<>();
        params.put("id", user.getId().toString());
        connection = new GetConnection(params, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                try {
                    JSONArray mainArray = new JSONArray(result);
                    for(int i = 0;i < mainArray.length();i++){
                        JSONObject object = (JSONObject) mainArray.get(i);
                        Post p = new Post();
                        p.setBody(object.getString("postbody"));
                        p.setId(Integer.parseInt(object.getString("postid")));
                        ArrayList<Integer> likes = new ArrayList<>();
                     //   likes = (ArrayList<Integer>) object.getString("likes");
                     //   p.setLikes(likes.size());
                  //      p.setDate(Double.parseDouble(object.getString("postdate")));
                        JSONObject placeObject = object.getJSONObject("place");
                        Place place = new Place();
                        place.setDescription(placeObject.getString("descrition"));
                        place.setId(Integer.parseInt(placeObject.getString("placeid")));
                        place.setLat(Double.parseDouble(placeObject.getString("lat")));
                        place.setLon(Double.parseDouble(placeObject.getString("long")));
                        place.setName(placeObject.getString("placename"));
                        p.setPlace(place);
                        JSONArray commentsArray = object.getJSONArray("comments");
                        ArrayList<team.fcisquare.Comment> comments = new ArrayList<>();
                        for(int j = 0;j < commentsArray.length();j++){
                            JSONObject tempObj = commentsArray.getJSONObject(i);
                            team.fcisquare.Comment comment = new team.fcisquare.Comment();
                            comment.setId(Integer.parseInt(tempObj.getString("commentid")));
                            comment.setBody(tempObj.getString("commentbody"));
                 //           comment.setDate(Double.parseDouble(tempObj.getString("commentdate")));
                            comments.add(comment);
                        }
                        p.setComments(comments);
                        posts.add(p);
                    }
                    setAdapter();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        connection.execute(URIs.GET_SHOW_HOME);



    }

    class MyExpandableAdapter extends BaseExpandableListAdapter{
        private Context context;
        private ArrayList<Post> posts;

        public MyExpandableAdapter(Context context, ArrayList<Post> posts){
            this.context = context;
            this.posts = posts;
        }
        @Override
        public int getGroupCount() {
            return posts.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return posts.get(groupPosition).getComments().size() + 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return posts.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if(childPosition == 0){
                return "";
            }
            return posts.get(groupPosition).getComments().get(childPosition - 1);
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

           // user.setText(userNames.get(groupPosition));
            place.setText(posts.get(groupPosition).getPlace().getName());
      //      like.setText(posts.get(groupPosition).getLikes() + " likes");
            userComment.setText(posts.get(groupPosition).getBody());

            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
                convertView = (childPosition == 0 ? getActivity().getLayoutInflater().inflate(R.layout.add_comment, parent, false) :getActivity().getLayoutInflater().inflate(R.layout.check_in_item, parent, false));
            if(childPosition != 0){
                ArrayList<Comment> comments = posts.get(groupPosition).getComments();
                Comment c = comments.get(childPosition - 1);
                TextView user = (TextView)convertView.findViewById(R.id.user_name_check_in_item);
                TextView comm = (TextView)convertView.findViewById(R.id.comment_check_in_item);
                comm.setText(c.getBody());
                user.setText(c.getId() + "");
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
                            ArrayList<Comment> comments = posts.get(groupPosition).getComments();
                            Comment c = new Comment();
                            c.setId(user.getId());
                            c.setBody(editText.getText().toString());
                            
                            comments.add(c);
                            editText.setText("");
                            updateView();
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
    public void updateView(){
        expandableAdapter.notifyDataSetChanged();
        expandableListView.refreshDrawableState();
    }
    public void setAdapter(){
        expandableAdapter = new MyExpandableAdapter(getActivity(), posts);
        expandableListView = (ExpandableListView)view.findViewById(R.id.home_expandable_list);
        expandableListView.setAdapter(expandableAdapter);
    }
}
