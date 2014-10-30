package com.example.robinsuri.tring;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


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
        final Tring tring = this;
        final String EXTRA_MESSAGE = "message";
        Log.d("Tring", "inside onClickNext");
        final SendHttpRequestImpl spRequest = new SendHttpRequestImpl();
        EditText fName = (EditText) findViewById(R.id.firstName);
        EditText lName = (EditText) findViewById(R.id.lastName);
        EditText mNumber = (EditText) findViewById(R.id.phoneNumber);
        EditText email = (EditText) findViewById(R.id.email);

        String firstName = fName.getText().toString();
        String lastName = lName.getText().toString();
        String number = "+91" + mNumber.getText().toString();
        String emailId = email.getText().toString();
        Log.d("new", firstName + " " + lastName + " " + number + " " + emailId);

        ZeusClient zeusclient = new ZeusClient();
        TestCallBack testcallback = new TestCallBack() {
            @Override
            public void getItBack(String mapping) {
                Log.d("Tring", "mapping : " + mapping);
                Intent intent = new Intent(tring, NumberActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "Call the number for verification : " + mapping);
                startActivity(intent);

            }
        };

        zeusclient.getMapping
                (firstName, lastName, number, emailId, testcallback);



    }
public interface TestCallBack{
    void getItBack(String mapping);
}
    HttpPost preparePostRequest(String url, String request) {
        final HttpPost postRequest = new HttpPost(url);
        StringEntity entity = null;
        try {
            entity = new StringEntity(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        entity.setContentType(new BasicHeader("Content-Type", "application/json"));
        postRequest.setEntity(entity);
        return postRequest;
    }
}
