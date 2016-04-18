package team.fcisquare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * This class is responsible for handling splash screen, and handling splash layout animation
 *
 * @author Andrew Albert
 * @version 1.0
 * @since 22/3/2016
 */
public class Splash extends Activity {
    private ImageView splashImage;
    private Intent intent;

    /**
     * Just the instructor
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //used to disable taskbar in layout
        setContentView(R.layout.splash);

        splashImage = (ImageView)findViewById(R.id.splash_image);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_rotate);
        splashImage.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPreferences preferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
              //  SharedPreferences.Editor editor = preferences.edit();

                String eMail = preferences.getString("email","");
                if(eMail.equals("")){ // user not signed in, take him to login/sign up form
                    intent = new Intent(getBaseContext(), Signup.class);
                    startActivity(intent);
                    finish();
                }else { // user is signed in, take him to his account
                    //attempting to read user data
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", eMail);
                    params.put("pass", preferences.getString("pass", ""));

                    Connection connection = new PostConnection(params, new ConnectionListener() {
                        @Override
                        public void getResult(String result) {
                            try {
                                JSONObject json = new JSONObject(result);
                                User user = new User();
                                user.setEmail(json.getString("email"));
                                user.setId(json.getInt("id"));
                                user.setLat(json.getDouble("lat"));
                                user.setLon(json.getDouble("long"));
                                user.setPass(json.getString("pass"));
                                user.setName(json.getString("name"));
                                intent = new Intent(getBaseContext(), UserProfile.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("user", user);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    connection.execute(URIs.POST_LOGIN);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
