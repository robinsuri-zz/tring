package com.example.robinsuri.tring;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Tring extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tring);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tring, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickNext(View v) throws IOException {

        EditText fName = (EditText) findViewById(R.id.firstName);
        EditText lName = (EditText) findViewById(R.id.lastName);
        EditText mNumber = (EditText) findViewById(R.id.phoneNumber);
        EditText email = (EditText) findViewById(R.id.email);

        String firstName = fName.getText().toString();
        String lastName = lName.getText().toString();
        String number =  "+91"+mNumber.getText().toString();
        String emailId = email.getText().toString();
        Log.d("new",firstName+" "+lastName+" "+number+" "+emailId);

        String jsonRequestGetOrCreate = "{\n" +
                "  \"name\" : {\n" +
                "    \"firstName\" : \""+firstName+"\",\n" +
                "    \"lastName\" : \""+lastName+"\"\n" +
                "  },\n" +
                "  \"emails\" : [\""+emailId+"\"],\n" +
                "  \"mobiles\" : [ \""+number+"\"]\n" +
                "}";
        Log.d("Tring",jsonRequestGetOrCreate);

    // send post request to /kujo.app/zeus/1.0/getOrCreateProfile with the above string
String url = "https://proxy-staging-external.handler.talk.to/kujo.app/zeus/1.0/getOrCreateProfile";
        final HttpPost postRequest = new HttpPost("https://proxy-staging-external.handler.talk.to/kujo.app/zeus/1.0/getOrCreateProfile");

        StringEntity entity = new StringEntity(jsonRequestGetOrCreate);
        entity.setContentType(new BasicHeader("Content-Type","application/json"));
        postRequest.setEntity(entity);

        Executor executor = Executors.newSingleThreadExecutor();
         HttpResponse response;

           sendPostRequest.requestCallback  requestCback= new sendPostRequest.requestCallback() {
               @Override
               public void getsResponse(HttpResponse httpresponse) throws IOException {
                 Log.d("Tring",httpresponse.toString());
                   HttpEntity entity = httpresponse.getEntity();
                   String responseString = EntityUtils.toString(entity, "UTF-8");
                   Log.d("Tring",responseString);

               }
           };

        sendPostRequest spRequest = new sendPostRequest();
        spRequest.sendRequest(postRequest,requestCback);

//        Log.d("Tring", response.toString());


    }
}
