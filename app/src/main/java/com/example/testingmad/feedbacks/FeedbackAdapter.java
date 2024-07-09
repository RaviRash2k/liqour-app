package com.example.testingmad.feedbacks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testingmad.R;

import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MainViewHolder> {

    Context context;
    ArrayList<FeedbackModel> list;

    public FeedbackAdapter(Context context, ArrayList<FeedbackModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.feedback, parent, false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        FeedbackModel model = list.get(position);

        // Set user
        holder.feedbackAddUser.setText(model.getUserName());

        // Set feedback
        holder.feedbackComment.setText(model.getFeedback());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        TextView feedbackAddUser;
        TextView feedbackComment;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            feedbackAddUser = itemView.findViewById(R.id.feedbackAddUser);
            feedbackComment = itemView.findViewById(R.id.feedbackComment);
        }
    }
}
