package com.example.robinsuri.tring;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;


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

        final ZeusService zeusservice = new ZeusService();
        zeusservice.setStagingUrl("https://proxy-staging-external.handler.talk.to/");

        final callbackForSessionCreate callbackForSessionCreate = new callbackForSessionCreate() {
            @Override
            public void sessionCallback(String mapping) {
                Log.d("Tring", "mapping : " + mapping);
                Intent intent = new Intent(tring, NumberActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "Call the number for verification : " + mapping);
                startActivity(intent);

            }

            @Override
            public void handleError(Exception e, String errorMessage) {

                new AlertDialog.Builder(tring)
                        .setTitle("Error!!")
                        .setMessage("Error in processing").setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        };


      zeusservice.getmapping(firstName, lastName, number, emailId, callbackForSessionCreate);


    }

    public interface callbackForSessionCreate {
        void sessionCallback(String mapping);

        void handleError(Exception e, String errorMessage);
    }

    public interface callbackForCreateAccount {
        void createCallback(String guid);

        void handleError(Exception e, String errorMessage);
    }

}
