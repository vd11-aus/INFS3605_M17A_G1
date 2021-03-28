package com.example.custodian;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderboardThreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderboardThreeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LeaderboardThreeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderboardThreeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderboardThreeFragment newInstance(String param1, String param2) {
        LeaderboardThreeFragment fragment = new LeaderboardThreeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView mIcon;
    private TextView mUsernameText;
    private TextView mPointsText;

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
        View view = inflater.inflate(R.layout.fragment_leaderboard_three, container, false);

        mIcon = view.findViewById(R.id.ivLeaderboardThreeIcon);
        mUsernameText = view.findViewById(R.id.tvLeaderboardThreeUsername);
        mPointsText = view.findViewById(R.id.tvLeaderboardThreePoints);

        ArrayList<Integer> pointArray = new ArrayList<Integer>();
        ArrayList<String> usernameArray = new ArrayList<String>();
        ArrayList<String> idArray = new ArrayList<String>();
        FirebaseFirestore firebaseFS = FirebaseFirestore.getInstance();
        firebaseFS.collection("users").orderBy("alltimepoints", Query.Direction.DESCENDING).limit(3).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot snapshot: snapshotList) {
                    System.out.println("onSuccess: " + snapshot.getData());
                    pointArray.add(snapshot.getLong("alltimepoints").intValue());
                    usernameArray.add(snapshot.getString("username"));
                    idArray.add(snapshot.getString("uniqueid"));
                }
                mUsernameText.setText(usernameArray.get(2));
                mPointsText.setText(pointArray.get(2).toString() + "pts");
                FirebaseStorage.getInstance().getReference().child("profileicons/").child(idArray.get(2)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getActivity()).load(uri).into(mIcon);
                    }
                });
            }
        });
        return view;
    }
}