package com.utley.android.remote;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;

public class MainActivity extends AppCompatActivity {

    ArrayMap<String, SeekBar> controls = new ArrayMap<>();
    String protocol="http";
    String piName = "raspberrypi.home";
    String portNumber = "3000";
    String piUrl = protocol+"://"+piName+":"+portNumber+"/"; //"http://raspberrypi.home:3000/";

    LinearLayout l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView helloworld = new TextView(this);
        helloworld.setText("hello world");
        l = (LinearLayout) findViewById(R.id.controls);
        l.addView(helloworld);

        addControl("Control 1");
        addControl("Control 2");
        addControl("Control 3");

        final Preset p = new Preset();
        p.setControl("Control 1",30f);
        p.setControl("Control 2",20f);
        p.setControl("control 3", 50f);
        p.activate();

        Button b = new Button(this);
        b.setText("preset 1");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.activate();
            }
        });
        l.addView(b);

        Button c = (Button) findViewById(R.id.button);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addControl("working, probably");
            }
        });

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
                    int progress = values.get(val).intValue();
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
        Control c = new Control(this);
        c.setName(name);
        c.addTo(l);

    }


}
