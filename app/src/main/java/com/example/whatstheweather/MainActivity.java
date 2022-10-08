package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {
    EditText editText ;
    TextView resultView ;
    RequestQueue requestQueue ;
    public void findWeather(View view)
    {
        requestQueue =  Volley.newRequestQueue(this);
        editText = findViewById(R.id.editcity);
        resultView = findViewById(R.id.result);
        InputMethodManager mg = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mg.hideSoftInputFromWindow(editText.getWindowToken(),0);
        JsonObjectRequest jsonObjectRequest ;
        try {
            String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");


            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=9113e68173a3a773b70607fa902224e2", null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String weatherinfo = response.getString("weather");
                        Log.d("weather", weatherinfo);
                        JSONArray jsonArray = new JSONArray(weatherinfo);
                        String message = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String main = jsonObject.getString("main");
                            String description = jsonObject.getString("description");

                            if (!main.equals("") && !description.equals("")) {
                                message += main + " : " + description + "\r\n";
                            }
                        }
                        if (!message.equals("")) {
                            resultView.setText(message);
                        } else {
                            Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_SHORT).show();
                    Log.d("weather", "Error");
                }
            });
            requestQueue.add(jsonObjectRequest)  ;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}