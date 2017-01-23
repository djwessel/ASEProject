package com.aat.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.ase.aat_android.R;

import org.restlet.resource.ClientResource;

import java.net.URI;
import java.net.URISyntaxException;


public class SigninActivity extends AppCompatActivity {
    private class SigninTask extends AsyncTask<URI, Boolean, Boolean> {

        private ProgressBar loadingBar;

        public  SigninTask() throws URISyntaxException {
            initializeProgressBar();
        }

        @Override
        protected Boolean doInBackground(URI... params) {
            return sendRequest(params[0]);
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean succeed) {
            loadingBar.setVisibility(View.INVISIBLE);
            if (succeed) {
                //TODO: pass User as an argument
                openUserActivity();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to login", Toast.LENGTH_LONG).show();
            }
        }

        private void initializeProgressBar() {
            loadingBar = new ProgressBar(getApplicationContext());
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            loadingBar.setLayoutParams(params);
            loadingBar.setVisibility(View.INVISIBLE);
        }

        private Boolean sendRequest(URI uri) {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            //TODO: move hard coded strings to utility class
            ClientResource request = new ClientResource(uri);
            request.addQueryParameter("username", username);
            request.addQueryParameter("password", password);
            //TODO: should get user data and use it
            //return (request.get() != null);
            return false;
        }
    }

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        intializeComponents();
        setLoginButtonListener();
        setSignupButtonListener();
    }

    private void intializeComponents() {
        usernameEditText = (EditText) findViewById(R.id.login_username_edittext);
        passwordEditText = (EditText) findViewById(R.id.login_password_edittext);
        loginButton = (Button) findViewById(R.id.login_button);
        signupButton = (Button) findViewById(R.id.signup_button);
    }

    private void setLoginButtonListener(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new SigninTask().execute(new URI("http://ase2016-148507.appspot.com/rest/user"));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setSignupButtonListener() {
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: move to signup activity
            }
        });
    }

    // TODO: pass User as an argument to this function
    private void openUserActivity() {
        Intent intent = new Intent(this, AttendancesActivity.class);
        startActivity(intent);
    }



}
