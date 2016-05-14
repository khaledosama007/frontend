package team.fcisquare;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

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
                        JSONArray likesIds = (JSONArray) object.get("likes");

                        for(int j = 0;j < likesIds.length();j++){
                            int id = likesIds.getInt(j);
                            if(user.getId().intValue() == id){
                                p.setLikedByUser(true);
                                break;
                            }
                        }
                        p.setLikes(likesIds.length());
                        Log.e("test", p.getLikes() + "");
                        p.setDate(Long.parseLong(object.getString("postdate")));
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
                            JSONObject tempObj = commentsArray.getJSONObject(j);
                            team.fcisquare.Comment comment = new team.fcisquare.Comment();
                            comment.setId(Integer.parseInt(tempObj.getString("commentid")));
                            comment.setBody(tempObj.getString("commentbody"));
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
        Log.e("test", posts.size() + "efsooh iwdufhjeifhjierhgiuerhguiws");
    }

    class MyExpandableAdapter extends BaseExpandableListAdapter{
        private Context context;
        private ArrayList<Post> posts;
        private final User innerUser;

        public MyExpandableAdapter(Context context, ArrayList<Post> posts, User innerUser){
            this.context = context;
            this.posts = posts;
            this.innerUser = user;
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
            setParentViewValues(groupPosition, convertView);
            final TextView like = (TextView) convertView.findViewById(R.id.place_likes_check_in);
            final Button likeButton = (Button) convertView.findViewById(R.id.like_check_in);
            if(posts.get(groupPosition).isLikedByUser())
                setLiked(likeButton);
            else{
                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        posts.get(groupPosition).setLikedByUser(true);
                        HashMap<String, String> parameters = new HashMap<String, String>();
                        parameters.put("userid", innerUser.getId().toString());
                        parameters.put("postid", String.valueOf(posts.get(groupPosition).getId()));
                        Connection conn = new PostConnection(parameters, new ConnectionListener() {
                            @Override
                            public void getResult(String result) {

                            }
                        });
                        conn.execute(URIs.POST_LIKE);
                        setLiked(likeButton);
                        like.setText(posts.get(groupPosition).getLikes() + 1 + " Likes");
                    }
                });
            }
            return convertView;
        }

        private void setParentViewValues(int groupPosition, View convertView) {
            //    TextView user = (TextView)convertView.findViewById(R.id.person_check_name);
            TextView place = (TextView)convertView.findViewById(R.id.place_check_in);
            TextView like = (TextView)convertView.findViewById(R.id.place_likes_check_in);
            TextView userComment = (TextView)convertView.findViewById(R.id.user_comment_check_in_header);
            TextView date = (TextView) convertView.findViewById(R.id.post_date);
            Date postDate = posts.get(groupPosition).getDate();
            date.setText(getDate(postDate));
            like.setText(posts.get(groupPosition).getLikes() + " Likes");

            // user.setText(userNames.get(groupPosition));
            place.setText(posts.get(groupPosition).getPlace().getName());
            like.setText(posts.get(groupPosition).getLikes() + " likes");
            userComment.setText(posts.get(groupPosition).getBody());
        }

        @NonNull
        private String getDate(Date postDate) {
            return String.valueOf(postDate.getMinutes()) + "/" + String.valueOf(postDate.getHours());
        }

        private void setLiked(Button likeButton) {
            likeButton.setEnabled(false);
            likeButton.setText("Liked");
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
                            Comment tempComment = new Comment();
                            tempComment.setId(user.getId());
                            tempComment.setBody(editText.getText().toString());
                            comments.add(tempComment);
                            editText.setText("");
                            updateView();

                            HashMap<String, String> parameters = new HashMap<String, String>();
                            parameters.put("userid", innerUser.getId().toString());
                            parameters.put("postid", String.valueOf(posts.get(groupPosition).getId()));
                            parameters.put("body", tempComment.getBody());
                            Connection conn = new PostConnection(parameters, new ConnectionListener() {
                                @Override
                                public void getResult(String result) {

                                }
                            });
                            conn.execute(URIs.POST_COMMENT);
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
        expandableAdapter = new MyExpandableAdapter(getActivity(), posts, user);
        expandableListView = (ExpandableListView)view.findViewById(R.id.home_expandable_list);
        expandableListView.setAdapter(expandableAdapter);
    }

}
