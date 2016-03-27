package team.fcisquare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    private HashMap<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void onClickLogin(View view) {
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
                    Intent intent = new Intent(getBaseContext(), UserProfile.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(Login.this, "Death Wish", Toast.LENGTH_SHORT).show();
                }
            }
        });
        postCon.execute(URIs.POST_LOGIN);

    }
    public void onClickSignUp(View view) {
        startActivity(new Intent(this, Signup.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
