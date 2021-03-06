package com.ase.aat_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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
import com.ase.aat_android.util.Constants;
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
    private ImageView imageView;
    private Button requestQRButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        displayTextView = (TextView)findViewById(R.id.msg);
        imageView = (ImageView)findViewById(R.id.imageView);
        requestQRButton = (Button) findViewById(R.id.request_qr_button);


        Intent intent = getIntent();
        String courseName = intent.getStringExtra(Constants.courseKey);
        final GroupPojo group = (GroupPojo) intent.getExtras().getSerializable(Constants.groupKey);
        final Long userId = SessionData.getUser().getId();

        displayTextView.setText("You have signed up for the course " + courseName +
                                " and its correspondient group: " + group.getName() + "." +
                                " To register your attendance for this week request your QR code.");

        requestQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequestToken(QRCodeActivity.this).execute(userId, group.getParentID(), group.getID());
            }
        });
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
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);

        int QRcodeWidth = imageView.getWidth();
        int QRcodeHeight = imageView.getHeight();

        int [] pixels = new int [QRcodeWidth*QRcodeHeight];

        try{
            bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.DATA_MATRIX.QR_CODE,QRcodeWidth,QRcodeHeight,hintMap);
        }catch(WriterException e){
            return null;
        }

        for (int y = 0; y < QRcodeWidth; y++){
            int offset = y * QRcodeWidth;
            for (int x = 0; x < QRcodeWidth; x++){
                pixels[offset + x] = bitMatrix.get(x,y)? getResources().getColor(R.color.white):getResources().getColor(R.color.black);;
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
    private class RequestToken extends BaseAsyncTask<Long, String, Bitmap > {


        public RequestToken(Activity activity) {
            super(activity);;
        }

        protected Bitmap doInBackground(Long... params) {
            String url = EndpointsURL.HTTP_ADDRESS+ EndpointsURL.REQUEST_QR_CODE;
            url = EndpointUtil.solveUrl(url, EndpointsURL.user_id, params[0].toString());
            url = EndpointUtil.solveUrl(url, EndpointsURL.course_id, params[1].toString());
            url = EndpointUtil.solveUrl(url, EndpointsURL.group_id, params[2].toString());

            String token = "";
            ClientResource resource = createClientResource(Method.POST, url, true);
            try {
                token = resource.post(new Form()).getText();
            } catch (ResourceException e) {
                failureMessage = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (token.isEmpty()) {
                return null;
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
