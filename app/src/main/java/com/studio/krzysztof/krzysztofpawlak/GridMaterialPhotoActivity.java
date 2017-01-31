package com.studio.krzysztof.krzysztofpawlak;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class GridMaterialPhotoActivity extends Activity {

//    static String BASE_SERVER_URL = "http://127.0.0.1:8080";
//    static String BASE_SERVER_URL = "http://10.0.2.2:8080";
    static String BASE_SERVER_URL = "http://sunpatrol.pe.hu";

    final HFAdapter hfAdapter = new HFAdapter(this);

    private RecyclerView mRecyclerView;
    private GridLayoutManager manager;
    private View footerView;

    private Gson gson;
    private AsyncHttpClient client;
    private Response responseObject;

    private int nrJSONFile = 0;
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private int columns;
    private boolean loading = true;
    private long startTime, stopTime, difference;
    private double sec;
    private String url;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                final SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                getPrefs.edit().putBoolean("checkbox", false).commit();
                slideTransition();
                finish();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_material_grid);

        client = new AsyncHttpClient();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(hfAdapter);

        if (isTablet(getApplicationContext())) {
            columns = 4;
        } else {
            columns = 2;
        }

        manager = new GridLayoutManager(this, columns);

        mRecyclerView.setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if ((hfAdapter.hasHeader() && hfAdapter.isHeader(position)) ||
                        hfAdapter.hasFooter() && hfAdapter.isFooter(position))
                    return manager.getSpanCount();
                return 1;
            }
        });

        footerView = LayoutInflater.from(this).inflate(R.layout.footer_view, mRecyclerView, false);

        setupWindowAnimation();

        takeData();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = manager.getItemCount();
                firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    nrJSONFile++;
                    takeData();
                    loading = true;
                }
            }
        });
    }

    // from Google I/O 2013 app source code, maybe now not working
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private Response takeData() {
        url = BASE_SERVER_URL + "/page_" + nrJSONFile + ".json";

        client.get(GridMaterialPhotoActivity.this, url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString = new String(responseBody);

                if (!responseString.contains("Error 404 - Page Not Found")) {
                    gson = new Gson();
                    responseObject = gson.fromJson(responseString, Response.class);

                    if (nrJSONFile == 0) {
                        hfAdapter.setData(responseObject.getArray());
                    } else {
                        stopTime = System.nanoTime();
                        difference = stopTime - startTime;
                        sec = (double) difference / 1000000000.0;

                        if (sec < 1.0) { // only for show progress bar when too fast
                            Handler handlerTimer = new Handler();
                            handlerTimer.postDelayed(new Runnable() {
                                public void run() {
                                    hfAdapter.removeFooter();
                                    hfAdapter.addData(responseObject.getArray());
                                }
                            }, 2000);
                        } else {
                            hfAdapter.removeFooter();
                            hfAdapter.addData(responseObject.getArray());
                        }
                    }
                } else {

                }
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                hfAdapter.setFooterView(footerView);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onStart() {
                super.onStart();
                startTime = System.nanoTime();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

        return responseObject;
    }

    private void setupWindowAnimation() {
        Fade explode = new Fade();
        explode.setDuration(3000);
        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);
        getWindow().setAllowReturnTransitionOverlap(false);
    }
    private void slideTransition() {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideTrans);
        intent.putExtra(Constants.KEY_TITLE, "Slide");
        startActivity(intent, options.toBundle());
    }
}


