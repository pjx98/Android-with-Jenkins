package com.example.convenienestudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Scene;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private EditText email, password;
    private Button registerBtn, loginBtn;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference schoolRef = myRootRef.child("School");
    private static final String TAG = "LoginActivity";
    public static final String instructorIdKey = "instructorId";
    public static final String schoolIdKey = "schoolId";
    public static final String studentIdKey = "studentId";
    public static final String sharedPreFile = "com.example.convenienestudy.mainsharedprefs";
    private String instructorId, studentId, schoolId;
    SharedPreferences mPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email = (EditText) findViewById(R.id.login_email_address);
        password = (EditText) findViewById(R.id.login_password);
        registerBtn = (Button) findViewById(R.id.sign_up_btn);
        loginBtn = (Button) findViewById(R.id.login_btn);

        mPreferences = getSharedPreferences(sharedPreFile, MODE_PRIVATE);
        instructorId = mPreferences.getString(instructorIdKey, "EMPTY");
        schoolId = mPreferences.getString(schoolIdKey, "EMPTY");
        studentId = mPreferences.getString(studentIdKey, "EMPTY");

        firebaseAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();

                if (TextUtils.isEmpty(Email)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference userRef = myRootRef.child("Users").child(userId);
                                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            schoolId = snapshot.child("schoolId").getValue(String.class);
                                            Log.d(TAG, "School id value from database is " + schoolId);
                                            if (snapshot.hasChild("instructorId")){
                                                instructorId = snapshot.child("instructorId").getValue(String.class);
                                                startActivity(new Intent(getApplicationContext(), InstructorMainActivity.class));
                                                finish();
                                            }
                                            if (snapshot.hasChild("studentId")){
                                                studentId = snapshot.child("studentId").getValue(String.class);
                                                startActivity(new Intent(getApplicationContext(), StudentMainActivity.class));
                                                finish();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                } else {
                                    Toast.makeText(getApplicationContext(), "E-mail or password is wrong", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                }
                            }
                        });
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(instructorIdKey, instructorId);
        preferencesEditor.putString(schoolIdKey, schoolId);
        Log.d(TAG, "School id value stored" + schoolId);
        preferencesEditor.putString(studentIdKey, studentId);
        preferencesEditor.apply();
    }

}
