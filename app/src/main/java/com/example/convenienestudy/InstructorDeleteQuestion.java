package com.example.convenienestudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class InstructorDeleteQuestion extends AppCompatActivity {
    //TODO DELETE THE SELECTED QUESTION
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_delete_question);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                startActivity(new Intent(InstructorDeleteQuestion.this, LoginActivity.class));
                return true;
            case R.id.return_home:
                startActivity(new Intent(InstructorDeleteQuestion.this, InstructorMainActivity.class));
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}