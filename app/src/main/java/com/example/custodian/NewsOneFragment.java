package com.example.custodian;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsOneFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsOneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsOneFragment newInstance(String param1, String param2) {
        NewsOneFragment fragment = new NewsOneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView mBackgroundImage;
    private TextView mTitle;
    private TextView mDate;
    private Button mLink;

    FirebaseFirestore mFirebaseDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_one, container, false);

        mBackgroundImage = view.findViewById(R.id.ivNewsOneFragmentBackground);
        mTitle = view.findViewById(R.id.tvNewsOneFragmentTitle);
        mDate = view.findViewById(R.id.tvNewsOneFragmentDate);
        mLink = view.findViewById(R.id.btNewsOneFragmentLink);

        mFirebaseDB = FirebaseFirestore.getInstance();
        DocumentReference referencePathOne = mFirebaseDB.document("news/newsone");
        referencePathOne.addSnapshotListener((Activity) getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String title = value.getString("title");
                String date = value.getString("date");
                String link = value.getString("link");
                String image = value.getString("image");
                mTitle.setText(title);
                mDate.setText(date);
                Glide.with(mBackgroundImage).load(image).centerCrop().placeholder(R.drawable.custom_background_2)
                        .error(R.drawable.custom_background_2).fallback(R.drawable.custom_background_2).into(mBackgroundImage);
                mLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Redirecting ...");
                        Intent intent = new Intent(getActivity(), RedirectActivity.class);
                        intent.putExtra("LINK_LOCATION", link);
                        startActivity(intent);
                    }
                });
            }
        });
        return view;
    }
}