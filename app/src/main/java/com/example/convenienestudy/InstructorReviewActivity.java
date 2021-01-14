package com.example.convenienestudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InstructorReviewActivity extends AppCompatActivity {

    private RecyclerView questionsRV;
    private RecyclerViewAdapterInstructorFeedbackQuestions questionsAdapter;
    private Assignment assignment;
    private EditText feedbackEdit;
    private Button feedbackSubmit;
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
    private String feedback, studentUserId;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_review);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        feedbackSubmit = (Button) findViewById(R.id.feedback_submitButton);
        feedbackEdit = (EditText) findViewById(R.id.feedback_editText);

        Intent getIntent = getIntent();
        studentUserId =  getIntent().getExtras().getString("studentUserId");
        assignment = getIntent.getExtras().getParcelable("assignmentObject");

        questionsRV = (RecyclerView) findViewById(R.id.instructorStudentsQuiz_recyclerview_id);
        questionsAdapter = new RecyclerViewAdapterInstructorFeedbackQuestions(this, assignment.getScoreArray());
        questionsRV.setLayoutManager(new LinearLayoutManager(this));
        questionsRV.setAdapter(questionsAdapter);

        feedbackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback = feedbackEdit.getText().toString();
                assignment.setFeedback(feedback);
                usersRef.child(studentUserId).child("listOfAssignment").child(assignment.getQuizNumber()).setValue(assignment);
                startActivity(new Intent(InstructorReviewActivity.this, InstructorMainActivity.class));
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
                startActivity(new Intent(InstructorReviewActivity.this, LoginActivity.class));
                return true;
            case R.id.return_home:
                startActivity(new Intent(InstructorReviewActivity.this, InstructorMainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}