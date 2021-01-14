package com.example.convenienestudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InstructorAddQuizActivity extends AppCompatActivity {

    private Button createQuiz;
    private EditText quizTitle, quizDescription;
    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
    private DatabaseReference quizRef = userRef.child("lstOfQuiz");
    private String title, description, noOfQuiz, instructorId;
    private SharedPreferences mPreferences;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_add_quiz);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        createQuiz = (Button) findViewById(R.id.createQuizButton);
        quizTitle = (EditText) findViewById(R.id.createQuizTitle);
        quizDescription = (EditText) findViewById(R.id.createQuizDescription);



        mPreferences = getSharedPreferences(LoginActivity.sharedPreFile, MODE_PRIVATE);
        instructorId = mPreferences.getString(LoginActivity.instructorIdKey, "EMPTY");

        Intent getIntent = getIntent();
        noOfQuiz = Integer.toString(getIntent.getIntExtra("numberOfQuiz", 0));


        createQuiz.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                title = quizTitle.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(getApplicationContext(),getString(R.string.fill_in_quiz_title),Toast.LENGTH_SHORT).show();
                    return;
                }
                description = quizDescription.getText().toString();
                quizRef.addListenerForSingleValueEvent(checkForChild);
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
                startActivity(new Intent(InstructorAddQuizActivity.this, LoginActivity.class));
                return true;
            case R.id.return_home:
                startActivity(new Intent(InstructorAddQuizActivity.this,InstructorMainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    ValueEventListener checkForChild = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            boolean existed = false;
            for (DataSnapshot ds : snapshot.getChildren()){
                String tempString = ds.child("title").getValue(String.class);
                Log.d("TESTING TITLE", "Title is " + title + " And the child is " + tempString);
                if (title.equalsIgnoreCase(tempString)){
                    existed = true;
                }
            }
            if (!existed){
                quizRef.child(noOfQuiz).setValue(new Quiz(title, description, Integer.parseInt(noOfQuiz), instructorId));
                startActivity(new Intent(InstructorAddQuizActivity.this, InstructorMainActivity.class));
            }
            else {
                Toast.makeText(InstructorAddQuizActivity.this,
                        R.string.quiz_exist_text,
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}