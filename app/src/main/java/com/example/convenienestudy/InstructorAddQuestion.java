package com.example.convenienestudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InstructorAddQuestion extends AppCompatActivity {


    private Button createQuestionButton;
    private EditText question,answer, score, choice1, choice2, choice3, choice4;
    private DatabaseReference questionRef, quizRef;
    private Intent intent;
    private String questionString, answerString, quizTitle, mcq1, mcq2, mcq3, mcq4, questionScoreString;
    private int questionScore, questionId;
    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private Quiz quiz;
    private TextView quiz_header;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_add_question);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }


        Intent getIntent = getIntent();
        quiz = getIntent.getExtras().getParcelable("quizObject");
        questionId = getIntent.getIntExtra("questionId", 0);

        quizTitle = quiz.getTitle();
        createQuestionButton = (Button) findViewById(R.id.createQuestion_Button);
        question = (EditText) findViewById(R.id.createQuestion_Question);
        answer = (EditText) findViewById(R.id.createQuestion_Answer);
        score = (EditText) findViewById(R.id.createQuestion_Score);
        choice1 = (EditText) findViewById(R.id.createQuestion_Choice1);
        choice2 = (EditText) findViewById(R.id.createQuestion_Choice2);
        choice3 = (EditText) findViewById(R.id.createQuestion_Choice3);
        choice4 = (EditText) findViewById(R.id.createQuestion_Choice4);
        quiz_header = findViewById(R.id.quiz_header);




        int questionNo = quiz.getListOfQuestion().size() / 5;
        quiz_header.setText(quizTitle + ": QUESTION " + String.valueOf(questionNo));

        createQuestionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                quizRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("lstOfQuiz").child(quiz.getQuizNumberString());
                questionRef = quizRef.child("listOfQuestion");
                questionString = question.getText().toString();
                answerString = answer.getText().toString();
                questionScoreString = score.getText().toString();
                mcq1 = choice1.getText().toString();
                mcq2 = choice2.getText().toString();
                mcq3 = choice3.getText().toString();
                mcq4 = choice4.getText().toString();

                if (TextUtils.isEmpty(questionString) || TextUtils.isEmpty(answerString) || TextUtils.isEmpty(String.valueOf(questionScore)) || TextUtils.isEmpty(mcq1) || TextUtils.isEmpty(mcq2)) {
                    Toast.makeText(getApplicationContext(),"Please input all the necessary fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                questionScore = Integer.parseInt(questionScoreString);



                questionRef.addListenerForSingleValueEvent(checkForChild);
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
                startActivity(new Intent(InstructorAddQuestion.this, LoginActivity.class));
                return true;
            case R.id.return_home:
                startActivity(new Intent(InstructorAddQuestion.this, InstructorMainActivity.class));
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    ValueEventListener checkForChild = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            //TODO MAKE SURE NO DUPLICATE QUESTION
            boolean existed = false;
            for (DataSnapshot ds: snapshot.getChildren()){
                String tempString = ds.child("questionTitle").getValue(String.class);
                if (tempString.equalsIgnoreCase(questionString)){
                    existed = true;
                }
            }
            if (!existed){
                Question tempQuestion = new Question(questionString, questionScore, answerString, mcq1, mcq2, mcq3, mcq4, quiz, questionId);
                quizRef.setValue(quiz);
                intent = new Intent(InstructorAddQuestion.this, InstructorQuizActivity.class);
                intent.putExtra("quizObject", quiz);
                startActivity(intent);
            }
            else {
                Toast.makeText(InstructorAddQuestion.this, "Question exist", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }

    };


    @Override
    public void onBackPressed() {
        startActivity(new Intent(InstructorAddQuestion.this, InstructorMainActivity.class));

    }
}