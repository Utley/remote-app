package com.utley.android.remote;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by stephenutley on 12/15/15.
 */
public class SendInfoAsync extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        try {
            String defaultURL = "raspberrypi.home";
            URL url = new URL(defaultURL);

            HttpURLConnection h = (HttpURLConnection) url.openConnection();
            h.setDoOutput(true);
            h.setChunkedStreamingMode(0);
            h.setRequestMethod("POST");
            h.setRequestProperty("Content-Type", "text");

            OutputStream out = h.getOutputStream();
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            w.write(params[0] + ":" + params[1]);

            w.flush();
            w.close();


            h.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

