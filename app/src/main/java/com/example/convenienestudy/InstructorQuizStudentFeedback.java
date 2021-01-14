package com.example.convenienestudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InstructorQuizStudentFeedback extends AppCompatActivity {

    //TODO List down the number of students
    //Get the name and score of the users and show them in the a recyclerview
    private RecyclerView studentRV;
    private RecyclerViewAdapterInstructorFeedbackStudents studentAdapter;
    private Quiz quiz;
    private HashMap<String, String> listOfStudents = new HashMap<String,String>(); //Key = userId, Value = studentId
    private ArrayList<Student> students;
    private HashMap<String, Assignment> assignments;
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
    private final String TAG = "STUDENTFEEDBACK";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_quiz_student_feedback);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        Intent getIntent = getIntent();
        listOfStudents = (HashMap<String,String>) getIntent.getSerializableExtra("listOfStudent");

        quiz = getIntent.getExtras().getParcelable("quizObject");
        students = new ArrayList<Student>();
        assignments = new HashMap<String, Assignment>();

        studentRV = (RecyclerView) findViewById(R.id.instructorStudentsFeedback_recyclerview_id);
        studentAdapter = new RecyclerViewAdapterInstructorFeedbackStudents(this, students, assignments);
        studentRV.setLayoutManager(new LinearLayoutManager(this));
        studentRV.setAdapter(studentAdapter);
//
        for (final Map.Entry<String, String> entry : listOfStudents.entrySet()){
            usersRef.child(entry.getKey()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.child("listOfAssignment").getChildren()){
                        if (ds.child("quizNumber").getValue(String.class).equals(quiz.getQuizNumberString()) && ds.child("completed").getValue(boolean.class)){
                            Student tempStudent = snapshot.getValue(Student.class);
                            Assignment tempAssignment = ds.getValue(Assignment.class);
                            Log.d(TAG, "Value of student added " + tempStudent.getStudentId());
                            Log.d(TAG, "Value of assignment added " + tempAssignment.getQuizNumber());
                            students.add(tempStudent);
                            assignments.put(entry.getValue(), tempAssignment);
                        }
                    }
                    studentRV.setAdapter(studentAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                startActivity(new Intent(InstructorQuizStudentFeedback.this, LoginActivity.class));
                return true;
            case R.id.home:
                startActivity(new Intent(InstructorQuizStudentFeedback.this, InstructorMainActivity.class));
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
