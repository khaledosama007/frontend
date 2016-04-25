package team.fcisquare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.HashMap;


public class Login extends AppCompatActivity {
    private HashMap<String, String> params;
    private RelativeLayout relativeLayout;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        relativeLayout = (RelativeLayout)findViewById(R.id.login_relative_layout);
    }

    public void onClickLogin(View view) {
        if(!isNetworkAvailable()){
            Snackbar snackbar = Snackbar.make(relativeLayout, "Please check your internet connection", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        EditText editText = (EditText)findViewById(R.id.loginBox);
        String username = editText.getText().toString();
        editText = (EditText)findViewById(R.id.passBox);
        String password = editText.getText().toString();
        params = new HashMap<String, String>();
        params.put("email", username);
        params.put("pass", password);
        //// TODO: 3/26/2016 add a part here 2 check if username and password are available or not !
        Connection postCon = new PostConnection(params, new ConnectionListener() {
            @Override
            public void getResult(String result) {
                try {
                 /*   Intent intent = new Intent(Login.this, UserProfile.class);
                    intent.putExtra("SuckMyJSon", result);
                    startActivity(intent);*/
                    // added by andrew
                    JSONObject json = new JSONObject(result);
                    User user = new User();
                    user.setEmail(json.getString("email"));
                    user.setId(json.getInt("id"));
                    user.setLat(json.getDouble("lat"));
                    user.setLon(json.getDouble("long"));
                    user.setPass(json.getString("pass"));
                    user.setName(json.getString("name"));
                    preferences = getSharedPreferences("userDetails", MODE_PRIVATE);
                    editor = preferences.edit();
                    editor.putString("email", user.getEmail());
                    editor.putString("pass", user.getPass());
                    editor.commit();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Toast.makeText(Login.this, "Death Wish", Toast.LENGTH_SHORT).show();
                }
            }
        });
        postCon.execute(URIs.POST_LOGIN);

    }
    public void onClickSignUp(View view) {
        startActivity(new Intent(this, Signup.class));
        finish();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
