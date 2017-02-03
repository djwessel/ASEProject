package com.ase.aat_android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aat.datastore.Group;
import com.aat.datastore.Student;
import com.ase.aat_android.data.SessionData;
import com.ase.aat_android.R;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.ase.aat_android.util.Constants;
import com.ase.aat_android.util.EndpointUtil;
import com.ase.aat_android.util.EndpointsURL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private class RetrieveUserTask extends BaseAsyncTask<Long, Boolean, com.aat.datastore.User> {

        public RetrieveUserTask(Activity activity) {
            super(activity);
        }

        @Override
        protected com.aat.datastore.User doInBackground(Long... params) {
            String url = EndpointsURL.HTTP_ADDRESS + EndpointsURL.REQUEST_USER_DATA;
            url = EndpointUtil.solveUrl(url, "user_id", Long.toString(params[0]));
            ClientResource retrieveRes = new ClientResource(Method.GET, url);
            retrieveRes.setResponseEntityBuffering(true);
            retrieveRes.setRequestEntityBuffering(true);
            retrieveRes.accept(MediaType.APPLICATION_ALL_JSON);

            ObjectMapper mapper = new ObjectMapper();
            Student user = null;
            try {
                retrieveRes.getRequest().getCookies().add(0, SessionData.getSessionToken());
                user = mapper.readValue(retrieveRes.get().getText(), new TypeReference<Student>(){});
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ResourceException e) {
                failureMessage = e.getMessage();
            }
            return user;
        }

        @Override
        protected void onPostExecute(com.aat.datastore.User user) {
            super.onPostExecute(user);
            if (user != null) {
                SessionData.updateUser(user);
                updateUserInfo();
            }

        }
    }

    private class RetrieveAttendancesGroups extends BaseAsyncTask<Long, String, HashMap<String,Group> > {

        public  RetrieveAttendancesGroups(Activity activity) {
            super(activity);
        }

        protected HashMap<String,Group> doInBackground(Long... params) {
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String,Group> groups = new HashMap<String,Group>();
            String url = EndpointsURL.HTTP_ADDRESS+ EndpointsURL.REQUEST_GROUPS_STUDENT;
            url = EndpointUtil.solveUrl(url, "user_id", Long.toString(params[0]));
            ClientResource resource = new ClientResource(Method.GET, url);
            resource.setRequestEntityBuffering(true);
            resource.accept(MediaType.APPLICATION_JSON);
            try {
                resource.getRequest().getCookies().add(0, SessionData.getSessionToken());
                groups= mapper.readValue(resource.get().getText(), new TypeReference<HashMap<String,Object>>(){});
            } catch (ResourceException e) {
                failureMessage = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return groups;
        }

        protected void onPostExecute(final HashMap<String,Group> result) {
            super.onPostExecute(result);
            if (result != null) {
                updateAttendancesList(result);
                SessionData.updateAttendances(result);
            }

        }
    }

    private class LogoutTask extends BaseAsyncTask<Long, Boolean, Boolean> {

        public LogoutTask(Activity activity) {
            super(activity, Constants.logoutLoad);
        }
        @Override
        protected Boolean doInBackground(Long... params) {
            String url = EndpointsURL.HTTP_ADDRESS+ EndpointsURL.LOGOUT;
            url = EndpointUtil.solveUrl(url, "user_id", Long.toString(params[0]));
            ClientResource logoutRes = new ClientResource(Method.DELETE, url);
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
    }

    private class AttendancesListAdaper extends BaseAdapter {
        private final ArrayList attendances;
        private LayoutInflater inflater;

        public AttendancesListAdaper(Context context, HashMap<String,Group> attendanceMap) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            attendances = new ArrayList();
            attendances.addAll(attendanceMap.entrySet());
        }

        public void updateAttendances(HashMap<String,Group> attendanceMap) {
            attendances.clear();
            attendances.addAll(attendanceMap.entrySet());
            notifyDataSetChanged();
        }

        public void update() {
            if (!SessionData.getUserAttendances().isEmpty()) {
                attendances.clear();
                attendances.addAll(SessionData.getUserAttendances().entrySet());
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return attendances.size();
        }

        @Override
        public Object getItem(int position) {
            return attendances.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView textView = (TextView) row.findViewById(android.R.id.text1);
            textView.setTextColor(Color.BLACK);
            Map.Entry<String, Group> item = (Map.Entry) getItem(position);
            if (item != null) {
                textView.setText(item.getKey());
            }
            return row;
        }
    }

    private TextView firstnameTextView;
    private TextView lastNameTextView;
    private ListView attendancesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = this.getIntent().getExtras();
        if (extras == null) {
            throw new RuntimeException("Error while retrieving user data");
        }
        Long id = extras.getLong(Constants.userIdKey);

        attendancesListView = (ListView) findViewById(R.id.attendancelistview);
        attendancesListView.setAdapter(new AttendancesListAdaper(getApplicationContext(), new HashMap<String, Group>()));
        setListItemActionListener();
        retrieveUser(id);
        retrieveAttendances(id);
        initializeComponents();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SessionData.getUserAttendances().isEmpty()) {
            retrieveAttendances(SessionData.getUser().getId());
        } else {
            ((AttendancesListAdaper) attendancesListView.getAdapter()).update();
        }
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setListItemActionListener() {
        attendancesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map.Entry<String, Object> listItem = (Map.Entry<String, Object>) attendancesListView.getItemAtPosition(position);
                openQRCodeActivity(listItem);
            }
        });
    }

    private void retrieveUser(Long userID) {
        new RetrieveUserTask(this).execute(userID);
    }


    private void retrieveAttendances(Long userID) {
        if (SessionData.getUser() != null) {
            assert(userID == SessionData.getUser().getId());
            new RetrieveAttendancesGroups(this).execute(SessionData.getUser().getId());
        } else {
            new RetrieveAttendancesGroups(this).execute(userID);
        }
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


    private void updateAttendancesList(HashMap<String, Group> result) {
        ((AttendancesListAdaper) attendancesListView.getAdapter()).updateAttendances(result);
    }

    private void openSigninActivity() {
        Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
    }

    private void openCoursesActivity() {
        Intent intent = new Intent(this, CoursesActivity.class);
        startActivity(intent);
    }


    private void openQRCodeActivity(Map.Entry<String, Object> attendanceItem) {
        Intent intent = new Intent(this, QRCodeActivity.class);
        LinkedHashMap<String, Object> value = (LinkedHashMap<String, Object>) attendanceItem.getValue();
        intent.putExtra("selected_group_name", value.get("name").toString());
        intent.putExtra("selected_group_id", value.get("id").toString());
        intent.putExtra("selected_course_name", attendanceItem.getKey());
        startActivity(intent);
    }

}
