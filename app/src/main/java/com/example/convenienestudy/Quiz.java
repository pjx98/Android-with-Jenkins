package com.example.convenienestudy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Quiz implements Parcelable {

    private String title,description,instructorId;
    private int totalScore = 0;
    private int currentScore = 0;
    private ArrayList<Question> listOfQuestion = new ArrayList<Question>();
    private int quizNumber;
    private boolean quizCompleted, quizPublished;


    public Quiz(){
    }

    public Quiz(String title, String description, int quizNumber,String instructorId) {
        this.title = title;
        this.description = description;
        this.quizNumber = quizNumber;
        this.instructorId = instructorId;
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in){
            return new Quiz(in);
        }
        public Quiz[] newArray(int size){
            return new Quiz[size];
        }
    };

    protected Quiz(Parcel in){
        title = in.readString();
        description = in.readString();
        totalScore = in.readInt();
        currentScore = in.readInt();
        in.readList(listOfQuestion, Quiz.class.getClassLoader());
        quizNumber = in.readInt();
        quizCompleted = in.readInt() == 1;
        instructorId = in.readString();
        quizPublished = in.readInt() == 1;
    }

    public String getDescription() {
        if (description == null){
            return "Description";
        }
        return description;
    }

    public int getQuizNumber() {
        return quizNumber;
    }

    public void setQuizNumber(int quizNumber) {
        this.quizNumber = quizNumber;
    }

    public String getQuizNumberString() {
        return Integer.toString(quizNumber);
    }

    public String getTitle() {
        return title;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public ArrayList<Question> getListOfQuestion() {
        return listOfQuestion;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public boolean isQuizPublished() {
        return quizPublished;
    }

    public void setQuizPublished(boolean quizPublished) {
        this.quizPublished = quizPublished;
    }

    public String getScoreToString(){
        return ("Score: " + Integer.toString(currentScore) + " / " + Integer.toString(totalScore));
    }

    public boolean addQuestion(Question q){
        boolean added = false;
        if(!listOfQuestion.contains(q)) {
            this.listOfQuestion.add(q);
            added = true;
        }
        return added;
    }

    public boolean updateQuestion(Question q){
        boolean updated = false;
        if(listOfQuestion.contains(q)){
            for(Question s : listOfQuestion){
                if (s.getQuestionId() == q.getQuestionId()){
                    listOfQuestion.remove(s);
                    listOfQuestion.add(q);
                    updated = true;
                    break;
                }
            }
        }
        return updated;
    }

    public boolean removeQuestion(Question q){
        boolean removed = false;
        if(listOfQuestion.contains(q)){
            this.listOfQuestion.remove(q);
            removed = true;
        }
        return removed;
    }

    public void updateMaxScore(int score){
        this.totalScore = this.totalScore + score;
    }

    public void updateCurrentScore(int score){
        this.currentScore = this.currentScore + score;
    }

    @Override
    //Parcel
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(totalScore);
        dest.writeInt(currentScore);
        dest.writeList(listOfQuestion);
        dest.writeInt(quizNumber);
        dest.writeInt(quizCompleted ? 1 : 0);
        dest.writeString(instructorId);
        dest.writeInt(quizPublished ? 1: 0);
    }
    public int describeContents() {
        return 0;
    }
}
