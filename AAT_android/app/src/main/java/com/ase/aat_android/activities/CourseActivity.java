package com.ase.aat_android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.aat_android.R;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ase.aat_android.data.Course;
import com.ase.aat_android.data.Group;
import com.ase.aat_android.data.SessionData;
import com.ase.aat_android.util.Constants;
import com.ase.aat_android.util.EndpointUtil;
import com.ase.aat_android.util.EndpointsURL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.datastore.Link;

public class CourseActivity extends ListActivity {

    private class RetrieveGroupsTask extends BaseAsyncTask<Long, Boolean, ArrayList<Group>> {

        private Long courseID;

        public RetrieveGroupsTask(Activity activity) {
            super(activity);
        }

        @Override
        protected ArrayList<Group> doInBackground(Long... params) {
            courseID = params[0];
            String url = EndpointsURL.HTTP_ADDRESS + EndpointsURL.REQUEST_COURSE_GROUPS;
            url = EndpointUtil.solveUrl(url, "course_id", Long.toString(params[0]));
            ClientResource groupsRetrieveRes = new ClientResource(Method.GET, url);
            groupsRetrieveRes.setResponseEntityBuffering(true);
            groupsRetrieveRes.setRequestEntityBuffering(true);
            groupsRetrieveRes.accept(MediaType.APPLICATION_ALL_JSON);

            ObjectMapper mapper = new ObjectMapper();
            List<Object> groupObjects = null;
            try {
               groupObjects = mapper.readValue(groupsRetrieveRes.get().getText(), new TypeReference<List<Object>>() {});
            } catch (ResourceException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (groupObjects == null) {
                return null;
            }
            return retrieveGroups(groupObjects);
        }

        private ArrayList<Group> retrieveGroups(List<Object> groupObjects) {
            ArrayList<Group> groups = new ArrayList<Group>(groupObjects.size());
            for (Object obj : groupObjects) {
                LinkedHashMap<String, Object> mapEntry = (LinkedHashMap<String, Object>) obj;
                groups.add(new Group(courseID, mapEntry));
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

    private class RetrieveUserGroupsTask extends  BaseAsyncTask<Long, Boolean, ArrayList<Group>> {
        public RetrieveUserGroupsTask(Activity activity) {
            super(activity);
        }

        @Override
        protected ArrayList<Group> doInBackground(Long... params) {
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Group> o) {
            super.onPostExecute(o);
        }
    }
    private class GroupListAdapter extends BaseAdapter {

        private class RegisterTask extends BaseAsyncTask<Long, Boolean, String> {

            private String failureMessage;

            public RegisterTask(Activity activity) {
                super(activity, "Registering");
            }

            @Override
            protected String doInBackground(Long... params) {
                String url = EndpointsURL.HTTP_ADDRESS+ EndpointsURL.CREATE_ATTENDANCE;
                url = EndpointUtil.solveUrl(url, "course_id", Long.toString(params[0]));
                url = EndpointUtil.solveUrl(url, "group_id", Long.toString(params[1]));
                ClientResource registerRes = new ClientResource(Method.POST, url);
                registerRes.setResponseEntityBuffering(true);
                registerRes.setRequestEntityBuffering(true);
                registerRes.accept(MediaType.APPLICATION_ALL_JSON);
                Form form = new Form();
                form.add(0, new Parameter("user", Long.toString(SessionData.getUser().getId())));
                String result = null;
                try {
                    registerRes.getRequest().getCookies().add(0, SessionData.getSessionToken());
                    result = registerRes.post(form).getText();
                } catch (ResourceException e) {
                    failureMessage = e.getMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String res) {
                super.onPostExecute(res);
                if (res == null && failureMessage != null) {
                    Toast.makeText(getApplicationContext(), failureMessage, Toast.LENGTH_LONG).show();
                } else {
                    SessionData.getUserAttendances().clear();
                    updateGroupsList();
                    Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG).show();
                }
            }
        }

        private class DeregisterTask extends BaseAsyncTask<Long, Boolean, Boolean> {

            private String failureMessage;

            public DeregisterTask(Activity activity) {
                super(activity, "De-register");
            }

            @Override
            protected Boolean doInBackground(Long... params) {
                String url = EndpointsURL.HTTP_ADDRESS+ EndpointsURL.CREATE_ATTENDANCE;
                url = EndpointUtil.solveUrl(url, "course_id", Long.toString(params[0]));
                url = EndpointUtil.solveUrl(url, "group_id", Long.toString(params[1]));
                url = EndpointUtil.solveUrl(url, "user_id", Long.toString(SessionData.getUser().getId()));
                ClientResource deregisterRes = new ClientResource(Method.DELETE, url);
                deregisterRes.setResponseEntityBuffering(true);
                deregisterRes.setRequestEntityBuffering(true);
                deregisterRes.accept(MediaType.APPLICATION_ALL_JSON);
                try {
                    deregisterRes.getRequest().getCookies().add(0, SessionData.getSessionToken());
                    deregisterRes.delete();
                } catch (ResourceException e) {
                    failureMessage = e.getMessage();
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (!result) {
                    Toast.makeText(getApplicationContext(), "Failed to de-register, you lucky", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "De-registered", Toast.LENGTH_LONG).show();
                    updateGroupsList();
                }
            }
        }

        private LayoutInflater inflater;
        private ArrayList<Group> groups;

        private int row_items_padding = 15;

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
            groupNameTextView.setTextColor(Color.BLACK);
            groupNameTextView.setText(item.getName());
            groupNameTextView.setTextSize(20);
            groupNameTextView.setPadding(row_items_padding, row_items_padding, row_items_padding, row_items_padding);

            ImageButton registerButton = (ImageButton) rowView.findViewById(R.id.register_button);
            ImageButton deregisterButton = (ImageButton) rowView.findViewById(R.id.unregister_button);
            registerButton.setPadding(row_items_padding, row_items_padding, row_items_padding, row_items_padding);
            deregisterButton.setPadding(row_items_padding, row_items_padding, row_items_padding, row_items_padding);
            deregisterButton.setEnabled(false);

            Object groupObj =  SessionData.getUserAttendances().get(course.getTitle());
            if (groupObj != null) {
               LinkedHashMap<String, Object> group = (LinkedHashMap) groupObj;
               if (group.get("id").equals(item.getID())) {
                   groupNameTextView.setTypeface(null, Typeface.BOLD_ITALIC);
                   registerButton.setEnabled(false);
                   deregisterButton.setEnabled(true);
                }
            }
            setClickListenerToRegisterButton(registerButton, item);
            setClickListenerToUnregisterButton(deregisterButton, item);

            return rowView;
        }

        private void setClickListenerToRegisterButton(ImageButton button, final Group group) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new RegisterTask(CourseActivity.this).execute(group.getParentID(), group.getID());
                }
            });
        }

