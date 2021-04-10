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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.MyViewHolder> {

    ArrayList<Date> timeData;
    ArrayList<String> titleData;
    ArrayList<String> overviewData;
    ArrayList<String> typeData;
    ArrayList<String> categoryData;
    ArrayList<Integer> postcodeData;
    ArrayList<Boolean> analysedData;
    ArrayList<String> indigenousHeritageData;
    ArrayList<String> indigenousPreservationData;
    ArrayList<String> indigenousExposureData;
    ArrayList<String> identifierData;
    String user;
    Context context;

    public HistoryListAdapter(ArrayList<Date> timeData, ArrayList<String> titleData, ArrayList<String> overviewData, ArrayList<String> typeData, ArrayList<String> categoryData, ArrayList<Integer> postcodeData, ArrayList<Boolean> analysedData, ArrayList<String> indigenousHeritageData, ArrayList<String> indigenousPreservationData, ArrayList<String> indigenousExposureData, ArrayList<String> identifierData, String user, Context context) {
        this.timeData = timeData;
        this.titleData = titleData;
        this.overviewData = overviewData;
        this.typeData = typeData;
        this.categoryData = categoryData;
        this.postcodeData = postcodeData;
        this.analysedData = analysedData;
        this.indigenousHeritageData = indigenousHeritageData;
        this.indigenousPreservationData = indigenousPreservationData;
        this.indigenousExposureData = indigenousExposureData;
        this.identifierData = identifierData;
        this.user = user;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_history_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        switch (typeData.get(position)) {
            case "text":
                holder.mIcon.setImageResource(R.drawable.ic_baseline_text_fields_24);
                break;
            case "image":
                holder.mIcon.setImageResource(R.drawable.ic_baseline_image_24);
                break;
            case "video":
                holder.mIcon.setImageResource(R.drawable.ic_baseline_videocam_24);
                break;
        }
        if (analysedData.get(position)) {
            holder.mAnalysed.setImageResource(R.drawable.custom_circle_5);
        } else {
            holder.mAnalysed.setImageResource(R.drawable.custom_circle_2);
        }
        holder.mTitle.setText(titleData.get(position));
        SimpleDateFormat format = new SimpleDateFormat("dd MMM YYYY");
        String date = format.format(timeData.get(position)).toUpperCase();
        holder.mDate.setText(date);
        FirebaseFirestore.getInstance().collection("lands").whereEqualTo("postcode", postcodeData.get(position)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                String land = "";
                for (DocumentSnapshot document: snapshotList) {
                    land = String.format("%s" + ", %d", document.getString("suburb"), postcodeData.get(position));
                }
                holder.mLocation.setText(land);
            }
        });
        holder.mOverview.setText(overviewData.get(position));
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HistoryInfoActivity.class);
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

        ImageView mIcon;
        ImageView mAnalysed;
        TextView mTitle;
        TextView mDate;
        TextView mLocation;
        TextView mOverview;
        ConstraintLayout mLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.ivHistoryRowItemIcon);
            mAnalysed = itemView.findViewById(R.id.ivHistoryRowItemAnalysed);
            mTitle = itemView.findViewById(R.id.tvHistoryRowItemTitle);
            mDate = itemView.findViewById(R.id.tvHistoryRowItemDate);
            mLocation = itemView.findViewById(R.id.tvHistoryRowItemLocation);
            mOverview = itemView.findViewById(R.id.tvHistoryRowItemOverview);
            mLayout = itemView.findViewById(R.id.clHistoryRowItemMainLayout);
        }
    }
}
