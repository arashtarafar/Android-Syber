package com.codersnest.lonenightingale.syber;

import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Details extends ActionBarActivity {

    private Context context;
    private SQLiteDatabase db;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView text;
    private TextView details;
    private TextView indicNoComments;

    private EditText input;

    private int postId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpWindow();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();

        userId = getIntent().getExtras().getInt("userId");
        postId = getIntent().getExtras().getInt("postId");

        db = context.openOrCreateDatabase("database", context.MODE_PRIVATE, null);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Cursor cursor = db.rawQuery("SELECT user_id, text, first_name, last_name, time, date, number_of_likes, number_of_hash_tags, number_of_comments, number_of_shares FROM post NATURAL JOIN member NATURAL JOIN status WHERE post_id = '" + postId + "'", null);
        cursor.moveToFirst();

        text = (TextView) findViewById(R.id.text_post_content_details);
        details = (TextView) findViewById(R.id.indic_post_details);
        indicNoComments = (TextView) findViewById(R.id.indic_no_comments);

        input = (EditText) findViewById(R.id.field_comment);

        actionBar.setTitle(cursor.getString(cursor.getColumnIndex("text")));

        text.setText(cursor.getString(cursor.getColumnIndex("text")));
        details.setText("Written by " + cursor.getString(cursor.getColumnIndex("first_name")) + " " + cursor.getString(cursor.getColumnIndex("last_name")) + " on " + cursor.getString(cursor.getColumnIndex("date")) + " in " + cursor.getString(cursor.getColumnIndex("time")) + " with " + cursor.getInt(cursor.getColumnIndex("number_of_likes")) + " likes, " + cursor.getInt(cursor.getColumnIndex("number_of_comments")) + " comments and " + cursor.getInt(cursor.getColumnIndex("number_of_shares")) + " shares so far...");

        mRecyclerView = (RecyclerView) findViewById(R.id.list_comments);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
//        adapter = new CardListAdapter(getApplicationContext(), getData());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        mAdapter = new ListAdapter(getComments());
        mRecyclerView.setAdapter(mAdapter);
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

    public void comment (View v) {
        String text = input.getText().toString();

        if(!text.isEmpty()) {
            //        db.execSQL("INSERT INTO post VALUES ('" + prefs.getInt("post_id", 0) + "', '" + userId + "', '" + input.getText().toString() + "', '0', '0', '0', '0');");
            db.execSQL("INSERT INTO post (user_id, text, number_of_likes, date, time, date_time, number_of_hash_tags) VALUES ('" + userId + "', '" + text + "', '0', date(), time(), datetime(), '0');");

            Cursor cursor = db.rawQuery("SELECT post_id FROM post WHERE user_id = '" + userId + "' AND text = '" + text + "'", null);
            cursor.moveToFirst();

            input.setText("");


            //        db.execSQL("INSERT INTO status VALUES ('" + prefs.getInt("post_id", 0) + "', '0', '0');");
            db.execSQL("INSERT INTO comment (status_id, post_id, number_of_replies) SELECT " + postId + ", " + cursor.getInt(cursor.getColumnIndex("post_id")) + ", 0 FROM post WHERE user_id = " + userId + " AND text = \"" + text + "\";");
            // You know what mistake you made here!

            //        editor.putInt("post_id", prefs.getInt("post_id", 0) + 1).commit();

            // Inefficient method for updating the list of posts
            updateDatabase();
        }
        else {
            Toast.makeText(context, "Enter Text!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDatabase() {
        mAdapter = new ListAdapter(getComments());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void updateDetails() {
        Cursor cursor = db.rawQuery("SELECT user_id, text, first_name, last_name, time, date, number_of_likes, number_of_hash_tags, number_of_comments, number_of_shares FROM post NATURAL JOIN member NATURAL JOIN status WHERE post_id = '" + postId + "'", null);
        cursor.moveToFirst();
        details.setText("Written by " + cursor.getString(cursor.getColumnIndex("first_name")) + " " + cursor.getString(cursor.getColumnIndex("last_name")) + " on " + cursor.getString(cursor.getColumnIndex("date")) + " in " + cursor.getString(cursor.getColumnIndex("time")) + " with " + cursor.getInt(cursor.getColumnIndex("number_of_likes")) + " likes, " + cursor.getInt(cursor.getColumnIndex("number_of_comments")) + " comments and " + cursor.getInt(cursor.getColumnIndex("number_of_shares")) + " shares so far...");
    }

    private Comment[] getComments () {

        Comment[] comments = null;
        int numberOfComments = 0;

        Cursor cursor = db.rawQuery("SELECT post_id, user_id, text, first_name, last_name, time, date, date_time, number_of_likes, number_of_hash_tags, number_of_replies FROM post NATURAL JOIN comment NATURAL JOIN member WHERE status_id = '" + postId + "' ORDER BY date_time DESC", null);
        numberOfComments = cursor.getCount();
        cursor.moveToFirst();

        if(numberOfComments != 0)
            indicNoComments.setVisibility(View.GONE);

        comments = new Comment[numberOfComments];
        for (int iterator = 0; iterator < numberOfComments; iterator ++) {
            String text = cursor.getString(cursor.getColumnIndex("text"));
            String author = cursor.getString(cursor.getColumnIndex("first_name")) + " " + cursor.getString(cursor.getColumnIndex("last_name"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            int postId = cursor.getInt(cursor.getColumnIndex("post_id"));
            int authorId = cursor.getInt(cursor.getColumnIndex("user_id"));
            int numberOfLikes = cursor.getInt(cursor.getColumnIndex("number_of_likes"));
            int numberOfHashTags = cursor.getInt(cursor.getColumnIndex("number_of_hash_tags"));
            int numberOfReplies = cursor.getInt(cursor.getColumnIndex("number_of_replies"));
            comments[iterator] = new Comment(postId, authorId, text, author, time, date, numberOfLikes, numberOfHashTags, numberOfReplies);
            cursor.moveToNext();
        }

        return comments;
    }

    public void like(View v) {
        try {
            db.execSQL("INSERT INTO likes VALUES (" + userId + ", " + postId + ");");
            db.execSQL("UPDATE post SET number_of_likes = number_of_likes + 1 WHERE post_id = " + postId + ";");
            updateDetails();
        }
        catch (Exception e) {
            db.execSQL("DELETE FROM likes WHERE member_id = " + userId + " AND post_id = " + postId + ";");
            db.execSQL("UPDATE post SET number_of_likes = number_of_likes - 1 WHERE post_id = " + postId + ";");
            Toast.makeText(context, "Post Un-Liked!!", Toast.LENGTH_SHORT).show();
            updateDetails();
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

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
        private Comment[] mDataset;

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
        public ListAdapter(Comment[] comments) {
            mDataset = comments;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_item, parent, false);
            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            // code to assign data to different views inside the view_holder
            TextView tv = (TextView) holder.v.findViewById(R.id.text_comment_content);
            TextView tv2 = (TextView) holder.v.findViewById(R.id.indic_comment_details);

            tv.setText(mDataset[position].getText());
            tv2.setText("Written by " + mDataset[position].getAuthor() + " on " + mDataset[position].getDate() + " in " + mDataset[position].getTime() + " with " + mDataset[position].getNumberOfLikes() + " likes and " + mDataset[position].getNumberOfReplies() + " replies so far...");
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
}
