package com.ase.aat_android.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aat.datastore.Course;
import com.ase.aat_android.R;
import com.ase.aat_android.util.Constants;
import com.ase.aat_android.util.EndpointsURL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.RuntimeExecutionException;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CoursesActivity extends ListActivity {
    private class RetrieveCoursesTask extends BaseAsyncTask<Boolean, Boolean, List<Course>> {

        public RetrieveCoursesTask(Activity activity) {
            super(activity);
        }

        @Override
        protected List<Course> doInBackground(Boolean... params) {
            String url = EndpointsURL.HTTP_ADDRESS+ EndpointsURL.REQUEST_COURSES;
            ClientResource coursesRetRes = new ClientResource(Method.GET, url);
            coursesRetRes.setResponseEntityBuffering(true);
            coursesRetRes.setRequestEntityBuffering(true);
            coursesRetRes.accept(MediaType.APPLICATION_ALL_JSON);

            ObjectMapper mapper = new ObjectMapper();
            List<Course> courseObjects = null;
            try {
                courseObjects = mapper.readValue(coursesRetRes.get().getText(), new TypeReference<List<Course>>() {});
            } catch (ResourceException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (courseObjects == null) {
                return null;
            }
            return courseObjects;
        }

        @Override
        protected void onPostExecute(List<Course> res) {
            super.onPostExecute(res);
            if (res != null) {
                CourseListAdapter adapter = (CourseListAdapter) getListAdapter();
                adapter.updateCourses(res);
            }
        }
    }

    private class CourseListAdapter extends BaseAdapter {

        private ArrayList<Course> courses;
        private LayoutInflater inflater;

        public CourseListAdapter(Context context) {
            courses = new ArrayList<Course>();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void updateCourses(List<Course> newcourses) {
            courses.addAll(newcourses);
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
            View row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView courseNameTextView = (TextView) row.findViewById(android.R.id.text1);
            courseNameTextView.setTextColor(Color.BLACK);
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
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.courseKey, new com.ase.aat_android.data.Course(course));
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void retrieveCourses() {
        new RetrieveCoursesTask(this).execute();
    }

}
