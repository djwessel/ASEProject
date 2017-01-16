package com.ase.aat_android.view;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.aat.datastore.Course;
import com.aat.datastore.Group;
import com.ase.aat_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.ArrayList;

import com.ase.aat_android.utils.Constants;

public class CourseActivity extends ListActivity {

    private class RetrieveGroupsTask extends BaseAsyncTask<Long, Boolean, ArrayList<Group>> {

        public RetrieveGroupsTask(Activity activity) {
            super(activity);
        }

        @Override
        protected ArrayList<Group> doInBackground(Long... params) {
            String url = getGroupsRetrievingUrl(params[0]);
            ClientResource groupsRetrieveRes = new ClientResource(url);
            try {
                // TODO: change to use Groups interface. Will get groups list.
                groupsRetrieveRes.get();
            } catch (ResourceException e) {
                e.printStackTrace();
                return null;
            }
            return new ArrayList<Group>();
        }


        @Override
        protected void onPostExecute(ArrayList<Group> groups) {
            super.onPostExecute(groups);
            GroupListAdapter adapter = (GroupListAdapter) getListAdapter();
            adapter.updateGroups(groups);
        }

        private String getGroupsRetrievingUrl(Long courseID) {
            StringBuilder urlBuilder = new StringBuilder(Constants.AATUrl);
            urlBuilder.append("course/");
            urlBuilder.append("5659313586569216");
            //urlBuilder.append(courseID);
            urlBuilder.append("/groups");
            return urlBuilder.toString();
        }

        // TODO: remove this function when changed to use Groups Interface
        /*private ArrayList<Group> parseGroups(String result) throws JSONException {
            JSONArray jsonGroups = new JSONArray(result);
            System.out.println(jsonGroups.length());
            for (int i = 0; i < jsonGroups.length(); ++i) {
                JSONObject groupObj = jsonGroups.getJSONObject(i);
                //TODO: won't need groupIDs if receive groups list
                groupIDs.add(Long.parseLong(groupObj.getString("id")));
                Long courseID = Long.parseLong("5659313586569216");
                String name = groupObj.getString("name");
                groups.add(new Group(courseID, name));
            }
            System.out.println(groups.size());
        }*/

    }

    private class GroupListAdapter extends BaseAdapter {

        private class RegisterTask extends BaseAsyncTask<Group, Boolean, Boolean> {

            public RegisterTask(Activity activity) {
                super(activity, "Register");
            }

            @Override
            protected Boolean doInBackground(Group... params) {
                //TODO: create Attendence record
                // To create Attendence record needs user id

                return null;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                // TODO: after registration list should be updated (disable register buttons)
                Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG).show();
            }
        }

        private class DeregisterTask extends BaseAsyncTask<Group, Boolean, Boolean> {

            public DeregisterTask(Activity activity) {
                super(activity, "De-register");
            }

            @Override
            protected Boolean doInBackground(Group... params) {
                //TODO: delete Attendence record
                return null;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                // TODO: after unregistering list should be updated: disable unregister buttons, enable register buttons
                Toast.makeText(getApplicationContext(), "Unregistered", Toast.LENGTH_LONG).show();
            }
        }

        private LayoutInflater inflater;
        private ArrayList<Group> groups;
        // TODO: id's should be able to be passed to group ctor
        private ArrayList<Long> groupIDs;

        public GroupListAdapter(Context context) {
            groups = new ArrayList<Group>();
            groupIDs = new ArrayList<Long>();

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void updateGroups(ArrayList<Group> newGroups) {
            groups = newGroups;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return groups.size();
        }

        @Override
        public Object getItem(int position) {
            return groups.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = inflater.inflate(R.layout.group_list_item, null);
            Group item = (Group) getItem(position);
            TextView groupNameTextView = (TextView) rowView.findViewById(R.id.groupname_textview);
            groupNameTextView.setText(item.getName());

            ImageButton registerButton = (ImageButton) rowView.findViewById(R.id.register_button);
            // TODO: register buttons should be inactive if user has already registered to a group in this course
            setClickListenerToRegisterButton(registerButton, item);

            ImageButton unregisterButton = (ImageButton) rowView.findViewById(R.id.unregister_button);
            setClickListenerToUnregisterButton(unregisterButton, item);
            return rowView;
        }

        private void setClickListenerToRegisterButton(ImageButton button, final Group group) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new RegisterTask(CourseActivity.this).execute(group);
                }
            });
        }

        private void setClickListenerToUnregisterButton(ImageButton button, final Group group) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DeregisterTask(CourseActivity.this).execute(group);
                }
            });
        }

    }


    private TextView courseNameTextView;
    private TextView requiredAttendancesTextView;
    private TextView requiredPresentationsTextView;

    private Long courseID;
    private Course course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        retrieveCourseFromExtras();
        updateTextViews();
        retrieveCourseGroups();

        setListAdapter(new GroupListAdapter(getApplicationContext()));
    }

    private void updateTextViews() {
        courseNameTextView = (TextView) findViewById(R.id.coursename_textview);
        courseNameTextView.setText(course.getTitle());
        requiredAttendancesTextView = (TextView) findViewById(R.id.req_attendancenum_textview);
        requiredAttendancesTextView.setText("Required Attendances: " + course.getReqAtten());
        requiredPresentationsTextView = (TextView) findViewById(R.id.req_presentnum_textview);
        requiredPresentationsTextView.setText("Required Presentations: " + course.getReqPresent());
    }

    private void retrieveCourseFromExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            course = new Course("ASE", 15, 2);
            return;
            //throw new RuntimeException("No course information");
        }
        ArrayList<String> courseData = extras.getStringArrayList(Constants.courseKey);
        courseID = Long.parseLong(courseData.get(0));
        course = new Course(courseData.get(1), Integer.parseInt(courseData.get(2)), Integer.parseInt(courseData.get(3)));
    }

    private void retrieveCourseGroups() {
        // If course object is passed to this activity, will do just course.getId();
        new RetrieveGroupsTask(CourseActivity.this).execute(courseID);
    }

}
