package team.fcisquare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
        Connection con = new PostConnection(params, new ConnectionListener() {
            @Override
            public void getResult(String result) {
            try{
                JSONObject json = new JSONObject(result);
                //WTF ?
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        });

    }
    public void onClickSignUp(View view) {
        startActivity(new Intent(this, Signup.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
