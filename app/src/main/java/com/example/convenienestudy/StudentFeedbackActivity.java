package com.example.convenienestudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StudentFeedbackActivity extends AppCompatActivity {

    private Assignment assignment;
    private TextView feedbackText;
    private Button backHomeButton;
    private TextView feedback_header;
    private String quizTitle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_feedback);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        Intent getIntent = getIntent();
        quizTitle = getIntent.getExtras().getString("quizTitle");
        assignment = getIntent.getExtras().getParcelable("assignmentObject");


        feedback_header = findViewById(R.id.feedback_header);
        feedback_header.setText("FEEDBACK FOR " + quizTitle.toUpperCase());


        String feedback = assignment.getFeedback();

        feedbackText = (TextView) findViewById(R.id.student_feedback);
        backHomeButton = (Button) findViewById(R.id.feedback_backHome_button);

        feedbackText.setText(feedback);

        backHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentFeedbackActivity.this, StudentMainActivity.class));
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                startActivity(new Intent(StudentFeedbackActivity.this, LoginActivity.class));
                return true;
            case R.id.return_home:
                startActivity(new Intent(StudentFeedbackActivity.this, StudentMainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}