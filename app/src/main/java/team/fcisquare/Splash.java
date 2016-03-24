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

/**
 * @author Andrew Albert
 * @version 1.0
 * @since 22/3/2016
 */
public class Splash extends Activity {
    private ImageView splashImage;
    private Intent intent;

    /**
     * this class is responsible for handling splash screen, and handling splash layout animation
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

                String userName = preferences.getString("name","");
                if(userName.equals("")){ // user not signed in, take him to login/sign up form
                    //// TODO: 3/22/2016 add intent for signing in, this is just for testing 
            //        intent = new Intent(getBaseContext(),MainActivity.class);
                }else { // user is signed in, take him to his account
                    //// TODO: 3/22/2016 add intent for user account 
                }
                intent = new Intent(getBaseContext(), UserProfile.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
