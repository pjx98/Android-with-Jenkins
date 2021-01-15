package com.example.convenienestudy;

import org.junit.Test;

import java.util.ArrayList;
import android.os.Parcel;
//import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {

    @Test
    public void add_question(){
        Quiz quiz = new Quiz();
        //ArrayList<Question> listOfQuestion = new ArrayList<Question>();
        Question q = null;
        boolean check_true = quiz.addQuestion(q);
        boolean check_false = quiz.addQuestion(q);
        assertEquals(true, check_true);
        assertEquals(false, check_false);
    }

    @Test
    public void remove_question(){
        Quiz quiz = new Quiz();
        //ArrayList<Question> listOfQuestion = new ArrayList<Question>();
        Question q = null;

        boolean check_false = quiz.removeQuestion(q);
        assertEquals(false, check_false);

        quiz.addQuestion(q);
        boolean check_true = quiz.removeQuestion(q);
        assertEquals(true, check_true);
    }

    @Test
    public void updateScore(){
        Quiz quiz = new Quiz();
        quiz.updateMaxScore(10);
        assertEquals(10, quiz.getTotalScore());
        assertEquals(0, quiz.getCurrentScore());
    }

    @Test
    public void quiz_constructor(){
        //public Quiz(String title, String description, int quizNumber,String instructorId) {
        Quiz quiz = new Quiz("Math Quiz 1", "Math", 1, "123");
        assertEquals("Math Quiz 1", quiz.getTitle());
        assertEquals("Math", quiz.getDescription());
        assertEquals(1, quiz.getQuizNumber());
        assertEquals("123", quiz.getInstructorId());
    }

    @Test
    public void checkAnswer(){
        Question question = new Question();
        question.setAnswer("45");
        assertEquals(true, question.checkAnswer("45"));
        assertEquals(false, question.checkAnswer("0"));

    }



}