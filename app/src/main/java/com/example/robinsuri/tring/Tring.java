package com.example.robinsuri.tring;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.IOException;


public class Tring extends Activity implements BlankFragment.OnFragmentInteractionListener {

    public static final String PREFS_NAME = "MyPrefsFile";
    final String EXTRA_MESSAGE = "message";
    String stagingUrl = "https://proxy-staging-external.handler.talk.to/";
    public static SharedPreferences settings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = this.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        Log.d("Tring", "Inside onCreate of Tring");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tring);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String number = settings.getString("number", "null");

        Boolean isBackButtonPressed = settings.getBoolean("isBackButtonPressed", true);
        String token = settings.getString("token", "");
        Log.d("Tring", "Token : " + token);
        if (!"null".equals(number) && isBackButtonPressed == false && token.equals("")) {
            Intent intent = new Intent(this, NumberActivity.class);
            intent.putExtra(EXTRA_MESSAGE, number);
            startActivityForResult(intent, 5);

        }

        if (!token.equals("")) {
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
            Log.d("Tring", "After intent of MainScreen");
            this.finish();
        }


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
        zeusservice.setContext(this.getApplicationContext());
        zeusservice.setSharePreferencesData(getSharedPreferences(PREFS_NAME, 0));

        zeusservice.setStagingUrl(stagingUrl);

        ListenableFuture<String> future = zeusservice.getmapping(firstName, lastName, number, emailId);
        Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void onSuccess(final String mapping) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(tring, NumberActivity.class);
                        intent.putExtra(EXTRA_MESSAGE, mapping);
                        Log.d("Tring", "startActivity NumberActivity");
                        startActivityForResult(intent, 1);
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Tring", "Inside onActivityResult of Tring2");
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Tring", "Inside onDestroy");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
