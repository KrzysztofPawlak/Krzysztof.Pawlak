package com.studio.krzysztof.krzysztofpawlak;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class PhotoActivity extends AppCompatActivity {

    ListView listView;
    Response responseObject;
    CustomAdapter adapter;
//    static String BASE_SERVER_URL = "http://127.0.0.1:8080/Android_2017_zadanie_3_dane/page_0.json";
//    static String BASE_SERVER_URL = "http://10.0.2.2:8080/Android_2017_zadanie_3_dane/page_0.json";
    static String BASE_SERVER_URL = "http://sunpatrol.pe.hu/page_0.json";

    Gson gson;
    AsyncHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        listView = (ListView) findViewById(R.id.list);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.myProgressBar);
        client = new AsyncHttpClient();
        client.get(PhotoActivity.this, BASE_SERVER_URL, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);
                gson = new Gson();
                responseObject = gson.fromJson(responseString, Response.class);
                adapter = new CustomAdapter(PhotoActivity.this, responseObject.getArray());
                listView.setAdapter(adapter);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
