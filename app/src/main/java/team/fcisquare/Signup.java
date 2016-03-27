package team.fcisquare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

public class Signup extends AppCompatActivity {
    private HashMap<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }


    public void onClickSignUp(View view) {
        EditText editText = (EditText) findViewById(R.id.name);
        String name = editText.getText().toString();
        Boolean error = false;
        if (name.isEmpty()) {
            editText.setError("Your name is required.");
            error = true;
        }

        else if (!name.matches("^[a-zA-Z]+$")){
            editText.setError("Invaild name.");
            error = true;
        }


        editText = (EditText)findViewById(R.id.email);
        String email = editText.getText().toString();
        if(email.isEmpty()){
            editText.setError("Your email is required.");
            error = true;
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editText.setError("Invaild email.");
            error = true;
        }

        editText = (EditText)findViewById(R.id.pass);
        String pass = editText.getText().toString();
        if(pass.isEmpty() || pass.length() < 8){
            editText.setError("Password must be at least 8 digits");
            error = true;
        }

        if(!error) {
            params = new HashMap<String, String>();
            params.put("name", name);
            params.put("email", email); //Note: The backend side should check if a another user registered with the same email
            params.put("pass", pass);
            Connection postCon = new PostConnection(params, new ConnectionListener() {
                @Override
                public void getResult(String result) {
                    try {
                        Intent intent = new Intent(Signup.this, UserProfile.class);
                        intent.putExtra("SuckMyJSon", result);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(Signup.this, "Death Wish", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            postCon.execute(URIs.POST_SIGN_UP);
        }
    }

    public void onClickLogin(View view) {
        startActivity(new Intent(this, Login.class));
    }
}
