package com.example.travelgram.Views.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelgram.Adapter.PostAdapter;
import com.example.travelgram.Models.Place;
import com.example.travelgram.Models.Post;
import com.example.travelgram.Models.WeatherResponse;
import com.example.travelgram.R;
import com.example.travelgram.ViewModels.PlaceVM.PlaceVM;
import com.example.travelgram.ViewModels.SignInSignUpVM.SignInSignUpVM;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlaceFragment extends Fragment {
    private View view;
    private PlaceVM placeVM;
    private PopupWindow popupWindow;
    private Uri image;

    private ImageView placeImage;
    private TextView nameOfThePlace;
    private FloatingActionButton followBtn;
    private TextView descriptionOfThePlace;
    private EditText createAPostTextFieldMulti;
    private RadioButton radioButton;
    private EditText contentField;
    private ImageButton uploadPicture;
    private Button createPostButton;
    private MutableLiveData<Place> currentPlace;
    private SignInSignUpVM signInSignUpVM;

    private static final int PICK_FROM_GALLERY = 1;

    private RecyclerView postsList;
    private PostAdapter postAdapter;

    private boolean followBtnState;
    private String placeID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_place, container, false);

        signInSignUpVM = new ViewModelProvider(getActivity()).get(SignInSignUpVM.class);
        placeVM = new ViewModelProvider(getActivity()).get(PlaceVM.class);

        currentPlace = new MutableLiveData<>();

        placeImage = view.findViewById(R.id.placeImage);

        nameOfThePlace = view.findViewById(R.id.nameOfThePlace);
        followBtn = view.findViewById(R.id.floatingActionButton);
        descriptionOfThePlace = view.findViewById(R.id.descriptionOfThePlace);

        String[] placeCoordinates = requireArguments().getStringArray("placeCoordinates");
        LatLng placeCoordinates1 = new LatLng(Double.parseDouble(placeCoordinates[0]), Double.parseDouble(placeCoordinates[1]));

        placeVM.getPlaceInfo(placeCoordinates1);

        listenForPlaceInfoResponse();

        listenForPostsResponse();

        createAPostTextFieldMulti = view.findViewById(R.id.createAPostTextFieldMulti);

        createAPostTextFieldMulti.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction())
                    PopUpWindow();
                return false;
            }
        });

        placeVM.getCreatePostToPlaceImageResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.equals("true"))
                    makeToast(s);
                else if (s.equals("true"))
                    if (popupWindow != null)
                        popupWindow.dismiss();
            }
        });

        placeVM.requestWeather(placeCoordinates1.latitude, placeCoordinates1.longitude);

        placeVM.getWeatherResponse().observe(getViewLifecycleOwner(), new Observer<WeatherResponse>() {
            @Override
            public void onChanged(WeatherResponse weather) {
                populateWeather(weather);
            }
        });


        postsList = view.findViewById(R.id.rvPost);
        postsList.hasFixedSize();
        postsList.setLayoutManager(new LinearLayoutManager(getContext()));

        placeVM.getFollowResponse().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    followBtnState = true;
                    followBtn.setImageResource(R.drawable.ic_heartfollowed1);
                } else {
                    followBtnState = false;
                    followBtn.setImageResource(R.drawable.ic_heartunfollowed);
                }
            }
        });
        return view;
    }

    private void checkFollowBtnState(String placeID) {
        placeVM.getFollowState(placeID,
                signInSignUpVM.getCurrentUser().getValue().getEmail());
    }

    private void listenForFollowButton(String placeID) {
        followBtn.setOnClickListener(v -> {
            placeVM.followUnfollowPlace(placeID,
                    signInSignUpVM.getCurrentUser().getValue().getEmail(), followBtnState);
        });
    }

    private void listenForPostsResponse() {
        placeVM.getPostsForPlaceResponse().observe(getViewLifecycleOwner(), new Observer<ArrayList<Post>>() {
            @Override
            public void onChanged(ArrayList<Post> posts) {
                if(posts != null || posts.size() != 0) {
                    postAdapter = new PostAdapter(posts, signInSignUpVM.getCurrentUser().getValue().getEmail());
                    postsList.setAdapter(postAdapter);
                }
            }
        });
    }

    private void listenForPlaceInfoResponse() {
        placeVM.getPlaceInfoResponse().observe(getViewLifecycleOwner(), new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                currentPlace.setValue(place);
                Picasso.get().load(place.getPlaceImageID()).into(placeImage);
                nameOfThePlace.setText(place.getPlaceName());
                descriptionOfThePlace.setText(place.getDescription());
                placeID = currentPlace.getValue().getPlaceID();
                checkFollowBtnState(placeID);
                listenForFollowButton(placeID);
                placeVM.getPostsForPlace(currentPlace.getValue().getPlaceID());
            }
        });
    }

    private void populateWeather(WeatherResponse weatherResponse) {
        TextView weather = (TextView) view.findViewById(R.id.weather);
        weather.setText(weatherResponse.getWeather().get(0).getDescription());
        TextView pressure = (TextView) view.findViewById(R.id.pressure);
        pressure.setText(String.valueOf(weatherResponse.getMain().getPressure()) + " Pa");
        TextView temperature = (TextView) view.findViewById(R.id.temperature);
        temperature.setText(String.valueOf(weatherResponse.getMain().getTemp()) + "°C");
        TextView sunrise = (TextView) view.findViewById(R.id.sunrise);
        sunrise.setText(weatherResponse.getSys().getSunrise());
        TextView sunset = (TextView) view.findViewById(R.id.sunset);
        sunset.setText(weatherResponse.getSys().getSunset());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void PopUpWindow() {
        View popupView = getLayoutInflater().inflate(R.layout.fragment_share_experience_pop_up_window, null);

        popupWindow = new PopupWindow(popupView);
        popupWindow.setWidth((int) (getView().getWidth() * 0.70));
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        contentField = popupView.findViewById(R.id.contentField);
        uploadPicture = popupView.findViewById(R.id.uploadPicture);
        createPostButton = popupView.findViewById(R.id.createPostButton);
        radioButton = popupView.findViewById(R.id.radioButton);
        radioButton.setClickable(false);
        radioButton.setChecked(false);

        uploadPicture.setOnClickListener(b -> {
            try {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PICK_FROM_GALLERY);
                } else {
                    Intent gallery = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(gallery);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        createPostButton.setOnClickListener(b -> createPost());

        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }

    private void createPost() {
        placeVM.createPostToPlace(currentPlace.getValue(), contentField.getText().toString(), image, signInSignUpVM.getCurrentUser().getValue().getEmail());
    }

    private void makeToast(String response) {
        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent gallery = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(gallery);
                } else {
                    Toast.makeText(getContext(), "To upload an image of the place you " +
                            "need to enable the access to your files in settings of your device.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getData() != null) {
                    image = result.getData().getData();
                    radioButton.setChecked(true);
                }
            });
}