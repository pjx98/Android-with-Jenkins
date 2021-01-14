package com.example.convenienestudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudentAnswerQuizActivity extends AppCompatActivity {

    private TextView quizTitle, questionTitle;
    private RadioButton choice1, choice2, choice3, choice4;
    private Button nextQuestion;
    private Quiz quiz;
    private Assignment assignment;
    private ArrayList<Question> lstQuestions;
    private String selectedChoice;
    private boolean lastQuestionIndicator;
    private Question tempQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_answer_quiz);

        quizTitle = (TextView) findViewById(R.id.start_quiz_header);
        questionTitle = (TextView) findViewById(R.id.answer_question_question);
        choice1 = (RadioButton) findViewById(R.id.answer_question_choice1);
        choice2 = (RadioButton) findViewById(R.id.answer_question_choice2);
        choice3 = (RadioButton) findViewById(R.id.answer_question_choice3);
        choice4 = (RadioButton) findViewById(R.id.answer_question_choice4);
        nextQuestion = (Button) findViewById(R.id.answer_question_next);
        lastQuestionIndicator = false;
        selectedChoice = "";

        Intent intent = getIntent();
        quiz = intent.getExtras().getParcelable("quizObject");
        assignment = intent.getExtras().getParcelable("assignmentObject");
        lstQuestions = quiz.getListOfQuestion();
        quizTitle.setText("START " + quiz.getTitle().toUpperCase());
        for (int i = 0; i < lstQuestions.size(); ++i){
            tempQuestion = lstQuestions.get(i);
            Log.d("Answer_Question Debug", "Question " + Integer.toString(i) + " completeness: " + String.valueOf(tempQuestion.isCompleted()));
            //Log.d("Answer_Question Debug", "Total Question" + Integer.toString(lstQuestions.size()));
            //Log.d("Answer_Question Debug", "Quiz Address " + System.identityHashCode(quiz));
            if (!tempQuestion.isCompleted()){
                if (lstQuestions.size() == 1){
                    lastQuestionIndicator = true;
                    Toast.makeText(getApplicationContext(), "Last Question", Toast.LENGTH_SHORT).show();
                    nextQuestion.setText("Submit Quiz");
                    Log.d("Answer_Question Debug", "Last Question ");
                }

                else if (lstQuestions.get(i+1).isCompleted()){
                    lastQuestionIndicator = true;
                    Toast.makeText(getApplicationContext(), "Last Question", Toast.LENGTH_SHORT).show();
                    nextQuestion.setText("Submit Quiz");
                    Log.d("Answer_Question Debug", "Last Question ");
                }
                questionTitle.setText(tempQuestion.getQuestionTitle());
                choice1.setText(tempQuestion.getMcqChoice1());
                choice2.setText(tempQuestion.getMcqChoice2());
                choice3.setText(tempQuestion.getMcqChoice3());
                choice4.setText(tempQuestion.getMcqChoice4());
                break;
            }
        }
        nextQuestion.setOnClickListener(nextQuestionListener);

    }

    View.OnClickListener nextQuestionListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if (!selectedChoice.isEmpty()){
                tempQuestion.setCompleted(true);
                Log.d("Answer_Question Debug", "Question Completeness " + String.valueOf(tempQuestion.isCompleted()));
                Toast.makeText(getApplicationContext(), String.valueOf(tempQuestion.isCompleted()), Toast.LENGTH_SHORT).show();
                if(!quiz.updateQuestion(tempQuestion)){
                    Log.d("Answer_Question Debug", "ERROR IN UPDATING QUESTION");
                };

                int tempScore = tempQuestion.answerQuestion(selectedChoice);
                assignment.setScore(tempScore);
                assignment.addScoreArray(String.valueOf(tempScore));
                Intent intent;
                if (lastQuestionIndicator){
                    intent = new Intent(StudentAnswerQuizActivity.this, StudentQuizSummaryActivity.class);
                }
                else {
                    intent = new Intent(StudentAnswerQuizActivity.this, StudentAnswerQuizActivity.class);
                }
                intent.putExtra("quizObject", quiz);
                intent.putExtra("assignmentObject", assignment);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "No choice selected", Toast.LENGTH_SHORT).show();
            }
        }
    };



    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.answer_question_choice1:
                if(checked)
                    selectedChoice = choice1.getText().toString();
                break;
            case R.id.answer_question_choice2:
                if(checked)
                    selectedChoice = choice2.getText().toString();
                break;
            case R.id.answer_question_choice3:
                if(checked)
                    selectedChoice = choice3.getText().toString();
                break;
            case R.id.answer_question_choice4:
                if(checked)
                    selectedChoice = choice4.getText().toString();
                break;
        }
        Toast.makeText(getApplicationContext(), selectedChoice, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Keep on going!", Toast.LENGTH_SHORT).show();


    }
}