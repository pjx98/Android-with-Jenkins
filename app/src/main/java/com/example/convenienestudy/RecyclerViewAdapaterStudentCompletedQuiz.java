package com.example.convenienestudy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class RecyclerViewAdapaterStudentCompletedQuiz extends RecyclerView.Adapter<RecyclerViewAdapaterStudentCompletedQuiz.StudentCompletedQuizViewHolder> {

    private Context mContext;
    private HashMap<String, Quiz> mDataQuiz;
    private List<Assignment> mDataAssignment;

    public RecyclerViewAdapaterStudentCompletedQuiz(Context mContext, HashMap<String, Quiz> mDataQuiz, List<Assignment> mDataAssignment){
        this.mContext = mContext;
        this.mDataQuiz = mDataQuiz;
        this.mDataAssignment = mDataAssignment;
    }

    @NonNull
    @Override
    public RecyclerViewAdapaterStudentCompletedQuiz.StudentCompletedQuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.cardview_item_studentquizes,parent,false);
        return new RecyclerViewAdapaterStudentCompletedQuiz.StudentCompletedQuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapaterStudentCompletedQuiz.StudentCompletedQuizViewHolder holder, final int position) {
        final String tempQuizId = mDataAssignment.get(position).getQuizNumber();
        Log.d("STUDENT MAIN ACTIVITY", "VALUE OF THE TEMPQUIZID" + tempQuizId);
        final String tempQuizTitle = mDataQuiz.get(tempQuizId).getTitle();

        Log.d("STUDENT MAIN ACTIVITY", "VALUE OF THE TITLE" + tempQuizTitle);
        holder.quiz_name.setText(tempQuizTitle);
        holder.due_date.setText(mDataAssignment.get(position).getDueDate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StudentQuizActivity.class);
                intent.putExtra("quizObject", mDataQuiz.get(tempQuizId));
                intent.putExtra("assignmentObject", mDataAssignment.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataAssignment.size();
    }

    public static class StudentCompletedQuizViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView quiz_name;
        TextView due_date;

        public StudentCompletedQuizViewHolder(View itemView){
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.student_cardview_id);
            quiz_name = (TextView) itemView.findViewById(R.id.studentQuiz_title_id);
            due_date = (TextView) itemView.findViewById(R.id.studentQuiz_due_date);
        }
    }
}
