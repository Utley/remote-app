package com.utley.android.remote;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<SeekBar> sensors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView helloworld = new TextView(this);
        helloworld.setText("hello world");
        LinearLayout l = (LinearLayout) findViewById(R.id.controls);
        l.addView(helloworld);

        Button button = new Button(this);
        button.setText("test");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new sendInfoAsync().execute("a", "b", "c");
            }
        });
        l.addView(button);

        SeekBar s = new SeekBar(this);
        sensors.add(s);
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                new sendInfoAsync().execute("default", String.valueOf(progress), "asdf");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        LayoutParams m = new LayoutParams(-1,-2);
        s.setLayoutParams(m);
        l.addView(s);

        addSensorControl("sensor 1");
        addSensorControl("sensor 2");


    }

    private class sendInfoAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://raspberrypi.home:3000/");

                HttpURLConnection h = (HttpURLConnection) url.openConnection();
                Log.i("info", url.getHost());
                Log.i("info", String.valueOf(url.getPort()));
                h.setDoOutput(true);
                h.setChunkedStreamingMode(0);
                h.setRequestMethod("POST");
                h.setRequestProperty("Content-Type", "application/json");
                h.setRequestProperty("Accept", "application/json");

                Permission p = h.getPermission();
                Log.i("info", "permission: " + p.getName());
                OutputStream out = h.getOutputStream();
                BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                JSONObject j = new JSONObject();
                j.put("sensor", params[0]);
                j.put("value",params[1]);
                w.write(j.toString());

                w.flush();
                w.close();


                h.connect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addSensorControl( final String name ){
        SeekBar s = new SeekBar(this);
        LinearLayout l = (LinearLayout) findViewById(R.id.controls);
        LayoutParams lparams = new LayoutParams(-1,-2);
        s.setLayoutParams(lparams);
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                new sendInfoAsync().execute( name, String.valueOf(progress), "asdf");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sensors.add(s);
        l.addView(s);

    }


}
