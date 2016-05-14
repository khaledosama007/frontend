package team.fcisquare;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lenovo on 26/04/2016.
 */
public class ShowPost extends Activity {
    private String postBody;
    private Integer postID;
    private JSONArray array;
    private JSONObject object , temp , jsonuser ,jsoncomment;
    private HashMap<String , String> params;
    private Place place = new Place();
    private ArrayList<Integer> likelist = new ArrayList<Integer>();
    private ArrayList<String> comments = new ArrayList<String>();
    private User user  = new User();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_post);
        postID = Integer.parseInt(getIntent().getStringExtra("postid"));

        params = new HashMap<String , String>();
        params.put("id", postID.toString());
        Connection con = new PostConnection(params, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                try {
                    object = new JSONObject(result);
                    postBody = object.getString("postbody");
                    temp = (JSONObject) object.get("place");
                        place.setName(temp.getString("name"));
                        place.setDescription(temp.getString("description"));
                        place.setLat(temp.getDouble("lat"));
                        place.setLon(temp.getDouble("long"));
                    likelist=(ArrayList<Integer>) object.get("likes");
                    array = (JSONArray) object.get("comment");

                    for(int i = 0 ; i < array.length() ; i++){
                        temp = (JSONObject) array.getJSONObject(i);
                        jsonuser = (JSONObject) temp.get("owner");
                            user.setId(jsonuser.getInt("id"));
                            user.setName(jsonuser.getString("name"));
                            user.setEmail(jsonuser.getString("email"));

                        comments.add(temp.getString("commentbody"));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });
        con.execute(URIs.SHOW_POST);

        setValues();
    }

    private void setValues() {
        TextView name = (TextView)findViewById(R.id.user_name_post);
        name.setText(user.getName());
        TextView placename=(TextView)findViewById(R.id.place_post);
        placename.setText(place.getName());
        TextView des = (TextView)findViewById(R.id.show_desc);
        des.setText(place.getDescription());
        TextView showLike =(TextView) findViewById(R.id.show_post_likes);
        int count = likelist.size();
        showLike.setText(count + "Likes");
        ListView comm =(ListView)findViewById(R.id.listView_show_post);
        comm.setAdapter((ListAdapter) comments);
    }
}
