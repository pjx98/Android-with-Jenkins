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

import java.util.List;

public class RecyclerViewAdapterInstructorQuiz extends RecyclerView.Adapter<RecyclerViewAdapterInstructorQuiz.InstructorQuizViewHolder> {

    private Context mContext;
    private List<Quiz> mData;

    public RecyclerViewAdapterInstructorQuiz(Context mContext, List<Quiz> mData){
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterInstructorQuiz.InstructorQuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.cardview_item_instructorquizes,parent,false);
        return new InstructorQuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterInstructorQuiz.InstructorQuizViewHolder holder, final int position) {
        Log.d("RecyclerViewForInstructor", mData.get(position).getTitle());
        holder.quiz_title.setText(mData.get(position).getTitle());
        holder.quiz_score.setText("Total Score: " + mData.get(position).getTotalScore());
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Intent intent = new Intent(mContext, Quiz_Activity.class);
                Intent intent = new Intent(mContext, InstructorQuizActivity.class);
                intent.putExtra("quizObject", mData.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class InstructorQuizViewHolder extends RecyclerView.ViewHolder{

        TextView quiz_title;
        TextView quiz_score;
        CardView cardView;

        public InstructorQuizViewHolder(View itemView){
            super(itemView);
            quiz_title = (TextView) itemView.findViewById(R.id.instructorQuiz_title_id);
            quiz_score = (TextView) itemView.findViewById(R.id.instructorQuiz_score_id);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }

}
