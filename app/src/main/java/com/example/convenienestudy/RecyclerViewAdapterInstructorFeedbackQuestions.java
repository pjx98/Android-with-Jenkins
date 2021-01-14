package com.example.convenienestudy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

public class RecyclerViewAdapterInstructorFeedbackQuestions  extends RecyclerView.Adapter<RecyclerViewAdapterInstructorFeedbackQuestions.InstructorFeedbackQuestionsViewHolder> {

    private Context mContext;
    private List<String> mData;

    public RecyclerViewAdapterInstructorFeedbackQuestions(Context mContext, List<String> mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterInstructorFeedbackQuestions.InstructorFeedbackQuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.cardview_item_instructorstudentquiz,parent,false);
        return new RecyclerViewAdapterInstructorFeedbackQuestions.InstructorFeedbackQuestionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterInstructorFeedbackQuestions.InstructorFeedbackQuestionsViewHolder holder, int position) {
        Log.d("RecyclerViewAdapterInstructorFeedbackStudents", "Scores added into the recyclerview");
        String tempString = "Question " + (position+1);
        holder.question_number.setText(tempString);
        holder.question_score.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class InstructorFeedbackQuestionsViewHolder extends RecyclerView.ViewHolder{

        TextView question_number;
        TextView question_score;
        CardView cardView;

        public InstructorFeedbackQuestionsViewHolder(View itemView){
            super(itemView);
            question_number = (TextView) itemView.findViewById(R.id.instructor_feedback_question_no_id);
            question_score = (TextView) itemView.findViewById(R.id.instructor_feedback_question_score_id);
            cardView = (CardView) itemView.findViewById(R.id.instructor_feedback_student_quiz_details_cardview_id);
        }
    }
}
