package com.example.custodian;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.MyViewHolder> {

    ArrayList<String> identifierData;
    ArrayList<Date> timeData;
    ArrayList<String> titleData;
    ArrayList<String> idData;
    ArrayList<String> authorData;
    ArrayList<String> contentData;
    String user;
    Context context;

    public FeedListAdapter(Context ct, ArrayList<String> identifiers, ArrayList<Date> times, ArrayList<String> titles, ArrayList<String> ids, ArrayList<String> authors, ArrayList<String> contents, String userId) {
        context = ct;
        identifierData = identifiers;
        titleData = titles;
        timeData = times;
        idData = ids;
        authorData = authors;
        contentData = contents;
        user = userId;
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
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FeedInfoActivity.class);
                intent.putExtra("IDENTIFIER", identifierData.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titleData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTime;
        TextView mTitle;
        ConstraintLayout mLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tvFeedRowItemTitle);
            mTime = itemView.findViewById(R.id.tvFeedRowItemDate);
            mLayout = itemView.findViewById(R.id.clFeedRowItemMainLayout);
        }
    }
}
