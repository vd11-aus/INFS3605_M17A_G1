package com.example.custodian;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

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
    Dialog dialog;

    public FeedListAdapter(Context ct, ArrayList<String> identifiers, ArrayList<Date> times, ArrayList<String> titles, ArrayList<String> ids, ArrayList<String> authors, ArrayList<String> contents, String userId, Dialog feedDialog) {
        context = ct;
        identifierData = identifiers;
        titleData = titles;
        timeData = times;
        idData = ids;
        authorData = authors;
        contentData = contents;
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
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        dialog.dismiss();
                    }
                });
                dialog.show();
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
