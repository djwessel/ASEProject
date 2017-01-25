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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Courses Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class RetrieveCoursesTask extends BaseAsyncTask<Boolean, Boolean, ArrayList<Course>> {

        public RetrieveCoursesTask(Activity activity) {
            super(activity);
        }

        @Override
        protected ArrayList<Course> doInBackground(Boolean... params) {
            ClientResource coursesRetRes = new ClientResource(Method.GET, Constants.AATUrl + Constants.coursesRetrieveResourceEndpoint);
            coursesRetRes.setResponseEntityBuffering(true);
            coursesRetRes.setRequestEntityBuffering(true);
            coursesRetRes.accept(MediaType.APPLICATION_ALL_JSON);

            ObjectMapper mapper = new ObjectMapper();
            List<Object> courseObjects = null;
            try {
                courseObjects = mapper.readValue(coursesRetRes.get().getText(), new TypeReference<List<Course>>() {
                });
            } catch (ResourceException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (courseObjects == null) {
                return null;
            }
            ArrayList<Course> courses = new ArrayList<Course>(courseObjects.size());
            for (Object obj : courseObjects) {
                if (!(obj instanceof Course)) {
                    throw new RuntimeException("Wrong object from server");
                }
                courses.add((Course) obj);
            }
            return courses;
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
