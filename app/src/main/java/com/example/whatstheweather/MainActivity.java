package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    public void checkWeather(View view) {
        try {
            String cityName = editText.getText().toString();
            DownloadTask task = new DownloadTask();
            String encodedString = URLEncoder.encode(cityName, "UTF-8");
            task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encodedString + "&APPID=953e95aec6a1a5fec9e3095beb1f2c81");
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Coumtt not find weather",Toast.LENGTH_SHORT).show();
        }
    }
    class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();
                InputStream is = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                int data = reader.read();
                while (data != -1) {
                    char c = (char) data;
                    result += c;
                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find",Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray jsonArray = new JSONArray(weatherInfo);
                String main="",desc=""; String message="";
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                     main = jsonPart.getString("main");
                     desc = jsonPart.getString("description");
                }
                if(!(main =="") && !(desc =="")){
                    message+= main+" : "+desc+" \n";
                }
                if(!(message =="")){
                    textView.setText(message);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Could not Find Weather",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"dasdasda",Toast.LENGTH_SHORT).show();
            }


        }
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            editText = findViewById(R.id.editText);
            textView = findViewById(R.id.textView);
        }

}