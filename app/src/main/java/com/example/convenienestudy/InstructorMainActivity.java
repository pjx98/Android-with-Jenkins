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
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InstructorMainActivity extends AppCompatActivity {

    private ArrayList<Quiz> lstQuiz;
    private RecyclerView quizRV;
    private FloatingActionButton btnFloating; 
    private RecyclerViewAdapterInstructorQuiz quizAdapter;
    private final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private DatabaseReference userRef = usersRef.child(userId);
    
    private DatabaseReference quizRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("lstOfQuiz");
    private TextView workspace_header;
    private Toolbar toolbar;


    private static final String TAG ="Instructor Main Activity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_main);

        lstQuiz = new ArrayList<>();
        quizRV = (RecyclerView) findViewById(R.id.instructorQuiz_recyclerview_id);
        quizAdapter = new RecyclerViewAdapterInstructorQuiz(this, lstQuiz);
        btnFloating = (FloatingActionButton) findViewById(R.id.instructorQuiz_btnFloating);
        workspace_header = findViewById(R.id.workspace_header);
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                workspace_header.setText(name.toUpperCase() + "'S WORKSPACE");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        quizRef.addValueEventListener(instructorQuizListener);

        quizRV.setLayoutManager(new LinearLayoutManager(this));
        quizRV.setAdapter(quizAdapter);

        quizRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(quizRV.SCROLL_STATE_DRAGGING==newState){
                    btnFloating.hide();
                }
                else {
                    btnFloating.show();
                }
            }
        });

        btnFloating.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(InstructorMainActivity.this, InstructorAddQuizActivity.class);
                intent.putExtra("numberOfQuiz",lstQuiz.size());
                startActivity(intent);
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
                startActivity(new Intent(InstructorMainActivity.this, LoginActivity.class));
                return true;

            case R.id.return_home:
                startActivity(new Intent(InstructorMainActivity.this, InstructorMainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    ValueEventListener instructorQuizListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Quiz temp = ds.getValue(Quiz.class);
                    if (!lstQuiz.contains(temp)){
                        lstQuiz.add(temp);
                    }
                }
                quizRV.setAdapter(quizAdapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.d(TAG, error.toException().toString());
        }
    };

    @Override
    public void onBackPressed() {

    }


}