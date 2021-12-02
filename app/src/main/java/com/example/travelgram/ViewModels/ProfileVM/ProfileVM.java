package com.example.travelgram.ViewModels.ProfileVM;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.travelgram.DAO.ProfileDAO;
import com.example.travelgram.Models.User;

/* View model used for handling the request from the views:
 * OtherProfileFragment */
public class ProfileVM extends AndroidViewModel {
    private final ProfileDAO profileDAO;

    public ProfileVM(@NonNull Application application) {
        super(application);
        profileDAO = ProfileDAO.getInstance();
    }

    public void getUserInfo(String email) {
        profileDAO.getUserInfo(email);
    }

    public MutableLiveData<User> getUserInfoResponse() {
        return profileDAO.getUserInfoResponse();
    }

    public void getUserInfoByUsername(String username) {
        profileDAO.getUserInfoByUsername(username);
    }

    public void followUnfollowProfile(String usernameToFollow, String email, String followBtnState) {
        profileDAO.followUnfollowProfile(usernameToFollow, email, followBtnState);
    }

    public MutableLiveData<String> getFollowResponse() {
        return profileDAO.getFollowResponse();
    }

    public void getFollowState(String username, String email) {
        profileDAO.getFollowState(username, email);
    }

    public void getFollowCount(String username) {
        profileDAO.getFollowCount(username);
    }

    public MutableLiveData<Long> getFollowCountResponse() {
        return profileDAO.getFollowCountResponse();
    }
}
