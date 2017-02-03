package com.ase.aat_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aat.datastore.Group;
import com.ase.aat_android.R;
import com.ase.aat_android.data.GroupPojo;
import com.ase.aat_android.data.SessionData;
import com.ase.aat_android.util.EndpointUtil;
import com.ase.aat_android.util.EndpointsURL;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class QRCodeActivity extends AppCompatActivity {

    private TextView displayTextView;
    private String url = EndpointsURL.HTTP_ADDRESS+ EndpointsURL.REQUEST_QR_CODE;
    private ImageView imageView;
    private Button requestQRButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayTextView = (TextView)findViewById(R.id.msg);
        imageView = (ImageView)findViewById(R.id.imageView);
        requestQRButton = (Button) findViewById(R.id.request_qr_button);

        Intent intent = getIntent();
        String courseName = intent.getStringExtra("selected_course_name");
        GroupPojo group = (GroupPojo) intent.getExtras().getSerializable("selected_group");
        String userId = SessionData.getUser().getId().toString();

        url = EndpointUtil.solveUrl(url,"user_id",userId);
        url = EndpointUtil.solveUrl(url,"group_id", group.getID().toString());
        url = EndpointUtil.solveUrl(url, "course_id", group.getParentID().toString());

        displayTextView.setText("You have signed up for the course "+courseName +
                                " and its correspondient group: "+group.getName()+"."+
                                " To register your attendance for this week request your QR code.");

        requestQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequestToken(QRCodeActivity.this).execute(url,"" , "");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qrcode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Encode a given string into a Bitmap which represent QR code
     * @Param String data
     * @Return Bitmap
     * */
    private Bitmap textToImageEncode(String data) {

        Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hintMap.put(EncodeHintType.MARGIN, 1); // default = 4
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix bitMatrix;
        int QRcodeWidth = 700 ;
        int [] pixels = new int [QRcodeWidth*QRcodeWidth];

        try{
            bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.DATA_MATRIX.QR_CODE,QRcodeWidth,QRcodeWidth,hintMap);
        }catch(WriterException e){
            return null;
        }

        for (int y = 0; y < QRcodeWidth; y++){
            int offset = y * QRcodeWidth;
            for (int x = 0; x < QRcodeWidth; x++){
                pixels[offset + x] = bitMatrix.get(x,y)? getResources().getColor(R.color.black):getResources().getColor(R.color.white);;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(QRcodeWidth, QRcodeWidth, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, QRcodeWidth, 0, 0, QRcodeWidth, QRcodeWidth);
        bitmap = Bitmap.createScaledBitmap(bitmap,QRcodeWidth,QRcodeWidth,false);
        return bitmap;
    }

    /**
     * Private class to call service
     * */
    private class RequestToken extends BaseAsyncTask<String, String, Bitmap > {


        public RequestToken(Activity activity) {
            super(activity);;
        }

        protected Bitmap doInBackground(String... urls) {
            String token = "";
            ClientResource resource;
            try {
                resource = new ClientResource(urls[0]);
                resource.setRequestEntityBuffering(true);
                resource.getRequest().getCookies().add(0, SessionData.getSessionToken());
                token = resource.post(new Form()).getText();
            } catch (ResourceException e) {
                failureMessage = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return textToImageEncode(token);
        }

        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }

    }
}
