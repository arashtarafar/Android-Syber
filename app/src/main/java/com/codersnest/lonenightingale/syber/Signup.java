package com.codersnest.lonenightingale.syber;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Signup extends ActionBarActivity {

    private Context context;
    private SQLiteDatabase db;

    private EditText first_name;
    private EditText last_name;
    private EditText username;
    private EditText password;
    private EditText email;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = getApplicationContext();

        prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = prefs.edit();

        db = context.openOrCreateDatabase("database", context.MODE_PRIVATE, null);

        first_name = (EditText) findViewById(R.id.field_first_name);
        last_name = (EditText) findViewById(R.id.field_last_name);
        username = (EditText) findViewById(R.id.field_username);
        password = (EditText) findViewById(R.id.field_password);
        email = (EditText) findViewById(R.id.field_email);
    }

    public void register (View v) {
        try {
//            db.execSQL("INSERT INTO member VALUES ('" + prefs.getInt("user_id", 0) + "', '" + first_name.getText().toString() + "', '" + last_name.getText().toString() + "', '" + email.getText().toString() + "', '" + username.getText().toString() + "', '" + password.getText().toString() + "', '1/1/2015', '0', '0', '0', '0', '0', '0', '0', '0');");
//            editor.putInt("user_id", prefs.getInt("user_id", 0) + 1).commit();
            db.execSQL("INSERT INTO member (first_name, last_name, email, username, password, date_of_registration, total_likes, total_comments, total_mentioned, total_following, total_followers, avg_post_count, number_of_posts, number_of_messages_in_inbox) VALUES ('" + first_name.getText().toString() + "', '" + last_name.getText().toString() + "', '" + email.getText().toString() + "', '" + username.getText().toString() + "', '" + password.getText().toString() + "', '1/1/2015', '0', '0', '0', '0', '0', '0', '0', '0');");
            editor.putString("lastUsername", username.getText().toString());
        }
        catch (Exception e) {
            Toast.makeText(context, "Sign-Up Failed!", Toast.LENGTH_LONG).show();
        }
        finish();
        overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_left);
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
    protected void onStop() {
        super.onStop();
        db.close();
    }
}
