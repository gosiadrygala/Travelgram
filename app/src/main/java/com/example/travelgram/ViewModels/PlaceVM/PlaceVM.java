package com.example.travelgram.ViewModels.PlaceVM;

import android.app.Application;
import android.media.Image;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.travelgram.API.ServiceGenerator;
import com.example.travelgram.API.WeatherAPI;
import com.example.travelgram.DAO.PlaceDAO;
import com.example.travelgram.Models.Place;
import com.example.travelgram.Models.Post;
import com.example.travelgram.Models.WeatherResponse;
import com.example.travelgram.Repository.PlaceRepo;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceVM extends AndroidViewModel {

    private final PlaceRepo placeRepo;
    private MutableLiveData<String> createPlaceResponse;
    private final PlaceDAO placeDAO;
    private MutableLiveData<WeatherResponse> weatherResponse;

    public PlaceVM(@NotNull Application app) {
        super(app);
        placeRepo = PlaceRepo.getInstance(app);
        createPlaceResponse = new MutableLiveData<>();
        placeDAO = PlaceDAO.getInstance();
        weatherResponse = new MutableLiveData<>();
    }

    public void createPlace(Place place, Uri image) {
        if(place.getPlaceName().equals("") || place.getDescription().equals(""))
            setCreatePlaceResponse("Name of the place or description of the place is empty.");
        else if(image == null)
            setCreatePlaceResponse("Image of the place is empty.");
        else {
            placeRepo.createPlaceImage(place, image);
        }
    }

    public MutableLiveData<String> getCreatePlaceResponse() {
        return placeRepo.getCreatePlaceResponse();
    }

    public void setCreatePlaceResponse(String response) {
        placeRepo.setCreatePlaceResponse(response);
    }

    public void getMarkersInArea(LatLngBounds bounds) {
        placeDAO.getMarkersInArea(bounds);
    }

    public MutableLiveData<HashMap<LatLng, String>> getMarkerResponse() {
        return placeDAO.getMarkerResponse();
    }

    public void getPlaceInfo(LatLng position) {
        placeDAO.getPlaceInfo(position);
    }

    public MutableLiveData<HashMap<Place, Image>> getPlaceInfoResponse() {
        return placeDAO.getPlaceInfoResponse();
    }

    public void getPlacePicture(String placeID) {
        placeRepo.getPlacePicture(placeID);
    }

    public MutableLiveData<HashMap<String, byte[]>> getPlacePictureResponse() {
        return placeRepo.getGetPlacePictureResponse();
    }

    public void createPostToPlace(Place place, String postContent, Uri image) {
        if(place.getPlaceID().equals("") || place.getPlaceID().isEmpty()) {
            setCreatePostToPlaceImageResponse("Something went wrong!");
        } else if(postContent.length() < 5) {
            setCreatePostToPlaceImageResponse("Post content is too short");
        } else if(image == null) {
            setCreatePostToPlaceImageResponse("Add image to create a post");
        }
        else {
            Post post = new Post("", postContent, "", 0, "", "userID", null);
            placeRepo.createPostToPlaceImage(place, post, image);
        }
        //else {
          //  String dateAndTime = Calendar.getInstance().getTime().toString();
          //  UUID uuid = UUID.nameUUIDFromBytes(dateAndTime.getBytes());
           // Post post = new Post(uuid.toString(), postContent, "", 0, dateAndTime, "userID", null);
            //placeDAO.createPost(place, post);
      //  }
    }

    private void setCreatePostToPlaceImageResponse(String response) {
        placeDAO.setCreatePostToPlaceImageResponse(response);
    }

    public MutableLiveData<String> getCreatePostToPlaceImageResponse() {
        return placeDAO.getCreatePostToPlaceImageResponse();
    }

    public void requestWeather(double lat, double lon ) {
        WeatherAPI weatherAPI = ServiceGenerator.getWeatherAPI();
        Call<WeatherResponse> call = weatherAPI.getWeather(lat, lon);


        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    double tempKel = response.body().getMain().getTemp();
                    int temp = (int) (tempKel - 273.15);
                    response.body().getMain().setTemp(temp);

                    long sunrise = Integer.parseInt(response.body().getSys().getSunrise());
                    long sunset = Integer.parseInt(response.body().getSys().getSunset());

                    String sunriseDate = getDate(sunrise);
                    String sunsetDate = getDate(sunset);

                    response.body().getSys().setSunrise(sunriseDate);
                    response.body().getSys().setSunset(sunsetDate);

                    weatherResponse.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.i("Retrofit", t.getMessage());
            }
        });
    }

    @NonNull
    private String getDate(long time) {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date timeDate = new Date(time *1000);
        String format = sdf.format(timeDate);
        return format;
    }

    public MutableLiveData<WeatherResponse> getWeatherResponse() { return weatherResponse; }
}
