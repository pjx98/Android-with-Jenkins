package com.example.convenienestudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailField, passwordField, nameField, verifyPasswordField;
    private Button registerBtn;
    private Spinner userSpinner, schoolSpinner;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference schoolRef = myRootRef.child("School");
    private HashMap<String, String> tempHashMapSchoolId = new HashMap<String, String>();
    private HashMap<String, String> tempHashMapStudentId = new HashMap<String, String>();
    private HashMap<String, String> tempHashMapInstructorId = new HashMap<String, String>();
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO UPDATE THE FIREBASE WITH THE CORRECT DATABASE REFERENCES
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        schoolRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    School tempSchoolObj = ds.getValue(School.class);
                    Log.d("REGISTER", "THE TEMP School HAS " + tempSchoolObj.getName() + " And " + tempSchoolObj.getSchoolId()
                            + " with " + tempSchoolObj.getStudentIdCounter() + " studentId and " + tempSchoolObj.getInstructorIdCounter() + " instructorId");
                    tempHashMapSchoolId.put(tempSchoolObj.getName(), tempSchoolObj.getSchoolId());
                    tempHashMapStudentId.put(tempSchoolObj.getName(), tempSchoolObj.getStudentIdCounter());
                    tempHashMapInstructorId.put(tempSchoolObj.getName(), tempSchoolObj.getInstructorIdCounter());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        userSpinner = (Spinner) findViewById(R.id.register_userSpinner);
        schoolSpinner = (Spinner) findViewById(R.id.register_schoolSpinner);
        emailField = (EditText) findViewById(R.id.register_email_address);
        passwordField = (EditText) findViewById(R.id.register_password);
        registerBtn = (Button) findViewById(R.id.register_submit);
        nameField = (EditText) findViewById(R.id.register_name);
        verifyPasswordField = (EditText) findViewById(R.id.register_verify_password);


        ArrayAdapter<CharSequence> userSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.user_array, android.R.layout.simple_spinner_item);
        userSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(userSpinnerAdapter);

        ArrayAdapter<CharSequence> schoolSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.school_array, android.R.layout.simple_spinner_item);
        schoolSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(schoolSpinnerAdapter);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailField.getText().toString();
                final String password = passwordField.getText().toString();
                final String name = nameField.getText().toString();
                final String verifyPassword = verifyPasswordField.getText().toString();
                final String selectedRole = userSpinner.getSelectedItem().toString();
                final String selectedSchool = schoolSpinner.getSelectedItem().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter name!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (!password.equals(verifyPassword)){
                    Toast.makeText(getApplicationContext(),
                            "Password not match",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if(Objects.equals(tempHashMapInstructorId.get(selectedSchool), "0") && selectedRole.equals("Instructor")){
                    Toast.makeText(getApplicationContext(),
                            "Instructor Exist",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    String schoolId = tempHashMapSchoolId.get(selectedSchool);
                                    Log.d(TAG, "The value of selectedRole: " + selectedRole + " Testing for unknown char");
                                    Log.d(TAG, "Value of name and email: " + email + " " + name);
                                    if (selectedRole.equals("Instructor")){
                                        int addedInstructorValue = Integer.parseInt(tempHashMapInstructorId.get(selectedSchool)) + 1;
                                        Log.d(TAG, "The role " + selectedRole + " Selected");
                                        DatabaseReference userRef = myRootRef.child("Users").child(userId);
                                        userRef.setValue(new Instructor(name, email,schoolId,userId, String.valueOf(addedInstructorValue)));
                                        schoolRef.child(schoolId).child("instructorIdCounter").setValue(String.valueOf(addedInstructorValue));
                                    }
                                    if (selectedRole.equals("Student")){
                                        int addedStudentValue = Integer.parseInt(tempHashMapStudentId.get(selectedSchool)) + 1;
                                        Log.d(TAG, "The role " + selectedRole + "Selected");
                                        DatabaseReference userRef = myRootRef.child("Users").child(userId);
                                        userRef.setValue(new Student(name, email,schoolId,userId, String.valueOf(addedStudentValue)));
                                        updateStudentAssignment(myRootRef, userRef, schoolId, String.valueOf(addedStudentValue));
                                        schoolRef.child(schoolId).child("studentIdCounter").setValue(String.valueOf(addedStudentValue));
                                    }
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    Toast.makeText(getApplicationContext(),
                                            "Registration Successful. Please login.",
                                            Toast.LENGTH_LONG)
                                            .show();
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "E-mail or password is wrong", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                }
                            }
                        });
            }
        });

    }
    private void updateStudentAssignment(DatabaseReference myRootRef, final DatabaseReference userRef, String schoolId, final String studentId){
        Query instructorQuery = myRootRef.child("Users").orderByChild("schoolId").equalTo(schoolId);
        instructorQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                        for (DataSnapshot ds2 : ds.child("lstOfQuiz").getChildren()){
                            if (ds2.child("quizPublished").getValue(boolean.class)){
                                if (ds.hasChild("instructorId")){
                                    Quiz tempQuiz = ds2.getValue(Quiz.class);
                                    userRef.child("listOfAssignment").child(tempQuiz.getQuizNumberString()).setValue(new Assignment(tempQuiz.getQuizNumberString(), studentId));
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    };

}
