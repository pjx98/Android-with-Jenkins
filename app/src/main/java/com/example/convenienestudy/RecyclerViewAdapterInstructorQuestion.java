package com.example.convenienestudy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterInstructorQuestion extends RecyclerView.Adapter<RecyclerViewAdapterInstructorQuestion.InstructorQuestionViewHolder> {

    private Context mContext;
    private List<Question> mData;

    public RecyclerViewAdapterInstructorQuestion(Context mContext, List<Question> mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterInstructorQuestion.InstructorQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.cardview_item_instructorquestions,parent,false);
        return new InstructorQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterInstructorQuestion.InstructorQuestionViewHolder holder, int position) {
        String problem = "Problem " + Integer.toString(position+1) + ": " + mData.get(position).getQuestionTitle();
        holder.question_title.setText(problem);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class InstructorQuestionViewHolder extends RecyclerView.ViewHolder{
        TextView question_title;

        public InstructorQuestionViewHolder(@NonNull View itemView){
            super(itemView);

            question_title = (TextView) itemView.findViewById(R.id.InstructorQuestionTitle);
        }
    }
}
