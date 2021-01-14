package com.example.convenienestudy;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CalendarContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.ArrayList;

public class Assignment implements Parcelable {
    private int score;
    private String feedback = "Await Feedback";
    private boolean completed;
    private String studentId, quizNumber, dueDate;
    private ArrayList<String> scoreArray = new ArrayList<String>();

    public Assignment(){}

    public Assignment(String quizNumber, String studentId) {
        this.quizNumber = quizNumber;
        this.studentId = studentId;
        this.dueDate = makeDueDate();
    }

    public static final Creator<Assignment> CREATOR = new Creator<Assignment>() {
        @Override
        public Assignment createFromParcel(Parcel source) {
            return new Assignment(source);
        }

        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };

    protected Assignment(Parcel in){
        score = in.readInt();
        feedback = in.readString();
        completed = in.readInt() == 1;
        studentId = in.readString();
        quizNumber = in.readString();
        dueDate = in.readString();
        scoreArray = in.readArrayList(String.class.getClassLoader());
    }

    public String getQuizNumber() {
        return quizNumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getScore() {
        return score;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getDueDate() {
        return dueDate;
    }

    public ArrayList<String> getScoreArray() {
        return scoreArray;
    }

    public void addScoreArray(String score){
        this.scoreArray.add(score);
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setScore(int score) {
        this.score = this.score + score;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    private String makeDueDate(){
        //LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        //localDateTime = localDateTime.plusDays(7);

        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusDays(7);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        Date date = Date.from(instant);
        String date_without_time = DateFormat.getDateInstance().format(date);
        String res = new StringBuilder("Due: ").append(date_without_time).toString();
        return res;
    }

    @Override
    public String toString(){
        return "Assignment: " + quizNumber + "(Quiz Number) belong to " + studentId + "(Student Id)";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(score);
        dest.writeString(feedback);
        dest.writeInt(completed ? 1 : 0);
        dest.writeString(studentId);
        dest.writeString(quizNumber);
        dest.writeString(dueDate);
        dest.writeList(scoreArray);
    }
}