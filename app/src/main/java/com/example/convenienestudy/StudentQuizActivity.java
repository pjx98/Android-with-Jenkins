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

import java.util.ArrayList;

public class StudentQuizActivity extends AppCompatActivity {

    private TextView quizTitle, quizScore, quizDescription;
    private Quiz quiz;
    private Assignment assignment;
    private Button startButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }


        quizTitle = (TextView) findViewById(R.id.quiz_start_title);
        quizScore = (TextView) findViewById(R.id.quiz_start_score);
        quizDescription = (TextView) findViewById(R.id.quiz_start_description);
        startButton = (Button) findViewById(R.id.start_quiz_button);



        Intent getIntent = getIntent();
        quiz = getIntent.getExtras().getParcelable("quizObject");
        assignment = getIntent.getExtras().getParcelable("assignmentObject");

        quizTitle.setText(quiz.getTitle().toUpperCase());
        quizScore.setText("Max Score: " + quiz.getTotalScore());
        quizDescription.setText(quiz.getDescription());

        if(assignment.isCompleted()){
            startButton.setText("REVIEW FEEDBACK");
            startButton.setOnClickListener(feedbackListener);
        }
        else{
            startButton.setOnClickListener(startQuestionsListener);
        }
    }

    View.OnClickListener startQuestionsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StudentQuizActivity.this, StudentAnswerQuizActivity.class);
            intent.putExtra("quizObject", quiz);
            intent.putExtra("assignmentObject", assignment);
            startActivity(intent);
        }
    };

    View.OnClickListener feedbackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StudentQuizActivity.this, StudentFeedbackActivity.class);
            intent.putExtra("quizTitle", quiz.getTitle());
            intent.putExtra("assignmentObject", assignment);
            startActivity(intent);
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                startActivity(new Intent(StudentQuizActivity.this, LoginActivity.class));
                return true;

            case R.id.return_home:
                startActivity(new Intent(StudentQuizActivity.this, StudentMainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}