package com.ase.aat_android.view;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.ase.aat_android.data.User;
import com.ase.aat_android.R;

import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.engine.adapter.HttpRequest;
import org.restlet.resource.ClientResource;

import com.ase.aat_android.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class UserActivity extends ExpandableListActivity {

    private class RetrieveUserTask extends BaseAsyncTask<Long, Boolean, com.aat.datastore.User> {

        public RetrieveUserTask(Activity activity) {
            super(activity);
        }

        @Override
        protected com.aat.datastore.User doInBackground(Long... params) {
            ClientResource retrieveRes = new ClientResource(Constants.AATUrl + Constants.userRetrieveResourceEndpoint);
            retrieveRes.setResponseEntityBuffering(true);
            //retrieveRes.setRequestEntityBuffering(true);
            retrieveRes.setAttribute(Constants.userIdAttribute, params[0].toString());

            System.out.println(params[0].toString());

            System.out.println(retrieveRes.toString());
            ObjectMapper mapper = new ObjectMapper();
            com.aat.datastore.User user = null;
            try {
                String str = retrieveRes.get().getText();
               // String resStr = retrieveRes.get(MediaType.ALL).getText();
               // user = mapper.convertValue(resStr, com.aat.datastore.User.class);
           } catch (IOException e) {
                e.printStackTrace();
            }
            /*com.aat.datastore.User user = retrieveRes.get(com.aat.datastore.User.class);*/
            return user;
        }

        @Override
        protected void onPostExecute(com.aat.datastore.User user) {
            super.onPostExecute(user);
            if (user == null) {
                throw new RuntimeException("Error while retrieving user data");
            }
            User.updateUser(user);
            updateUserInfo();
        }
    }

    private TextView firstnameTextView;
    private TextView lastNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        retrieveUser();
        initializeComponents();

        // TODO: add list view part
        // TODO: add menu items: edit profile, courses, logout
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                // TODO: open courses activity
                return true;
            case R.id.logout_item:
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

        com.aat.datastore.User user = User.getUser();
        if (user != null) {
            firstnameTextView.setText(user.getFirstName());
            lastNameTextView.setText(user.getLastName());
        }
    }

    private void updateUserInfo() {
        com.aat.datastore.User user = User.getUser();
        if (firstnameTextView != null) {
            firstnameTextView.setText(user.getFirstName());
        }
        if (lastNameTextView != null) {
            lastNameTextView.setText(user.getLastName());
        }
    }

}
