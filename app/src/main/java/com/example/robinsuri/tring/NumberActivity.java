package com.example.robinsuri.tring;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NumberActivity extends Activity {
    public static final String PREFS_NAME = "MyPrefsFile";
    String stagingUrl = "https://proxy-staging-external.handler.talk.to/";
    boolean isClicked = false;
    public static SharedPreferences settings = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = this.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        Log.d("NumberActivity", "Inside onCreate of NumberActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        EditText t = (EditText) findViewById(R.id.calltext);
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        t.setText("Call the number for verification : " + message);

        SharedPreferences settings = this.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isBackButtonPressed", false);
        editor.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.number, menu);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("NumberActivity", "Inside onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("NumberActivity", "Inside onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("NumberActivity", "Inside onResume");
        final NumberActivity numberActivity = this;

        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        boolean isPaused = settings.getBoolean("isPaused", false);
        Log.d("NumberActivity", "Value of isClicked = " + isClicked);
        if (isClicked) {
            final ZeusService zeusservice = new ZeusService();
            Log.d("NumberActivity", "After call");
            zeusservice.setStagingUrl(stagingUrl);
            SharedPreferences.Editor editor = settings.edit();
            String sessionId = settings.getString("sessionId", "");
            String guid = settings.getString("guid", "");
            Log.d("NumberActivity", "Calling getToken of zeusService");
            ListenableFuture<String> future = zeusservice.getToken(guid, sessionId);
            Futures.addCallback(future, new FutureCallback<String>() {
                @Override
                public void onSuccess(String token) {
                    Log.d("NumberActivity", "After REfactoring Token is : " + token);
//                    SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putString("token", token);
//                        editor.commit();
                    runOnUiThread(new Runnable() {


                        @Override
                        public void run() {
                            Intent intent = new Intent(numberActivity, MainScreen.class);
                            startActivity(intent);
                            Log.d("NumberActivity", "SetResult called");
                            setResult(RESULT_OK, new Intent());
                            finish();
                            Log.d("NumberActivity", "Finish Called");
                        }
                    });

                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });

            Log.d("NumberActivity", "Before addCallback current Thread Name : " + Thread.currentThread().getName());
            editor.putBoolean("isPaused", false);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("NumberActivity", "Inside onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("NumberActivity", "Inside onPause()");
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isPaused", true);
        editor.commit();

    }

    public void onClickCall(View v) {
        isClicked = true;
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        Log.d("NumberActivity", "Mobile number : " + message);

        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + message));
        try {
            startActivity(phoneIntent);

            Log.i("Finished making a call...", "");

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(NumberActivity.this,
                    "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("NumberActivity", "Inside onBackPressed");
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isBackButtonPressed", true);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("NumberActivity", "Inside onDestroy");
    }


}
