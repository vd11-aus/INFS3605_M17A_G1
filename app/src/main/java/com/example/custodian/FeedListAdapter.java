package com.example.custodian;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.MyViewHolder> {

    ArrayList<String> identifierData;
    ArrayList<Date> timeData;
    ArrayList<String> titleData;
    ArrayList<String> idData;
    ArrayList<String> authorData;
    ArrayList<String> contentData;
    ArrayList<Boolean> readData;
    String user;
    Context context;
    Dialog dialog;

    public FeedListAdapter(Context ct, ArrayList<String> identifiers, ArrayList<Date> times, ArrayList<String> titles, ArrayList<String> ids, ArrayList<String> authors, ArrayList<String> contents, ArrayList<Boolean> read, String userId, Dialog feedDialog) {
        context = ct;
        identifierData = identifiers;
        titleData = titles;
        timeData = times;
        idData = ids;
        authorData = authors;
        contentData = contents;
        readData = read;
        user = userId;
        dialog = feedDialog;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_feed_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mTitle.setText(titleData.get(position));
        SimpleDateFormat format = new SimpleDateFormat("dd MMM YYYY");
        String date = format.format(timeData.get(position)).toUpperCase();
        holder.mTime.setText(date);
        if (readData.get(position)) {
            holder.mIndicator.setVisibility(View.INVISIBLE);
        } else {
            holder.mIndicator.setVisibility(View.VISIBLE);
            holder.mTitle.setPadding(50, 0, 0, 0);
        }
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("read", true);
                FirebaseFirestore.getInstance().collection("messages").document(identifierData.get(position)).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.setContentView(R.layout.feed_info_view);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TextView mTitle = dialog.findViewById(R.id.tvFeedInfoTitle);
                        TextView mAuthor = dialog.findViewById(R.id.tvFeedInfoAuthor);
                        TextView mDate = dialog.findViewById(R.id.tvFeedInfoDate);
                        TextView mContent = dialog.findViewById(R.id.tvFeedInfoContent);
                        Button mCloseButton = dialog.findViewById(R.id.btFeedInfoClose);
                        mTitle.setText(titleData.get(position));
                        mAuthor.setText(authorData.get(position));
                        mContent.setText(contentData.get(position));
                        mDate.setText(date);
                        dialog.setCancelable(false);
                        mCloseButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ProfileActivity.class);
                                context.startActivity(intent);
                            }
                        });
                        dialog.show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return titleData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mIndicator;
        TextView mTime;
        TextView mTitle;
        ConstraintLayout mLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mIndicator = itemView.findViewById(R.id.ivFeedRowItemIndicator);
            mTitle = itemView.findViewById(R.id.tvFeedRowItemTitle);
            mTime = itemView.findViewById(R.id.tvFeedRowItemDate);
            mLayout = itemView.findViewById(R.id.clFeedRowItemMainLayout);
        }
    }
}
