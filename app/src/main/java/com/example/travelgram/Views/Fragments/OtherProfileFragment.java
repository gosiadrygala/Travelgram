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

/* Class managing the profile fragment */

public class OtherProfileFragment extends Fragment {
    private View view;
    private ImageView profilePicture;
    private TextView usernameProfile;
    private TextView descriptionProfile;
    private TextView followCount;
    private Button followButtonProfile;
    private RecyclerView profileRecyclerView; //TODO DELETE?
    private ProfileVM profileVM;
    private SignInSignUpVM signInSignUpVM;
    private User user;
    private String followBtnState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileVM = new ViewModelProvider(getActivity()).get(ProfileVM.class);
        signInSignUpVM = new ViewModelProvider(getActivity()).get(SignInSignUpVM.class);

        /* Check whether the user is redirecting from the search action or
        by accessing this fragment from the bottom navigation, and based on that
         request user info for this specific profile */
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

        /* Observe the follow button */
        followButtonProfile.setOnClickListener(r -> profileVM.followUnfollowProfile(user.getUsername(),
                signInSignUpVM.getCurrentUser().getValue().getEmail(), followBtnState));

        observeForUserInfoResponse();

        observerForFollowResponse();

        observingForFollowCountResponse();

        return view;
    }

    /* Set the user after getting the user info response */
    private void observeForUserInfoResponse() {
        profileVM.getUserInfoResponse().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                setUser(user);
            }
        });
    }

    /* Method used for setting up the profile of the user */
    public void setUser(User user) {
        this.user = user;

        profileVM.getFollowCount(user.getUsername());

        profileVM.getFollowState(user.getUsername(),
                signInSignUpVM.getCurrentUser().getValue().getEmail());

        Picasso.get().load(user.getPictureID()).into(profilePicture);
        usernameProfile.setText(user.getUsername());
        descriptionProfile.setText(user.getShortDescription());
        followCount.setText("0");

        /* If the user visits its own profile, make the follow button gone, otherwise visible */
        if (user.getEmail().equals(signInSignUpVM.getCurrentUser().getValue().getEmail())){
            followButtonProfile.setVisibility(View.GONE);
        } else followButtonProfile.setVisibility(View.VISIBLE);
    }

    /* Observe the follow response, sets the button accordingly to the response follow/ unfollow */
    private void observerForFollowResponse() {
        profileVM.getFollowResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                followBtnState = s;
                followButtonProfile.setText(s);
            }
        });
    }

    /* Setting the number of followers */
    private void observingForFollowCountResponse() {
        profileVM.getFollowCountResponse().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                followCount.setText(String.valueOf(aLong));
            }
        });
    }
}