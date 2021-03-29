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

import java.util.ArrayList;

public class RewardsListAdapter extends RecyclerView.Adapter<RewardsListAdapter.MyViewHolder> {

    ArrayList<String> titleData;
    ArrayList<String> descriptionData;
    ArrayList<String> imageData;
    ArrayList<String> idData;
    ArrayList<Integer> costData;
    Integer currentPoints;
    Context context;

    public RewardsListAdapter(Context ct, ArrayList<String> titles, ArrayList<String> descriptions, ArrayList<String> images, ArrayList<String> ids, ArrayList<Integer> costs, Integer points) {
        context = ct;
        titleData = titles;
        descriptionData = descriptions;
        imageData = images;
        idData = ids;
        costData = costs;
        currentPoints = points;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_rewards_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mTitle.setText(titleData.get(position));
        holder.mDescription.setText(descriptionData.get(position));
        if (currentPoints < costData.get(position)) {
            holder.mIndicator.setImageResource(R.drawable.custom_circle_2);
        } else {
            holder.mIndicator.setImageResource(R.drawable.custom_circle_1);
        }
        if (imageData.get(position) != null) {
            Glide.with(holder.mImage).load(imageData.get(position)).centerCrop().placeholder(R.drawable.custom_background_2)
                    .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(holder.mImage);
        } else {
            holder.mImage.setImageResource(R.drawable.custom_background_2);
        }

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RewardInfoActivity.class);
                intent.putExtra("id", idData.get(position));
                intent.putExtra("cost", costData.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titleData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mDescription;
        ConstraintLayout mBackground;
        ConstraintLayout mLayout;
        ImageView mIndicator;
        ImageView mImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tvRewardsRowItemTitle);
            mDescription = itemView.findViewById(R.id.tvRewardsRowItemDescription);
            mBackground = itemView.findViewById(R.id.clRewardsRowItemBackground);
            mIndicator = itemView.findViewById(R.id.ivRewardsRowItemIndicator);
            mImage = itemView.findViewById(R.id.ivRewardsRowItemImage);
            mLayout = itemView.findViewById(R.id.clRewardsRowItemMainLayout);
        }
    }
}
