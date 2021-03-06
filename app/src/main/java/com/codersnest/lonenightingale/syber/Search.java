package com.codersnest.lonenightingale.syber;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Search extends ActionBarActivity {

    private Context context;
    private SQLiteDatabase db;

    private RecyclerView mRecyclerView, mRecyclerView2;
    private RecyclerView.Adapter mAdapter, mAdapter2;
    private RecyclerView.LayoutManager mLayoutManager, mLayoutManager2;

    private EditText searchBox;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpWindow();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // get the action bar
        ActionBar actionBar = getSupportActionBar();

        // Enabling Back navigation on Action Bar icon
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();
        db = context.openOrCreateDatabase("database", context.MODE_PRIVATE, null);

        searchBox = (EditText) findViewById(R.id.searchBox);
    }

    public void search(View v) {
        // Code to perform the search
    }

    public void setUpWindow() {

        // Creates the layout for the window and the look of it
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        // Params for the window.
        // You can easily set the alpha and the dim behind the window from here
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1.0f;    // lower than one makes it more transparent
        params.dimAmount = 0f;  // set it higher if you want to dim behind the window
        getWindow().setAttributes(params);

        // Gets the display size so that you can set the window to a percent of that
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        // You could also easily used an integer value from the shared preferences to set the percent
        if (height > width) {
            getWindow().setLayout((int) (width * .9), (int) (height * .9));
        } else {
            getWindow().setLayout((int) (width * .7), (int) (height * .8));
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }
}
