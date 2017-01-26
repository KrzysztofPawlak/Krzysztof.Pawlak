package com.studio.krzysztof.krzysztofpawlak;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotoActivity extends AppCompatActivity {

    ListView listView;
    GridView gridView;
    Response responseObject;
    CustomAdapter adapter;
    //    static String BASE_SERVER_URL = "http://127.0.0.1:8080/Android_2017_zadanie_3_dane/page_0.json";
//    static String BASE_SERVER_URL = "http://10.0.2.2:8080/Android_2017_zadanie_3_dane/page_0.json";
    static String BASE_SERVER_URL = "http://sunpatrol.pe.hu";

    public Handler mHandler;
    public View ftView;
    public boolean isLoading = false;
    int nrJSONFile = 0;

    Gson gson;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_photo);
        setContentView(R.layout.grid_view_2);
//        listView = (ListView) findViewById(R.id.list);
        gridView = (GridView) findViewById(R.id.gridview2);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(PhotoActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//
//            }
//
//            @Override // tu musi byc pobranie nowego pliku
//            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                if (absListView.getLastVisiblePosition() == i2 - 1 && listView.getCount() >= 10 && isLoading == false) {
//                    isLoading = true;
//                    nrJSONFile++;
//                    mHandler.sendEmptyMessage(0);
//                    takeData();
//                }
//            }
//        });

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.footer_view, null);
        mHandler = new MyHandler();

//        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.myProgressBar);
//        progressBar.setVisibility(View.GONE);

        client = new AsyncHttpClient();

        responseObject = takeData();
    }

    private Response takeData() {
        String url = BASE_SERVER_URL + "/page_" + nrJSONFile + ".json";
        client.get(PhotoActivity.this, url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);

                if (!responseString.contains("Error 404 - Page Not Found")) {
                    gson = new Gson();
                    responseObject = gson.fromJson(responseString, Response.class);
                    if (nrJSONFile == 0) {
//                        adapter = new CustomAdapter(PhotoActivity.this, responseObject.getArray());
//                        listView.setAdapter(adapter);

                        adapter = new CustomAdapter(PhotoActivity.this, responseObject.getArray());
                        gridView.setAdapter(adapter);
                    } else {
//                        Message msg = mHandler.obtainMessage(1, responseObject.getArray());
//                        mHandler.sendMessage(msg);
                    }
                }
//                mHandler.sendEmptyMessage(2);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
//                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
//                progressBar.setVisibility(View.GONE);
            }
        });

        return responseObject;
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    listView.addFooterView(ftView);
                    break;
                case 1:
                    adapter.addListItemToAdapter((ArrayList<Response.ArrayBean>) msg.obj);
                    isLoading = false;
                    break;
                case 2:
                    listView.removeFooterView(ftView);
                    break;
                default:
                    break;
            }
        }
    }
}
