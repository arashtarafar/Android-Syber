package com.codersnest.lonenightingale.syber;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class Dashboard extends ActionBarActivity {

    private Context context;
    private SQLiteDatabase db;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText input;
    private TextView title;

    private int userId;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();

        prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = prefs.edit();

        calendar = Calendar.getInstance();

        userId = getIntent().getExtras().getInt("id");

        db = context.openOrCreateDatabase("database", context.MODE_PRIVATE, null);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        input = (EditText) findViewById(R.id.field_status);
        title = (TextView) findViewById(R.id.title_dashboard);

        Cursor cursor = db.rawQuery("SELECT first_name, last_name FROM member WHERE user_id = '" + userId + "'", null);
        cursor.moveToFirst();
        title.setText(cursor.getString(cursor.getColumnIndex("first_name")) + " " + cursor.getString(cursor.getColumnIndex("last_name")) + "'s Syber");

        //        recyclerView = (RecyclerView) findViewById(R.id.listQuestions);
        mRecyclerView = (RecyclerView) findViewById(R.id.list_posts);

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

    private Status[] getPosts () {

        Status[] posts = null;
        int numberOfPosts = 0;

        Cursor cursor = db.rawQuery("SELECT text, first_name, last_name, time, date, number_of_likes, number_of_hash_tags, number_of_comments, number_of_shares FROM post NATURAL JOIN status NATURAL JOIN member", null);
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
            posts[iterator] = new Status(text, author, time, date, numberOfLikes, numberOfHashTags, numberOfComments, numberOfShares);
            cursor.moveToNext();
        }

        return posts;
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

    public void post (View v) {
        String text = input.getText().toString();

        if(!text.isEmpty()) {
            //        db.execSQL("INSERT INTO post VALUES ('" + prefs.getInt("post_id", 0) + "', '" + userId + "', '" + input.getText().toString() + "', '0', '0', '0', '0');");
            db.execSQL("INSERT INTO post (user_id, text, number_of_likes, date, time, number_of_hash_tags) VALUES ('" + userId + "', '" + text + "', '0', 'date()', 'time()', '0');");

//            Cursor cursor = db.rawQuery("SELECT post_id FROM post WHERE (user_id, text) = (" + userId + ", " + text + ")", null);
//            cursor.moveToFirst();

            input.setText("");


            //        db.execSQL("INSERT INTO status VALUES ('" + prefs.getInt("post_id", 0) + "', '0', '0');");
            db.execSQL("INSERT INTO status (post_id, number_of_comments, number_of_shares) SELECT post_id, 0, 0 FROM post WHERE user_id = " + userId + " AND text = \"" + text + "\";");
            //        editor.putInt("post_id", prefs.getInt("post_id", 0) + 1).commit();
            mAdapter = new ListAdapter(getPosts());
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(context, "Enter Text!", Toast.LENGTH_SHORT).show();
        }
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
                    .inflate(R.layout.status_item, parent, false);
            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            // code to assign data to different views inside the view_holder
            TextView tv = (TextView) holder.v.findViewById(R.id.text_post_content);
            TextView tv2 = (TextView) holder.v.findViewById(R.id.indic_details);
            tv.setText(mDataset[position].getText());
            tv2.setText("Written by " + mDataset[position].getAuthor() + " on " +  mDataset[position].getDate() + " in " +  mDataset[position].getTime() + " with " +  mDataset[position].getNumberOfLikes() + " likes, " +  mDataset[position].getNumberOfComments() + " comments and " +  mDataset[position].getNumberOfShares() + " shares so far...");
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }

        @Override
        public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            super.registerAdapterDataObserver(observer);
        }
    }
}
