package com.ase.aat_android.view;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aat.datastore.Course;
import com.ase.aat_android.R;
import com.ase.aat_android.utils.Constants;

import org.restlet.resource.ClientResource;

import java.util.ArrayList;

public class CoursesActivity extends ListActivity {

    private class RetrieveCoursesTask extends BaseAsyncTask<Boolean, Boolean, ArrayList<Course>> {

        public RetrieveCoursesTask(Activity activity) {
            super(activity);
        }

        @Override
        protected ArrayList<Course> doInBackground(Boolean... params) {
            ClientResource coursesRetRes = new ClientResource(Constants.AATUrl + Constants.coursesRetrieveResourceEndpoint);
            // TODO: use interface for courses to get list of courses
            coursesRetRes.get();

            return new ArrayList<Course>();
        }

        @Override
        protected void onPostExecute(ArrayList<Course> res) {
            super.onPostExecute(res);
            CourseListAdapter adapter = (CourseListAdapter) getListAdapter();
            adapter.updateCourses(res);
        }
    }

    private class CourseListAdapter extends BaseAdapter {

        private ArrayList<Course> courses;
        private LayoutInflater inflater;

        public CourseListAdapter(Context context) {
            courses = new ArrayList<Course>();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void updateCourses(ArrayList<Course> newcourses) {
            courses = newcourses;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return courses.size();
        }

        @Override
        public Object getItem(int position) {
            return courses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = inflater.inflate(android.R.layout.simple_list_item_1, parent);
            TextView courseNameTextView = (TextView) row.findViewById(R.id.text1);
            Course course = (Course) getItem(position);
            if (course != null) {
                courseNameTextView.setText(course.getTitle());
            }
            return row;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        retrieveCourses();

        setListAdapter(new CourseListAdapter(getApplicationContext()));
        setupListItemClickListener();
    }

    private void setupListItemClickListener() {
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = (Course) parent.getItemAtPosition(position);
                openCourseActivity(course);
            }
        });
    }

    private void openCourseActivity(final Course course) {
        Intent intent = new Intent(this, CourseActivity.class);
        ArrayList<String> courseDataArray = new ArrayList<String>();
        // TODO: make Course class of datastore serializable or create Course's serializable wrapper in android side
        // In that case it will be possible to put course into extras
        courseDataArray.add(0, course.getId().toString());
        courseDataArray.add(1, course.getTitle());
        courseDataArray.add(1, String.valueOf(course.getReqAtten()));
        courseDataArray.add(1, String.valueOf(course.getReqPresent()));
        intent.putStringArrayListExtra(Constants.courseKey, courseDataArray);
        startActivity(intent);
    }


    private void retrieveCourses() {
        new RetrieveCoursesTask(this).execute();
    }

}
