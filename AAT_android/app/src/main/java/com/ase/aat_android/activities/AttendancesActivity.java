package com.ase.aat_android.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aat.datastore.Group;
import com.ase.aat_android.R;
import com.ase.aat_android.data.SessionData;
import com.ase.aat_android.util.EndpointUtil;
import com.ase.aat_android.util.EndpointsURL;
import com.ase.aat_android.util.HashMapAdapter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AttendancesActivity extends AppCompatActivity {
    String userId = SessionData.getUser().getId().toString();
    private String url = EndpointsURL.HTTP_ADDRESS+ EndpointsURL.REQUEST_GROUPS_STUDENT;
    TextView content;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendances);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("user_id");
        url = EndpointUtil.solveUrl(url,"user_id",userId);
        new RequestAttendancesGroups().execute(url,null , null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attendances, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_courses) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callQRCodeActivity(String courseName, String groupName, String groupId){
        Intent intent = new Intent(this, QRCodeActivity.class);
        intent.putExtra("selected_group_name", groupName);
        intent.putExtra("selected_group_id", groupId);
        intent.putExtra("selected_course_name", courseName);
        startActivity(intent);
    }


    private class RequestAttendancesGroups extends AsyncTask<String, String, HashMap<String,Group> > {

        private ProgressBar loadingBar;

        public  RequestAttendancesGroups() {
            initializeProgressBar();
        }

        protected HashMap<String,Group> doInBackground(String... urls) {
            ClientResource resource;
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String,Group> groups = new HashMap<String,Group>();
            try {

                resource = new ClientResource(urls[0]);
                resource.setRequestEntityBuffering(true);
                resource.accept(MediaType.APPLICATION_JSON);
                groups= mapper.readValue(resource.get().getText(), new TypeReference<HashMap<String,Object>>(){});

            } catch (ResourceException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return groups;
        }


        protected void onProgressUpdate(String... values) {
            loadingBar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(final HashMap<String,Group> result) {
            loadingBar.setVisibility(View.GONE);

            if (result.size()==0){
                content = (TextView)findViewById(R.id.message);
                content.setText(R.string.text_no_attendences);
            }else{
                HashMapAdapter adapter = new HashMapAdapter(result);
                list = (ListView)findViewById(R.id.list);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Map.Entry<String, Object> listItem = (Map.Entry<String, Object>)list.getItemAtPosition(position);
                        LinkedHashMap<String,Object> value = (LinkedHashMap<String, Object>) listItem.getValue();
                        callQRCodeActivity(listItem.getKey(), value.get("name").toString(), value.get("id").toString());
                    }
                });
                // Assign adapter to List
                list.setAdapter(adapter);
            }
        }

        private void initializeProgressBar() {
            loadingBar = (ProgressBar) findViewById(R.id.progress_attendances);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            loadingBar.setLayoutParams(params);
        }
    }

}
