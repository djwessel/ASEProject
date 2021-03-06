package com.ase.aat_android.activities;

import android.app.Activity;
import android.widget.Toast;

import com.ase.aat_android.data.SessionData;
import com.ase.aat_android.util.Constants;
import com.ase.aat_android.util.EndpointsURL;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * The only reason why this class is not private like other tasks,
 * is that it is used from both signIn and signup activities
 * Created by anahitik on 02.02.17.
 */

public class SigninTask extends BaseAsyncTask<String, Boolean, Long> {
    private Runnable callback;

    public  SigninTask(Activity activity) throws URISyntaxException {
        super(activity, Constants.loginLoad);
    }

    public void setCallback(Runnable runnable) {
        callback = runnable;
    }

    @Override
    protected Long doInBackground(String... params) {
        String url = EndpointsURL.HTTP_ADDRESS+ EndpointsURL.LOGIN;
        ClientResource loginRes = createClientResource(Method.POST, url, false);
        Form loginForm = createLoginForm(params[0], params[1]);
        Long result;
        try {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.convertValue(loginRes.post(loginForm, MediaType.ALL).getText(), Long.class);
            SessionData.updateSessionToken(loginRes.getResponse().getCookieSettings().getFirst(Constants.sessionToken));
        } catch (ResourceException e) {
            failureMessage = e.getMessage();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    private Form createLoginForm(String username, String password) {
        Form form = new Form();
        form.add(new Parameter(Constants.emailParamName, username));
        form.add(new Parameter(Constants.passwordParamName, password));
        return form;
    }

    @Override
    protected void onPostExecute(Long res) {
        super.onPostExecute(res);
        if (res != null && callback != null) {
            callback.run();
        }
    }
}