        private void setClickListenerToUnregisterButton(ImageButton button, final Group group) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createWarningDialog(group);
                }
            });
        }

        private void createWarningDialog(final Group group) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CourseActivity.this);
            dialogBuilder.setTitle("Warning!");
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("You are going to deregister from group " + group.getName());
            messageBuilder.append(". All your records are going to be deleted");
            dialogBuilder.setMessage(messageBuilder.toString());
            dialogBuilder.setCancelable(true);
            dialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new DeregisterTask(CourseActivity.this).execute(group.getParentID(), group.getID());
                }
            });
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialogBuilder.show();
                }
            });

        }
    }

    private TextView courseNameTextView;
    private TextView requiredAttendancesTextView;
    private TextView requiredPresentationsTextView;

    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        retrieveCourseFromExtras();
        updateTextViews();
        retrieveCourseGroups();
        retrieveGroupsForUser();

        setListAdapter(new GroupListAdapter(getApplicationContext()));
    }

    private void updateTextViews() {
        courseNameTextView = (TextView) findViewById(R.id.coursename_textview);
        courseNameTextView.setText(course.getTitle());
        requiredAttendancesTextView = (TextView) findViewById(R.id.req_attendancenum_textview);
        requiredAttendancesTextView.setText("Required Attendances: " + course.getRequiredAttendances());
        requiredPresentationsTextView = (TextView) findViewById(R.id.req_presentnum_textview);
        requiredPresentationsTextView.setText("Required Presentations: " + course.getRequiredPresentation());
    }

    private void retrieveCourseFromExtras() {
        Bundle extras = getIntent().getExtras();
        course = (Course) extras.getSerializable(Constants.courseKey);
    }

    private void retrieveCourseGroups() {
        new RetrieveGroupsTask(CourseActivity.this).execute(course.getID());
    }

    private void retrieveGroupsForUser() {
        new RetrieveUserGroupsTask(CourseActivity.this).execute(course.getID());
    }


    private void updateGroupsList() {
        ((GroupListAdapter) getListAdapter()).notifyDataSetChanged();
        SessionData.getUserAttendances().remove(course.getTitle());
    }



}
