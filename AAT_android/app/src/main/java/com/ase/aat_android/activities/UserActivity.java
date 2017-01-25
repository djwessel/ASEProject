package com.ase.aat_android.activities;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.aat.datastore.Student;
import com.ase.aat_android.data.SessionData;
import com.ase.aat_android.R;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;

import com.ase.aat_android.util.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class UserActivity extends AppCompatActivity {

    private class RetrieveUserTask extends BaseAsyncTask<Long, Boolean, com.aat.datastore.User> {

        public RetrieveUserTask(Activity activity) {
            super(activity);
        }

        @Override
        protected com.aat.datastore.User doInBackground(Long... params) {
            ClientResource retrieveRes = new ClientResource(Method.GET, buildUrl(params[0]));
            retrieveRes.setResponseEntityBuffering(true);
            retrieveRes.setRequestEntityBuffering(true);
            //retrieveRes.setAttribute(Constants.userIdAttribute, params[0]);
            retrieveRes.accept(MediaType.APPLICATION_ALL_JSON);

            ObjectMapper mapper = new ObjectMapper();
            Student user = null;
            try {
                retrieveRes.getRequest().getCookies().add(0, SessionData.getSessionToken());
                user = mapper.readValue(retrieveRes.get().getText(), new TypeReference<Student>(){});
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ResourceException e) {
                e.printStackTrace();
            }
            return user;
        }

        @Override
        protected void onPostExecute(com.aat.datastore.User user) {
            super.onPostExecute(user);
            if (user == null) {
                throw new RuntimeException("Error while retrieving user data");
            }
            SessionData.updateUser(user);
            updateUserInfo();
        }

        private String buildUrl(Long attribute) {
            StringBuilder builder = new StringBuilder(Constants.AATUrl);
            builder.append(Constants.userResourceEndpoint);
            builder.append("/");
            builder.append(attribute);
            return builder.toString();
        }
    }

    private class LogoutTask extends BaseAsyncTask<Long, Boolean, Boolean> {

        public LogoutTask(Activity activity) {
            super(activity, Constants.logoutLoad);
        }
        @Override
        protected Boolean doInBackground(Long... params) {
            // TODO: This part asks to be in a base class
            ClientResource logoutRes = new ClientResource(Method.DELETE, buildUrl(params[0]));
            logoutRes.setResponseEntityBuffering(true);
            logoutRes.setRequestEntityBuffering(true);
            logoutRes.accept(MediaType.APPLICATION_ALL_JSON);
            String result = null;
            try {
                logoutRes.getRequest().getCookies().add(0, SessionData.getSessionToken());
                result = logoutRes.delete().getText();
                SessionData.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return (result != null);
        }

        @Override
        protected void onPostExecute(Boolean res) {
            super.onPostExecute(res);
            if (!res) {
                throw new RuntimeException("Error while retrieving user data");
            }
            openSigninActivity();
        }

        private String buildUrl(Long attribute) {
            StringBuilder builder = new StringBuilder(Constants.AATUrl);
            builder.append(Constants.userResourceEndpoint);
            builder.append("/");
            builder.append(attribute);
            builder.append("/logout");
            return builder.toString();
        }
    }

    private TextView firstnameTextView;
    private TextView lastNameTextView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        retrieveUser();
        initializeComponents();

        // TODO: add list activities part
        // TODO: add menu items: edit profile, courses, logout

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_item:
                // TODO: open profile activity
                return true;
            case R.id.courses_item:
                openCoursesActivity();
                return true;
            case R.id.logout_item:
                new LogoutTask(this).execute(SessionData.getUser().getId());
                //TODO: logout, open signin activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void retrieveUser() {
        Bundle extras = this.getIntent().getExtras();
        if (extras == null) {
            throw new RuntimeException("Error while retrieving user data");
        }
        Long id = extras.getLong(Constants.userIdKey);
        new RetrieveUserTask(this).execute(id);
    }

    private void initializeComponents() {
        firstnameTextView = (TextView) findViewById(R.id.firstname_textview);
        lastNameTextView = (TextView) findViewById(R.id.lastname_textview);

        com.aat.datastore.User user = SessionData.getUser();
        if (user != null) {
            firstnameTextView.setText(user.getFirstName());
            lastNameTextView.setText(user.getLastName());
        }
    }

    private void updateUserInfo() {
        com.aat.datastore.User user = SessionData.getUser();
        if (firstnameTextView != null) {
            firstnameTextView.setText(user.getFirstName());
        }
        if (lastNameTextView != null) {
            lastNameTextView.setText(user.getLastName());
        }
    }

    private void openSigninActivity() {
        Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
    }

    private void openCoursesActivity() {
        Intent intent = new Intent(this, CoursesActivity.class);
        startActivity(intent);
    }

}
