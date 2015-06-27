package com.codersnest.lonenightingale.syber;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;


public class Profile extends ActionBarActivity {

    private Context context;
    private SQLiteDatabase db;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setUpWindow();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();

        userId = getIntent().getExtras().getInt("userId");

        db = context.openOrCreateDatabase("database", context.MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT first_name, last_name FROM member WHERE user_id = '" + userId + "'", null);
        cursor.moveToFirst();

        actionBar.setTitle(cursor.getString(cursor.getColumnIndex("first_name")) + " " + cursor.getString(cursor.getColumnIndex("last_name")) + "'s Profile");

        mRecyclerView = (RecyclerView) findViewById(R.id.list_posts_profile);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
//        adapter = new CardListAdapter(getApplicationContext(), getData());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        mAdapter = new ListAdapter(getPosts());
        mRecyclerView.setAdapter(mAdapter);
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

    private Status[] getPosts () {

        Status[] posts = null;
        int numberOfPosts = 0;

        Cursor cursor = db.rawQuery("SELECT text, first_name, last_name, time, date, number_of_likes, number_of_hash_tags, number_of_comments, number_of_shares FROM post NATURAL JOIN status NATURAL JOIN member WHERE user_id = '" + userId + "' ORDER BY date, time DESC", null);
        numberOfPosts = cursor.getCount();
        cursor.moveToFirst();

        posts = new Status[numberOfPosts];
        for (int iterator = 0; iterator < numberOfPosts; iterator ++) {
            String text = cursor.getString(cursor.getColumnIndex("text"));
            String author = cursor.getString(cursor.getColumnIndex("first_name")) + " " + cursor.getString(cursor.getColumnIndex("last_name"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            int numberOfLikes = cursor.getInt(cursor.getColumnIndex("number_of_likes"));
            int numberOfHashTags = cursor.getInt(cursor.getColumnIndex("number_of_hash_tags"));
            int numberOfComments = cursor.getInt(cursor.getColumnIndex("number_of_comments"));
            int numberOfShares = cursor.getInt(cursor.getColumnIndex("number_of_shares"));
            posts[iterator] = new Status(userId, text, author, time, date, numberOfLikes, numberOfHashTags, numberOfComments, numberOfShares);
            cursor.moveToNext();
        }

        return posts;
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
            getWindow().setLayout((int) (width * .9), (int) (height * .7));
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

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
        private Status[] mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {

            // define views that are inside the view_holder : public
            public View v;
//            public TextView text;
//            public TextView details;

            public ViewHolder(View v) {
                super(v);
                this.v = v;

                // initialize views inside the view_holder
//                text = (TextView) findViewById(R.id.text_post_content);
//                details = (TextView) findViewById(R.id.indic_details);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ListAdapter(Status[] posts) {
            mDataset = posts;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.status_profile, parent, false);
            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            // code to assign data to different views inside the view_holder
            TextView tv = (TextView) holder.v.findViewById(R.id.text_post_content_profile);
            TextView tv2 = (TextView) holder.v.findViewById(R.id.indic_details_profile);
            tv.setText(mDataset[position].getText());
            tv2.setText("Written on " + mDataset[position].getDate() + " in " + mDataset[position].getTime() + " with " + mDataset[position].getNumberOfLikes() + " likes, " + mDataset[position].getNumberOfComments() + " comments and " + mDataset[position].getNumberOfShares() + " shares so far...");
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
}