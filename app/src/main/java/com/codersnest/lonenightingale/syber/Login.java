package com.codersnest.lonenightingale.syber;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends ActionBarActivity {

    private Context context;
    private SQLiteDatabase db;

    private EditText username;
    private EditText password;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();

        prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = prefs.edit();

        db = context.openOrCreateDatabase("database", context.MODE_PRIVATE, null);

        // Tables for members
        db.execSQL("CREATE TABLE IF NOT EXISTS member (user_id INTEGER PRIMARY KEY AUTOINCREMENT, first_name VARCHAR, last_name VARCHAR, email VARCHAR, username VARCHAR, password VARCHAR, date_of_registration DATE, total_likes INT, total_comments INT, total_mentioned INT, total_following INT, total_followers INT, avg_post_count INT, number_of_posts INT, number_of_messages_in_inbox INT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS moderator (user_id INT, FOREIGN KEY (user_id) REFERENCES member(user_id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS analyzer (user_id INT, FOREIGN KEY (user_id) REFERENCES moderator(user_id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS admin (user_id INT, FOREIGN KEY (user_id) REFERENCES analyzer(user_id));");

        // Tables for posts
        db.execSQL("CREATE TABLE IF NOT EXISTS post (post_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INT, text VARCHAR, number_of_likes INT, date DATE, time TIME, date_time DATETIME, number_of_hash_tags INT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS status (post_id INTEGER PRIMARY KEY, number_of_comments INT, number_of_shares INT, FOREIGN KEY (post_id) REFERENCES post(post_id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS comment (status_id INT, post_id INTEGER PRIMARY KEY, number_of_replies INT, FOREIGN KEY (post_id) REFERENCES post(post_id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS reply (comment_id INT, post_id INTEGER PRIMARY KEY, FOREIGN KEY (post_id) REFERENCES post(post_id));");

        // Tables for relations
        db.execSQL("CREATE TABLE IF NOT EXISTS follower (id_follower INT, id_followed INT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS blocked (id_blocker INT, id_blocked INT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS messages (id_sender INT, id_receiver INT, text VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS shares (member_id INT, post_id INT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS likes (member_id INT, post_id INT, PRIMARY KEY (member_id, post_id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS reply_to_reply (replied_to_id INT, reply_id INT, depth INT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS hash_tag (text VARCHAR, number_of_repeats INT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS post_tags (post_id INT, text VARCHAR);");

//        db.execSQL("INSERT INTO ___ VALUES ('', '', ...);");

//        Cursor cursor = db.rawQuery("SELECT ___ FROM ___", null); cursor.moveToFirst(); cursor.moveToNext();

        username = (EditText) findViewById(R.id.field_login_uname);
        password = (EditText) findViewById(R.id.field_login_passwd);

        username.setText(prefs.getString("lastUsername", ""));
        if(!prefs.getString("lastUsername", "").equals(""))
            password.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = context.openOrCreateDatabase("database", context.MODE_PRIVATE, null);
        password.setText("");
    }

    public void login (View v) {
        Cursor cursor;
        boolean found = false;

        String username_text = username.getText().toString();
        String password_text = password.getText().toString();

        cursor = db.rawQuery("SELECT user_id FROM member WHERE username = '" + username_text + "' AND password = '" + password_text + "'", null);

        cursor.moveToFirst();

        if(cursor.getCount() == 1) {
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            intent.putExtra("id", cursor.getInt(0));
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
        }
    }

    public void signup (View v) {
        Intent intent = new Intent(getApplicationContext(), Signup.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.putString("lastUsername", username.getText().toString()).commit();
        db.close();
    }
}
