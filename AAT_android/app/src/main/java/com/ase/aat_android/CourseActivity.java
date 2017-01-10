package com.ase.aat_android;

import android.app.ListActivity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends ListActivity {

    private class GroupListAdapter extends BaseAdapter {

        private class RegisterTask extends AsyncTask<Group, Boolean, Boolean> {
            @Override
            protected Boolean doInBackground(Group... params) {
                //TODO: create Attendence record
                return null;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                // TODO: after registration list should be updated (disable register buttons)
                Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG).show();
            }
        }

        private class UnregisterTask extends AsyncTask<Group, Boolean, Boolean> {
            @Override
            protected Boolean doInBackground(Group... params) {
                //TODO: delete Attendence record
                return null;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
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
            return course.groups.size();
        }

        @Override
        public Object getItem(int position) {
            return course.groups.get(position);
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
            groupNameTextView.setText(item.name);

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
                    new RegisterTask().execute(group);
                }
            });
        }

        private void setClickListenerToUnregisterButton(ImageButton button, final Group group) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new UnregisterTask().execute(group);
                }
            });
        }

    }

    // TODO: change to be ase group class.
    public class Group {
        public String name;

        public Group(String name) {
            this.name = name;
        }
    }

    public class Course {
        public String name;
        public ArrayList<Group> groups;

        public Course(String name) {
            this.name = name;
            groups = new ArrayList<Group>();
        }
    }

    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            course = (Course) extras.get("course");
        } else {
            fillCourseWithTestData();
        }
        TextView courseNameTextView = (TextView) findViewById(R.id.coursename_textview);
        courseNameTextView.setText(course.name);

        setListAdapter(new GroupListAdapter(getApplicationContext()));
    }

    private void fillCourseWithTestData() {
        course = new Course("ASE");
        course.groups.add(new Group("group 1"));
        course.groups.add(new Group("group 2"));
        course.groups.add(new Group("group 3"));
        course.groups.add(new Group("group 4"));
        course.groups.add(new Group("group 5"));
        course.groups.add(new Group("group 6"));
        course.groups.add(new Group("group 7"));
    }

}
