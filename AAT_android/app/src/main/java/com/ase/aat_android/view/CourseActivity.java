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

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.util.ArrayList;

import utils.Constants;

public class CourseActivity extends ListActivity {

    private class RetrieveGroupsTask extends BaseAsyncTask<Long, Boolean, Boolean> {

        public RetrieveGroupsTask(Activity activity) {
            super(activity);
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            StringBuilder urlBuilder = new StringBuilder(Constants.AATUrl);
            urlBuilder.append("course/");
            urlBuilder.append(params[0]);
            urlBuilder.append("/groups");
            ClientResource groupsRetrieveRes = new ClientResource(urlBuilder.toString());
            // TODO: should get group list from representation
            Representation result = groupsRetrieveRes.get();
            return (result != null);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            getListAdapter().notifyAll();
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

        public GroupListAdapter(Context context) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


    private Course course;
    private ArrayList<Group> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            course = (Course) extras.get("course");
        } else {
            throw new RuntimeException("No course information");
        }
        TextView courseNameTextView = (TextView) findViewById(R.id.coursename_textview);
        courseNameTextView.setText(course.getTitle());

        retrieveCourseGroups();

        setListAdapter(new GroupListAdapter(getApplicationContext()));
    }

    private void retrieveCourseGroups() {
        new RetrieveGroupsTask(CourseActivity.this).execute(course.getId());
    }

}
