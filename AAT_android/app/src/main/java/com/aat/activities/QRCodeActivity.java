package com.aat.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aat.util.EnpointUtil;
import com.aat.util.EnpointsURL;
import com.ase.aat_android.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class QRCodeActivity extends AppCompatActivity {

    private TextView displayTextView;
    String userId="5722646637445120";
    private String url = EnpointsURL.HTTP_ADDRESS+EnpointsURL.REQUEST_QR_CODE;
    private ImageView imageView;
    private Bitmap bitmap ;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayTextView = (TextView)findViewById(R.id.msg);
        imageView = (ImageView)findViewById(R.id.imageView);

        Intent intent = getIntent();
        String groupName = intent.getStringExtra("selected_group_name");
        String groupId = intent.getStringExtra("selected_group_id");
        String courseName = intent.getStringExtra("selected_course_name");

        url = EnpointUtil.solveUrl(url,"user_id",userId);
        url = EnpointUtil.solveUrl(url,"group_id",groupId);
        displayTextView.setText("You have signed up for the course "+courseName +
                                " and its correspondient group: "+groupName+"."+
                                " To register your attendance for this week request your QR code.");
        initializeProgressBar();
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


    /** Called when the user clicks the Send button */
    public void requestQRcode(View view) {
        try {
            new RequestToken().execute(url,"" , "").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void initializeProgressBar() {
        loadingBar = (ProgressBar) findViewById(R.id.progress_qr);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        loadingBar.setLayoutParams(params);
        loadingBar.setVisibility(View.GONE);
    }

    /**
     * Encode a given string into a Bitmap which represent QR code
     * @Param String data
     * @Return Bitmap
     * */
    public Bitmap TextToImageEncode (String data) {

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
    private class RequestToken extends AsyncTask<String, String, String > {


        public RequestToken(){
           // initializeProgressBar();
        }

        protected String doInBackground(String... urls) {
            String token = "";
            ClientResource resource;
            try {
                resource = new ClientResource(urls[0]);
                resource.setRequestEntityBuffering(true);
                token = resource.get().getText();
            } catch (ResourceException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return token;
        }

        protected void onProgressUpdate(String... values) {
            loadingBar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(String result) {
            loadingBar.setVisibility(View.GONE);
            bitmap = TextToImageEncode(result);
            imageView.setImageBitmap(bitmap);
        }

    }
}