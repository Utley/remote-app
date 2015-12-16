package com.utley.android.remote;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by stephenutley on 12/15/15.
 */
public class Control extends LinearLayout {
    SeekBar s;
    String name = "";
    TextView label;

    public Control(Context context) {
        super(context);

        label = new TextView(context);

        s = new SeekBar(context);
        LayoutParams lparams = new LayoutParams(-1, -2);
        s.setLayoutParams(lparams);
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //limit the amount of data sent
                if (progress % 5 == 0) {
                    new SendInfoAsync().execute(name, String.valueOf(progress), "asdf");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setName(String str) {
        this.name = str;
        this.label.setText(this.name);
    }

    public void addTo(LinearLayout l) {
        l.addView(s);
        l.addView(label);
    }
}
