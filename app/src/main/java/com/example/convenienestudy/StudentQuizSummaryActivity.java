package com.example.convenienestudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StudentQuizSummaryActivity extends AppCompatActivity {

    private Quiz quiz;
    private Assignment assignment;
    private Button endOfQuizButton;
    private TextView showScore;
    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference assignmentRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("listOfAssignment");
    private ImageView fireworks;
    private AnimationDrawable fireworks_anim;
    private TextView summary_header;
    private Toolbar toolbar;


    @Override
    protected void onStart() {
        super.onStart();
        fireworks = findViewById(R.id.fireworks);
        fireworks.setBackgroundResource(R.drawable.fireworks_anim);
        fireworks_anim = (AnimationDrawable) fireworks.getBackground();
        fireworks_anim.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz_summary);
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        Intent getIntent = getIntent();
        quiz = getIntent.getExtras().getParcelable("quizObject");
        summary_header = findViewById(R.id.summary_header);
        summary_header.setText("SUMMARY FOR " + quiz.getTitle());

        assignment = getIntent.getExtras().getParcelable("assignmentObject");

        assignment.setCompleted(true);

        showScore = (TextView) findViewById(R.id.quiz_summary_score);
        endOfQuizButton = (Button) findViewById(R.id.quiz_summary_button);

        showScore.setText(String.valueOf(assignment.getScore()));

        endOfQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignmentRef.child(assignment.getQuizNumber()).child("completed").setValue(true);
                assignmentRef.child(assignment.getQuizNumber()).setValue(assignment);
                startActivity(new Intent(StudentQuizSummaryActivity.this, StudentMainActivity.class));
            }
        });



    }

    @Override
    public void onBackPressed() {
        Toast.makeText(StudentQuizSummaryActivity.this, "Congratulations! You have reached the end", Toast.LENGTH_SHORT);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                startActivity(new Intent(StudentQuizSummaryActivity.this, LoginActivity.class));
                return true;

            case R.id.return_home:
                startActivity(new Intent(StudentQuizSummaryActivity.this, StudentMainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}