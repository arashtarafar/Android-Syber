package com.codersnest.lonenightingale.syber;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;


public class Details extends ActionBarActivity {

    private Context context;
    private SQLiteDatabase db;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView text;
    private TextView details;

    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();

        postId = getIntent().getExtras().getInt("postId");

        db = context.openOrCreateDatabase("database", context.MODE_PRIVATE, null);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Cursor cursor = db.rawQuery("SELECT user_id, text, first_name, last_name, time, date, number_of_likes, number_of_hash_tags, number_of_comments, number_of_shares FROM post NATURAL JOIN member NATURAL JOIN status WHERE post_id = '" + postId + "'", null);
        cursor.moveToFirst();

        text = (TextView) findViewById(R.id.text_post_content_details);
        details = (TextView) findViewById(R.id.indic_post_details);

        actionBar.setTitle(cursor.getString(cursor.getColumnIndex("text")));

        text.setText(cursor.getString(cursor.getColumnIndex("text")));
        details.setText("Written by " + cursor.getString(cursor.getColumnIndex("first_name")) + " " + cursor.getString(cursor.getColumnIndex("last_name")) + " on " + cursor.getString(cursor.getColumnIndex("date")) + " in " + cursor.getString(cursor.getColumnIndex("time")) + " with " + cursor.getInt(cursor.getColumnIndex("number_of_likes")) + " likes, " + cursor.getInt(cursor.getColumnIndex("number_of_comments")) + " comments and " + cursor.getInt(cursor.getColumnIndex("number_of_shares")) + " shares so far...");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_left);
                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = context.openOrCreateDatabase("database", context.MODE_PRIVATE, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
}
