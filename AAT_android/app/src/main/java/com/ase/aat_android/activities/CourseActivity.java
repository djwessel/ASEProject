package com.ase.aat_android.activities;

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
import com.aat.interfaces.restlet.IGroupsResource;
import com.ase.aat_android.R;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ase.aat_android.util.Constants;
import com.ase.aat_android.util.EndpointUtil;
import com.ase.aat_android.util.EndpointsURL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CourseActivity extends ListActivity {

    private class RetrieveGroupsTask extends BaseAsyncTask<Long, Boolean, ArrayList<Group>> {

        public RetrieveGroupsTask(Activity activity) {
            super(activity);
        }

        @Override
        protected ArrayList<Group> doInBackground(Long... params) {
            String url = EndpointsURL.HTTP_ADDRESS + EndpointsURL.REQUEST_COURSE_GROUPS;
            url = EndpointUtil.solveUrl(url, "course_id", Long.toString(params[0]));
            ClientResource groupsRetrieveRes = new ClientResource(Method.GET, url);
            groupsRetrieveRes.setResponseEntityBuffering(true);
            groupsRetrieveRes.setRequestEntityBuffering(true);
            groupsRetrieveRes.accept(MediaType.APPLICATION_ALL_JSON);

            ObjectMapper mapper = new ObjectMapper();
            List<Object> groupObjects = null;
            try {
                // Throws exception.
               // groupObjects = mapper.readValue(groupsRetrieveRes.get().getText(), new TypeReference<List<Group>>() {});
            } catch (ResourceException e) {
                e.printStackTrace();
            } /*catch (IOException e) {
                e.printStackTrace();
            }*/
            if (groupObjects == null) {
                return null;
            }
            return retrieveGroups(groupObjects);
        }

        private ArrayList<Group> retrieveGroups(List<Object> groupObjects) {
            ArrayList<Group> groups = new ArrayList<Group>(groupObjects.size());
            for (Object obj : groupObjects) {
                if (!(obj instanceof Group)) {
                    throw new RuntimeException("Wrong object from server");
                }
                groups.add((Group) obj);
            }
            return groups;
        }


        @Override
        protected void onPostExecute(ArrayList<Group> groups) {
            super.onPostExecute(groups);
            if (groups != null) {
                GroupListAdapter adapter = (GroupListAdapter) getListAdapter();
                adapter.updateGroups(groups);
            }
        }

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

        public GroupListAdapter(Context context) {
            groups = new ArrayList<Group>();
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
        ArrayList<String> courseData = extras.getStringArrayList(Constants.courseKey);
        courseID = Long.parseLong(courseData.get(0));
        course = new Course(courseData.get(1), Integer.parseInt(courseData.get(2)), Integer.parseInt(courseData.get(3)));
    }

    private void retrieveCourseGroups() {
        // If course object is passed to this activity, will do just course.getId();
        new RetrieveGroupsTask(CourseActivity.this).execute(courseID);
    }

}
