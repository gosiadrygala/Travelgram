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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.travelgram.Models.Place;
import com.example.travelgram.R;
import com.example.travelgram.ViewModels.PlaceVM.PlaceVM;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashMap;
import java.util.Map;

/* Class managing the comment fragment: displaying map with markers,
displaying the pop up window to create place */

public class MapsFragment extends Fragment {
    private View view;
    private PlaceVM placeVM;
    private PopupWindow popupWindow;
    private EditText nameOfThePlaceField;
    private EditText descriptionField;
    private ImageButton uploadPicture;
    private Button createPlaceButton;
    private Uri image;
    private static final int PICK_FROM_GALLERY = 1;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        GoogleMap googleMap;
        @SuppressLint({"MissingPermission", "NewApi"})
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                googleMap.setMyLocationEnabled(true);
                                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                            } else if (coarseLocationGranted != null && coarseLocationGranted) ;
                            else {
                            }
                        }
                );

        @Override
        public void onMapReady(GoogleMap googleMap) {
            this.googleMap = googleMap;
            googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                private float currentZoom = -1; //keep track of your current zoom level

                @Override
                public void onCameraChange(CameraPosition camera) {
                    LatLngBounds bounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
                    if (camera.zoom != currentZoom) {
                        currentZoom = camera.zoom;
                        /* Request all markers within specified bounds on the map */
                        placeVM.getMarkersInArea(bounds);
                    }
                }
            });

            listenForMarkerResponse(googleMap);

            /* When the window above the marker is clicked, redirect to the specified place channel */
            googleMap.setOnInfoWindowClickListener(p -> {
                LatLng position = p.getPosition();
                Bundle bundle = new Bundle();
                bundle.putStringArray("placeCoordinates", new String[]{String.valueOf(position.latitude),
                        String.valueOf(position.longitude)});
                Navigation.findNavController(view).navigate(R.id.action_map_to_place, bundle);
            });

            /* Setting map style to the one specified in the json file */
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.styles_json));

            /* Check if the permission for the location is enabled,
             * if yes, mark the location on the map, and enable the my location button,
             * if not request permission */
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                locationPermissionRequest.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
            }
            /* Enable components on the map */
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(true);

            /* Opening a create place pop window on the long click on the map */
            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    PopUpWindow(latLng);
                }
            });
        }
    };

    private void listenForMarkerResponse(GoogleMap googleMap) {
        placeVM.getMarkerResponse().observe(getViewLifecycleOwner(), new Observer<HashMap<LatLng, String>>() {
            @Override
            public void onChanged(HashMap<LatLng, String> stringLatLngHashMap) {
                for (Map.Entry<LatLng, String> entry : stringLatLngHashMap.entrySet()) {
                    String key = entry.getValue();
                    LatLng latLng = entry.getKey();
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(key).icon(BitmapDescriptorFactory.fromResource(R.drawable.mappin)));
                }
            }
        });
    }

    /* Pop up window used for creating a place */
    @SuppressLint("ResourceAsColor")
    private void PopUpWindow(LatLng latLng) {
        View popupView = getLayoutInflater().inflate(R.layout.fragment_pop_up_window, null);

        popupWindow = new PopupWindow(popupView);
        popupWindow.setWidth((int) (getView().getWidth() * 0.70));
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        nameOfThePlaceField = popupView.findViewById(R.id.nameOfThePlaceField);
        descriptionField = popupView.findViewById(R.id.descriptionField);
        uploadPicture = popupView.findViewById(R.id.uploadPicture);
        createPlaceButton = popupView.findViewById(R.id.createPlaceButton);

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

        createPlaceButton.setOnClickListener(b -> createPlace(latLng));

        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }

    /* Method used to request the creation of the place from VM */
    private void createPlace(LatLng latLng) {
        Place place = new Place(nameOfThePlaceField.getText().toString(),
                descriptionField.getText().toString(),
                String.valueOf(latLng.latitude), String.valueOf(latLng.longitude),
                "", null);
        placeVM.createPlace(place, image);
    }

    /* Request permission to access the gallery when upload picture icon is clicked */
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
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maps, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        placeVM = new ViewModelProvider(getActivity()).get(PlaceVM.class);
        observingTheCreatePlaceResponse();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    /* Method observing the create place response, place created successfully - dismiss window, failed - make toast */
    private void observingTheCreatePlaceResponse() {
        placeVM.getCreatePlaceResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("Place created.")) {
                    if(popupWindow != null)
                        popupWindow.dismiss();
                } else makeToast(s);
            }
        });
    }

    private void makeToast(String response) {
        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
    }

}