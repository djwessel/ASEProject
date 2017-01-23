package com.ase.aat_android.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aat.datastore.ILongResource;
import com.aat.datastore.User;
import com.ase.aat_android.R;

import org.restlet.Response;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.ase.aat_android.utils.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SigninActivity extends AppCompatActivity {
    private class SigninTask extends BaseAsyncTask<String, Boolean, Long> {
        public  SigninTask(Activity activity) throws URISyntaxException {
            super(activity, Constants.loginLoad);
        }

        @Override
        protected Long doInBackground(String... params) {
            ClientResource loginRes = new ClientResource(Method.POST, Constants.AATUrl + Constants.loginResourceEndpoint);
            loginRes.setRequestEntityBuffering(true);
            loginRes.setResponseEntityBuffering(true);
            Form loginForm = createLoginForm(params[0], params[1]);
            Long result;
            try {
                ObjectMapper mapper = new ObjectMapper();
                result = mapper.convertValue(loginRes.post(loginForm, MediaType.ALL).getText(), Long.class);
                System.out.println(result);
            } catch (ResourceException e) {
                System.out.println(e.getMessage());
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return result;
        }

        private Form createLoginForm(String username, String password) {
            Form form = new Form();
            form.add(new Parameter(Constants.emailParamName, username));
            form.add(new Parameter(Constants.passwordParamName, password));
            return form;
        }

        @Override
        protected void onPostExecute(Long res) {
            super.onPostExecute(res);
            if (res != null) {
                openUserActivity(res);
            } else {
                Toast.makeText(getApplicationContext(), "Failed to login", Toast.LENGTH_LONG).show();
            }
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
                if (!checkSigninFields()) {
                    return;
                }
                try {
                    new SigninTask(SigninActivity.this).execute(usernameEditText.getText().toString(),
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
        // TODO: set actual id
        intent.putExtra(Constants.userIdKey, userID);
        startActivity(intent);
    }



}
