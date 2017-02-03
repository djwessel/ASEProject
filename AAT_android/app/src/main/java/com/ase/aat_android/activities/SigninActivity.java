package com.ase.aat_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ase.aat_android.R;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import com.ase.aat_android.data.SessionData;
import com.ase.aat_android.util.Constants;
import com.ase.aat_android.util.EndpointsURL;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SigninActivity extends AppCompatActivity {

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

    private void setLoginButtonListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkSigninFields()) {
                    return;
                }
                try {
                    final SigninTask task = new SigninTask(SigninActivity.this);
                    task.setCallback(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                openUserActivity(task.get());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    task.execute(usernameEditText.getText().toString(),
                                 passwordEditText.getText().toString());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean checkSigninFields() {
        if (usernameEditText.getText().toString().isEmpty()) {
            usernameEditText.setHighlightColor(Color.RED);
            return false;
        } else if (passwordEditText.getText().toString().isEmpty()) {
            passwordEditText.setHighlightColor(Color.RED);
            return false;
        }
        return true;
    }

    private void setSignupButtonListener() {
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openUserActivity(Long userID) {
        Intent intent = new Intent(SigninActivity.this, UserActivity.class);
        intent.putExtra(Constants.userIdKey, userID);
        startActivity(intent);
    }



}
