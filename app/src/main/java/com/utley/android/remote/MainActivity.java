package com.utley.android.remote;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
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

    ArrayMap<String, SeekBar> controls = new ArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView helloworld = new TextView(this);
        helloworld.setText("hello world");
        LinearLayout l = (LinearLayout) findViewById(R.id.controls);
        l.addView(helloworld);

        addControl("Control 1");
        addControl("Control 2");
        addControl("Control 3");

        Preset p = new Preset();
        p.setControl("Control 1",30f);
        p.setControl("Control 2",20f);
        p.setControl("control 3",50f);
        p.activate();
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
                h.setRequestProperty("Content-Type", "text");

                Permission p = h.getPermission();
                OutputStream out = h.getOutputStream();
                BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
                w.write(params[0]+":"+params[1]);

                w.flush();
                w.close();


                h.connect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //basic class for setting all values at once
    //e.g. turning lights out
    //currently untested
    private class Preset {

        private ArrayMap<String, Float> values;
        public Preset(){
            values = new ArrayMap<>();
        }
        public void setControl(String name, float val){
            values.put(name, val);
        }
        public void deleteControl(String name){
            values.remove(name);
        }
        public void activate(){
            for( String val : values.keySet()){
                if(controls.containsKey(val)){
                    int progress = Float.floatToIntBits(values.get(val));
                    controls.get(val).setProgress( progress );
                }

            }
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

    private void addControl( final String name ){
        SeekBar s = new SeekBar(this);
        LinearLayout l = (LinearLayout) findViewById(R.id.controls);
        LayoutParams lparams = new LayoutParams(-1,-2);
        s.setLayoutParams(lparams);
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress % 5 == 0) {
                    new sendInfoAsync().execute(name, String.valueOf(progress), "asdf");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        controls.put(name,s);
        l.addView(s);

    }


}
