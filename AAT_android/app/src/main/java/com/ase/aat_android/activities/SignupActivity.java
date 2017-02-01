package com.ase.aat_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.ase.aat_android.data.SessionData;
import com.ase.aat_android.util.Constants;
import com.ase.aat_android.util.EndpointsURL;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    private class SignupTask extends BaseAsyncTask<String, Boolean, Long> {

        public SignupTask(Activity activity) {
            super(activity, Constants.signupLoad);
        }

        @Override
        protected Long doInBackground(String... params) {
            ClientResource signupRes = new ClientResource(Method.POST, EndpointsURL.HTTP_ADDRESS + EndpointsURL.SIGNUP);
            signupRes.setRequestEntityBuffering(true);
            signupRes.setResponseEntityBuffering(true);
            Form signupForm = createSignupForm(params[0], params[1], params[2], params[3]);
            System.out.println(signupRes.toString());
            System.out.println(signupForm.toString());
            Long userID;
            try {
                ObjectMapper mapper = new ObjectMapper();
                userID = mapper.convertValue(signupRes.post(signupForm, MediaType.ALL).getText(), Long.class);
                SessionData.updateSessionToken(signupRes.getResponse().getCookieSettings().getFirst("sessionToken"));
                System.out.println(userID);
            } catch (ResourceException e) {
                System.out.println(e.getMessage());
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return userID;
        }

        @Override
        protected void onPostExecute(Long res) {
            super.onPostExecute(res);
            if (res == null) {
                Toast.makeText(getApplicationContext(), "Failed to signup", Toast.LENGTH_LONG).show();
            } else {
                openUsetActivity(res);
            }

        }

        private Form createSignupForm(String firstName, String lastName, String email, String password) {
            Form form = new Form();
            form.add(new Parameter(Constants.userTypePramName, Constants.userType));
            form.add(new Parameter(Constants.emailParamName, email));
            form.add(new Parameter(Constants.passwordParamName, password));
            form.add(new Parameter(Constants.firstnameParamName, firstName));
            form.add(new Parameter(Constants.lastnameParamName, lastName));
            return form;
        }
    }

    private EditText firstnameEditText;
    private EditText lastnameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button   signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        initializeComponents();
        setupListeners();

    }

    private void initializeComponents() {
        firstnameEditText = (EditText) findViewById(R.id.firstname_edittext);
        lastnameEditText = (EditText) findViewById(R.id.lastname_edittext);
        emailEditText = (EditText) findViewById(R.id.email_edittext);
        passwordEditText = (EditText) findViewById(R.id.password_edittext);
        signupButton = (Button) findViewById(R.id.signup_button);
    }

    private void setupListeners() {
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkSignupFields()) {
                    return;
                }
                new SignupTask(SignupActivity.this).execute(firstnameEditText.getText().toString(),
                                                            lastnameEditText.getText().toString(),
                                                            emailEditText.getText().toString(),
                                                            passwordEditText.getText().toString());
            }
        });
    }

    private boolean checkSignupFields() {
        boolean requiredFieldsFilled = true;
        if (firstnameEditText.getText().toString().isEmpty()) {
            firstnameEditText.setHighlightColor(Color.RED);
            requiredFieldsFilled = false;
        }
        if (lastnameEditText.getText().toString().isEmpty()) {
            lastnameEditText.setHighlightColor(Color.RED);
            requiredFieldsFilled = false;
        }
        if (emailEditText.getText().toString().isEmpty()) {
            emailEditText.setHighlightColor(Color.RED);
            requiredFieldsFilled = false;
        }
        if (passwordEditText.getText().toString().isEmpty()) {
            passwordEditText.setHighlightColor(Color.RED);
            requiredFieldsFilled = false;
        }
        return requiredFieldsFilled;
    }

    private void openUsetActivity(Long userID) {
        Intent intent = new Intent(SignupActivity.this, UserActivity.class);
        // TODO: set actual id
        intent.putExtra(Constants.userIdKey, userID);
        startActivity(intent);
    }

}
