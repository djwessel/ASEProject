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
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.ase.aat_android.data.CoursePojo;
import com.ase.aat_android.data.GroupPojo;
import com.ase.aat_android.data.SessionData;
import com.ase.aat_android.util.Constants;
import com.ase.aat_android.util.EndpointUtil;
import com.ase.aat_android.util.EndpointsURL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CourseActivity extends ListActivity {

    private class RetrieveGroupsTask extends BaseAsyncTask<Long, Boolean, ArrayList<GroupPojo>> {

        public RetrieveGroupsTask(Activity activity) {
            super(activity);
        }

        @Override
        protected ArrayList<GroupPojo> doInBackground(Long... params) {
            String url = EndpointsURL.HTTP_ADDRESS + EndpointsURL.REQUEST_COURSE_GROUPS;
            url = EndpointUtil.solveUrl(url, EndpointsURL.course_id, Long.toString(params[0]));
            ClientResource groupsRetrieveRes = createClientResource(Method.GET, url, false);
            ObjectMapper mapper = new ObjectMapper();
            List<Object> groupObjects = null;
            try {
               groupObjects = mapper.readValue(groupsRetrieveRes.get().getText(), new TypeReference<List<Object>>() {});
            } catch (ResourceException e) {
                failureMessage = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (groupObjects == null) {
                return null;
            }
            return retrieveGroups(groupObjects);
        }

        @Override
        protected void onPostExecute(ArrayList<GroupPojo> groups) {
            super.onPostExecute(groups);
            if (groups != null) {
                GroupListAdapter adapter = (GroupListAdapter) getListAdapter();
                adapter.updateGroups(groups);
            }
        }

        private ArrayList<GroupPojo> retrieveGroups(List<Object> groupObjects) {
            ArrayList<GroupPojo> groups = new ArrayList<GroupPojo>(groupObjects.size());
            for (Object obj : groupObjects) {
                LinkedHashMap<String, Object> mapEntry = (LinkedHashMap<String, Object>) obj;
                groups.add(new GroupPojo(course.getID(), mapEntry));
            }
            return groups;
        }
    }

    private class GroupListAdapter extends BaseAdapter {

        private class RegisterTask extends BaseAsyncTask<Long, Boolean, String> {

            private String failureMessage;

            public RegisterTask(Activity activity) {
                super(activity, Constants.registerLoad);
            }

            @Override
            protected String doInBackground(Long... params) {
                String url = EndpointsURL.HTTP_ADDRESS+ EndpointsURL.CREATE_ATTENDANCE;
                url = EndpointUtil.solveUrl(url, EndpointsURL.course_id, Long.toString(params[0]));
                url = EndpointUtil.solveUrl(url, EndpointsURL.group_id, Long.toString(params[1]));
                ClientResource registerRes = createClientResource(Method.POST, url, true);
                Form form = new Form();
                form.add(0, new Parameter(EndpointsURL.user, Long.toString(SessionData.getUser().getId())));
                String result = null;
                try {
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
                if (res != null) {
                    SessionData.getUserAttendances().clear();
                    updateGroupsList();
                    Toast.makeText(getApplicationContext(), Constants.registered, Toast.LENGTH_LONG).show();
                }
            }
        }

        private class DeregisterTask extends BaseAsyncTask<Long, Boolean, Boolean> {
            public DeregisterTask(Activity activity) {
                super(activity, Constants.deregisterLoad);
            }

            @Override
            protected Boolean doInBackground(Long... params) {
                String url = EndpointsURL.HTTP_ADDRESS+ EndpointsURL.DELETE_ATTENDANCE;
                url = EndpointUtil.solveUrl(url, EndpointsURL.course_id, Long.toString(params[0]));
                url = EndpointUtil.solveUrl(url, EndpointsURL.group_id, Long.toString(params[1]));
                url = EndpointUtil.solveUrl(url, EndpointsURL.user_id, Long.toString(SessionData.getUser().getId()));
                ClientResource deregisterRes = createClientResource(Method.DELETE, url, true);
                try {
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
                if (result) {
                    Toast.makeText(getApplicationContext(), Constants.deregistered, Toast.LENGTH_LONG).show();
                    updateGroupsList();
                }
            }
        }

        private LayoutInflater inflater;
        private ArrayList<GroupPojo> groups;
        private GroupPojo userGroup;

        public GroupListAdapter(Context context) {
            groups = new ArrayList<GroupPojo>();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void updateGroups(ArrayList<GroupPojo> newGroups) {
            groups = newGroups;
            userGroup =  SessionData.getRegisteredGroup(course.getTitle());
            notifyDataSetChanged();
        }

        public void updateUserGroup(GroupPojo group) {
            userGroup = group;
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
            GroupPojo item = (GroupPojo) getItem(position);
            TextView groupNameTextView = (TextView) rowView.findViewById(R.id.groupname_textview);
            groupNameTextView.setText(item.getName());
            groupNameTextView.setTextColor(Color.BLACK);

            ImageButton registerButton = (ImageButton) rowView.findViewById(R.id.register_button);
            ImageButton deregisterButton = (ImageButton) rowView.findViewById(R.id.unregister_button);
            deregisterButton.setEnabled(false);

            if (userGroup != null) {
                if (userGroup.getID().equals(item.getID())) {
                    groupNameTextView.setTypeface(null, Typeface.BOLD_ITALIC);
                    registerButton.setEnabled(false);
                    deregisterButton.setEnabled(true);
                }
            }
            setClickListenerToRegisterButton(registerButton, item);
            setClickListenerToUnregisterButton(deregisterButton, item);

            return rowView;
        }

        private void setClickListenerToRegisterButton(ImageButton button, final GroupPojo group) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new RegisterTask(CourseActivity.this).execute(group.getParentID(), group.getID());
                }
            });
        }

        private void setClickListenerToUnregisterButton(ImageButton button, final GroupPojo group) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createWarningDialog(group);
                }
            });
        }

        private void createWarningDialog(final GroupPojo group) {
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

    private CoursePojo course;

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
        requiredAttendancesTextView.append(Integer.toString(course.getRequiredAttendances()));
        requiredPresentationsTextView = (TextView) findViewById(R.id.req_presentnum_textview);
        requiredPresentationsTextView.append(Integer.toString(course.getRequiredPresentation()));
    }

    private void retrieveCourseFromExtras() {
        Bundle extras = getIntent().getExtras();
        course = (CoursePojo) extras.getSerializable(Constants.courseKey);
    }

    private void retrieveCourseGroups() {
        new RetrieveGroupsTask(CourseActivity.this).execute(course.getID());
    }

    private void updateGroupsList() {
        SessionData.getUserAttendances().remove(course.getTitle());
        ((GroupListAdapter) getListAdapter()).notifyDataSetChanged();
    }

}
