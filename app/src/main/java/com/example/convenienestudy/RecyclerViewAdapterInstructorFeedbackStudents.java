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

public class RecyclerViewAdapterInstructorFeedbackStudents extends RecyclerView.Adapter<RecyclerViewAdapterInstructorFeedbackStudents.InstructorFeedbackStudentsViewHolder> {

    private Context mContext;
    private List<Student> mData;
    private HashMap<String, Assignment> mAssignment;

    public RecyclerViewAdapterInstructorFeedbackStudents(Context mContext, List<Student> mData, HashMap<String, Assignment> mAssignment){
        this.mContext = mContext;
        this.mData = mData;
        this.mAssignment = mAssignment;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterInstructorFeedbackStudents.InstructorFeedbackStudentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.cardview_item_instructorfeedbackstudents,parent,false);
        return new RecyclerViewAdapterInstructorFeedbackStudents.InstructorFeedbackStudentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterInstructorFeedbackStudents.InstructorFeedbackStudentsViewHolder holder, final int position) {
        final String tempStudentId = mData.get(position).getStudentId();
        Log.d("RecyclerViewAdapterInstructorFeedbackStudents", tempStudentId);
        holder.student_name.setText(mData.get(position).getName());
        holder.student_score.setText(String.valueOf(mAssignment.get(tempStudentId).getScore()));
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mContext, InstructorReviewActivity.class);
                intent.putExtra("studentUserId", mData.get(position).getUserId());
                intent.putExtra("assignmentObject", mAssignment.get(tempStudentId));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class InstructorFeedbackStudentsViewHolder extends RecyclerView.ViewHolder{

        TextView student_name;
        TextView student_score;
        CardView cardView;

        public InstructorFeedbackStudentsViewHolder(View itemView){
            super(itemView);
            student_name = (TextView) itemView.findViewById(R.id.instructor_feedback_student_name_id);
            student_score = (TextView) itemView.findViewById(R.id.instructor_feedback_student_score_id);
            cardView = (CardView) itemView.findViewById(R.id.instructor_feedback_student_cardview_id);
        }
    }
}
