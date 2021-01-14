package com.example.convenienestudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstructorQuizActivity extends AppCompatActivity {

    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private TextView quizTitle, quizScore, quizDescription;
    private List<Question> lstQuestion;
    private HashMap<String, String> lstStudentId;
    private RecyclerView questionRV;
    private RecyclerViewAdapterInstructorQuestion questionAdapter;
    private Button addQuestionButton, deleteQuizButton, publishQuizButton, feedbackQuizButton;
    private DatabaseReference usersRef, quizRef, questionRef;
    private Quiz quiz;
    private String quizNumberString, instructorId, schoolId;
    private int quizNumber;
    private LinearLayout add_delete_qns, publish_delete_qns;
    SharedPreferences mPreferences;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_quiz);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        quizTitle = (TextView) findViewById(R.id.quizTitle_id);
        quizScore = (TextView) findViewById(R.id.quizScore_id);
        quizDescription = (TextView) findViewById(R.id.quizDescription_id);
        lstQuestion = new ArrayList<Question>();
        lstStudentId = new HashMap<String, String>();

        mPreferences = getSharedPreferences(LoginActivity.sharedPreFile, MODE_PRIVATE);
        instructorId = mPreferences.getString(LoginActivity.instructorIdKey, "EMPTY");
        schoolId = mPreferences.getString(LoginActivity.schoolIdKey, "EMPTY");

        addQuestionButton = (Button) findViewById(R.id.addQuestionButton); //visible when quiz not published

        deleteQuizButton = (Button) findViewById(R.id.deleteQuizButton); //visible when quiz not published
        publishQuizButton = (Button) findViewById(R.id.publishQuizButton); //visible when quiz not published
        feedbackQuizButton = (Button) findViewById(R.id.feedbackQuizButton); //always visible

        add_delete_qns = findViewById(R.id.add_delete_qns); //layout visible when quiz not published
        publish_delete_qns = findViewById(R.id.publish_delete_quiz); //layout visible when quiz not published

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        Query studentQuery = usersRef.orderByChild("schoolId").equalTo(schoolId);
        studentQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.hasChild("studentId")){
                        String tempString = ds.child("userId").getValue(String.class);
                        String tempInt = ds.child("studentId").getValue(String.class);
                        lstStudentId.put(tempString, tempInt);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Adding Student", error.getDetails());
            }
        });


        Intent intent = getIntent();
        quiz = intent.getExtras().getParcelable("quizObject");

        quizNumberString = quiz.getQuizNumberString();
        quizNumber = quiz.getQuizNumber();
        if (quiz.isQuizPublished()){
            //hiding unwanted layout
            add_delete_qns.setVisibility(View.GONE);
            publish_delete_qns.setVisibility(View.GONE);

            //making new layout visible and reassigning variables
            publishQuizButton = findViewById(R.id.publishQuizButton_hide);
            deleteQuizButton = findViewById(R.id.deleteQuizButton_hide);
            deleteQuizButton.setVisibility(View.VISIBLE);
            publishQuizButton.setVisibility(View.VISIBLE);
        }

        addQuestionButton.setOnClickListener(addQuestionListener);
        deleteQuizButton.setOnClickListener(deleteQuizListener);
        publishQuizButton.setOnClickListener(publishQuizListener);
        feedbackQuizButton.setOnClickListener(feedbackQuizListener);

        quizRef = usersRef.child(userId).child("lstOfQuiz");

        String title = quiz.getTitle();
        String description = quiz.getDescription();

        questionRef = quizRef.child(quizNumberString).child("listOfQuestion");
        questionRef.addValueEventListener(questionListener);

        String score = "Total Score: " + String.valueOf(quiz.getTotalScore());

        quizTitle.setText(title);
        quizScore.setText(score);
        quizDescription.setText(description);

        questionRV = (RecyclerView) findViewById(R.id.recyclerviewQuestion_id);
        questionAdapter  = new RecyclerViewAdapterInstructorQuestion(this, lstQuestion);
        questionRV.setLayoutManager(new LinearLayoutManager(this));
        questionRV.setAdapter(questionAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                startActivity(new Intent(InstructorQuizActivity.this, LoginActivity.class));
                return true;
            case R.id.return_home:
                startActivity(new Intent(InstructorQuizActivity.this, InstructorMainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InstructorQuizActivity.this, InstructorMainActivity.class));
    }

    View.OnClickListener addQuestionListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(InstructorQuizActivity.this, InstructorAddQuestion.class);
            intent.putExtra("quizObject", quiz);
            int nextQuestionId = 0;
            if (!lstQuestion.isEmpty()){
                nextQuestionId = lstQuestion.get(lstQuestion.size()-1).getQuestionId() + 1;
                Log.d("Adding Question", "Value of id is " + nextQuestionId);
            }
            intent.putExtra("questionId", nextQuestionId);
            startActivity(intent);
        }
    };

    View.OnClickListener publishQuizListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (quiz.isQuizPublished()){//Unpublish
                for (Map.Entry<String, String> entry: lstStudentId.entrySet()){
                    usersRef.child(entry.getKey()).child("listOfAssignment").child(quizNumberString).setValue(null);
                }
                quiz.setQuizPublished(false);
            }
            else {//Publish
                if(lstQuestion.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Cannot publish empty quiz",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if(!lstStudentId.isEmpty()) {
                    for (Map.Entry<String, String> entry : lstStudentId.entrySet()) {
                        usersRef.child(entry.getKey()).child("listOfAssignment").child(quizNumberString).setValue(new Assignment(quizNumberString, entry.getValue()));
                    }
                    quiz.setQuizPublished(true);
                }
            }
            quizRef.child(quizNumberString).child("quizPublished").setValue(quiz.isQuizPublished());
            Intent tempIntent = new Intent(InstructorQuizActivity.this, InstructorQuizActivity.class);
            tempIntent.putExtra("quizObject", quiz);
            startActivity(tempIntent);
        }
    };

    View.OnClickListener deleteQuizListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            quizRef.child(quizNumberString).setValue(null);
            quizRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()){
                        Quiz temp = ds.getValue(Quiz.class);
                        int tempNumber = temp.getQuizNumber();
                        if (tempNumber > quizNumber){
                            quizRef.child(String.valueOf(tempNumber)).setValue(null);
                            quizRef.child(String.valueOf(tempNumber-1)).setValue(new Quiz(temp.getTitle(), temp.getDescription(), tempNumber-1, instructorId));
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            if(quiz.isQuizPublished()){
                for (final Map.Entry<String, String> entry : lstStudentId.entrySet()){
                    usersRef.child(entry.getKey()).child("listOfAssignment").child(quiz.getQuizNumberString()).setValue(null);
                    usersRef.child(entry.getKey()).child("listOfAssignment").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()){
                                Assignment tempAssignment = ds.getValue(Assignment.class);
                                int tempAssignmentNumber = Integer.parseInt(tempAssignment.getQuizNumber());
                                if (tempAssignmentNumber > quizNumber){
                                    usersRef.child(entry.getKey()).child("listOfAssignment").child(String.valueOf(tempAssignmentNumber)).setValue(null);
                                    usersRef.child(entry.getKey()).child("listOfAssignment").child(String.valueOf(tempAssignmentNumber-1)).setValue(new Assignment(String.valueOf(tempAssignmentNumber-1), tempAssignment.getStudentId()));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            startActivity(new Intent(InstructorQuizActivity.this, InstructorMainActivity.class));
        }
    };

    View.OnClickListener feedbackQuizListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent feedbackIntent = new Intent(InstructorQuizActivity.this, InstructorQuizStudentFeedback.class);
            feedbackIntent.putExtra("listOfStudent", (Serializable) lstStudentId);
            feedbackIntent.putExtra("quizObject", quiz);
            startActivity(feedbackIntent);
        }
    };

    ValueEventListener questionListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Question temp = ds.getValue(Question.class);
                    if (!lstQuestion.contains(temp)){
                        lstQuestion.add(temp);
                        quizScore.setText("Total Score: " + String.valueOf(quiz.getTotalScore()));
                    }
                }
                questionRV.setAdapter(questionAdapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w("loadPost:onCancelled", error.toException());
        }
    };

}