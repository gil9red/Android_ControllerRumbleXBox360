package com.org.controllerrumblexbox360;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainActivity extends Activity {

    private String url = "http://192.168.0.102:10000";
    private ToggleButton toggleButton_Enabled;
    private SeekBar seekBar_LeftMotor;
    private SeekBar seekBar_RightMotor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton_Enabled = (ToggleButton) findViewById(R.id.toggleButton_Enabled);
        toggleButton_Enabled.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendStatus();
            }
        });

        seekBar_LeftMotor = (SeekBar) findViewById(R.id.seekBar_LeftMotor);
        seekBar_LeftMotor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sendStatus();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar_RightMotor = (SeekBar) findViewById(R.id.seekBar_RightMotor);
        seekBar_RightMotor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sendStatus();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void sendStatus() {
        JSONObject data = new JSONObject();
        try {
            data.put("enabled", toggleButton_Enabled.isChecked());
            data.put("left_motor", seekBar_LeftMotor.getProgress());
            data.put("right_motor", seekBar_RightMotor.getProgress());

            String json = data.toString();
            ((TextView) findViewById(R.id.textView_LOG)).setText("set_status " + json);

            new AsyncRequest().execute(json);

        } catch (Exception e) {
            ((TextView) findViewById(R.id.textView_LOG)).setText("error " + e);
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("controllerrumblexbox360", e.toString(), e);
        }
    }

    class AsyncRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... json) {
            MediaType JSONType = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSONType, json[0]);
            Request request = new Request.Builder()
                    .url(url + "/set_status")
                    .post(body)
                    .build();

            try {
                OkHttpClient client = new OkHttpClient();
                return client.newCall(request).execute().body().toString();

            } catch (Exception e) {
                Log.e("controllerrumblexbox360", e.toString(), e);
            }

            return "";
        }
    }
}
