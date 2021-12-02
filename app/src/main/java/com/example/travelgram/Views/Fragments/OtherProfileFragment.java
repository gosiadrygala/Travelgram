package com.example.travelgram.Views.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.travelgram.Models.User;
import com.example.travelgram.R;
import com.example.travelgram.ViewModels.ProfileVM.ProfileVM;
import com.example.travelgram.ViewModels.SignInSignUpVM.SignInSignUpVM;
import com.squareup.picasso.Picasso;

public class OtherProfileFragment extends Fragment {
    private View view;
    private ImageView profilePicture;
    private TextView usernameProfile;
    private TextView descriptionProfile;
    private TextView followCount;
    private Button followButtonProfile;
    private RecyclerView profileRecyclerView;

    private ProfileVM profileVM;
    private SignInSignUpVM signInSignUpVM;
    private User user;
    private String followBtnState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileVM = new ViewModelProvider(getActivity()).get(ProfileVM.class);
        signInSignUpVM = new ViewModelProvider(getActivity()).get(SignInSignUpVM.class);

        String email = requireArguments().getString("email");

        String username = null;
        if(email.equals("No email")) {
            username = requireArguments().getString("username");

            if(username.equals("No username"))
                profileVM.getUserInfo(signInSignUpVM.getCurrentUser().getValue().getEmail());
            else
                profileVM.getUserInfoByUsername(username);

        }
        else profileVM.getUserInfo(email);
    }

    public void setUser(User user) {
        this.user = user;

        profileVM.getFollowCount(user.getUsername());

        profileVM.getFollowState(user.getUsername(),
                signInSignUpVM.getCurrentUser().getValue().getEmail());

        Picasso.get().load(user.getPictureID()).into(profilePicture);
        usernameProfile.setText(user.getUsername());
        descriptionProfile.setText(user.getShortDescription());
        followCount.setText("0");
        if (user.getEmail().equals(signInSignUpVM.getCurrentUser().getValue().getEmail())){
            followButtonProfile.setVisibility(View.GONE);
        } else followButtonProfile.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        profilePicture = view.findViewById(R.id.profilePicture);
        usernameProfile = view.findViewById(R.id.usernameProfile);
        descriptionProfile = view.findViewById(R.id.descriptionProfile);
        followCount = view.findViewById(R.id.followCount);
        followButtonProfile = view.findViewById(R.id.followButtonProfile);
        profileRecyclerView = view.findViewById(R.id.profileRecyclerView);

        followButtonProfile.setVisibility(View.VISIBLE);

        followBtnState = followButtonProfile.getText().toString();

        followButtonProfile.setOnClickListener(r -> profileVM.followUnfollowProfile(user.getUsername(),
                signInSignUpVM.getCurrentUser().getValue().getEmail(), followBtnState));

        observeForUserInfoResponse();

        observerForFollowResponse();

        observingForFollowCountResponse();

        return view;
    }

    private void observeForUserInfoResponse() {
        profileVM.getUserInfoResponse().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setUser(user);
            }
        });
    }

    private void observerForFollowResponse() {
        profileVM.getFollowResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                followBtnState = s;
                followButtonProfile.setText(s);
            }
        });
    }

    private void observingForFollowCountResponse() {
        profileVM.getFollowCountResponse().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                followCount.setText(String.valueOf(aLong));
            }
        });
    }
}