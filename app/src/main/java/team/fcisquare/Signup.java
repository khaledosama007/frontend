package team.fcisquare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

public class Signup extends AppCompatActivity {

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
            //send user.email.pass to sign up service
        }
    }

    public void onClickLogin(View view) {
        startActivity(new Intent(this, Login.class));
    }
}
