package me.vivh.socialtravelapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etUsername) EditText etUsername;

    @BindView(R.id.etPassword) EditText etPassword;

    @BindView(R.id.btnLogin) Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser != null){
            addUsernameToParseInstallation(currentUser.getUsername());
            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                login(username,password);
            }
        });


    }

    private void login(final String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){

                    addUsernameToParseInstallation(username);

                    Log.d("LoginActivity", "Login successful.");
                    Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_LONG).show();
                    final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.d("LoginActivity", "Login failure.");
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addUsernameToParseInstallation(String username){
        ParseInstallation.getCurrentInstallation().getInstallationId();
        ParseInstallation.getCurrentInstallation().put("username", username);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
