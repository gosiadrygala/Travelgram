package com.example.travelgram.Views.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TabHost;
import com.example.travelgram.Adapter.SearchPlacesFirebaseAdapter;
import com.example.travelgram.Adapter.SearchUserFirebaseAdapter;
import com.example.travelgram.Models.Place;
import com.example.travelgram.Models.User;
import com.example.travelgram.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class SearchFragment extends Fragment implements SearchPlacesFirebaseAdapter.OnListItemClickListener, SearchUserFirebaseAdapter.OnListItemClickListenerUser {
    private View view;
    private EditText searchText;
    private TabHost fragmentTabHost;
    private TabHost.TabSpec placesTab;
    private TabHost.TabSpec peopleTab;
    private RecyclerView placesRecycledView;
    private RecyclerView peopleRecycledView;
    private String currentTab;
    private SearchPlacesFirebaseAdapter searchPlacesAdapter;
    private SearchUserFirebaseAdapter searchUsersAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        placesRecycledView = view.findViewById(R.id.placesRecyclerTab);
        placesRecycledView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        placesRecycledView.setLayoutManager(linearLayoutManager);

        peopleRecycledView = view.findViewById(R.id.peopleRecyclerTab);
        peopleRecycledView.hasFixedSize();
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        peopleRecycledView.setLayoutManager(linearLayoutManager1);


        fragmentTabHost = view.findViewById(R.id.tabHost);
        fragmentTabHost.setup();

        //Tab places
        placesTab = fragmentTabHost.newTabSpec("Places");
        placesTab.setContent(R.id.tab1);
        placesTab.setIndicator("Places");
        fragmentTabHost.addTab(placesTab);

        //Tab people
        peopleTab = fragmentTabHost.newTabSpec("People");
        peopleTab.setContent(R.id.tab2);
        peopleTab.setIndicator("People");
        fragmentTabHost.addTab(peopleTab);

        fragmentTabHost.setCurrentTab(0);
        currentTab = "Places";

        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                currentTab = tabId;
                searchText.setText("");
            }
        });

        searchText = view.findViewById(R.id.editTextSearch);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println(s.length());
                searchInFirebase(s.toString(), s.length());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchInFirebase(String searchText, int length) {
        if(currentTab.equals("Places")){
            if(length != 0){
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("places");
                Query firebaseSearchQuery = reference.orderByChild("placeName").startAt(searchText).endAt(searchText + "\uf8ff");
                FirebaseRecyclerOptions<Place> options
                        = new FirebaseRecyclerOptions.Builder<Place>()
                        .setQuery(firebaseSearchQuery, Place.class)
                        .build();

                searchPlacesAdapter = new SearchPlacesFirebaseAdapter(options, this);
                placesRecycledView.setAdapter(searchPlacesAdapter);
                searchPlacesAdapter.startListening();
            } else placesRecycledView.setAdapter(null);
        }
        else{
            if(length != 0){
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
                Query firebaseSearchQuery = reference.orderByChild("username").startAt(searchText).endAt(searchText + "\uf8ff");
                FirebaseRecyclerOptions<User> options
                        = new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(firebaseSearchQuery, User.class)
                        .build();

                searchUsersAdapter = new SearchUserFirebaseAdapter(options, this);
                peopleRecycledView.setAdapter(searchUsersAdapter);
                searchUsersAdapter.startListening();
            } else peopleRecycledView.setAdapter(null);
        }
    }

    @Override
    public void onListItemClick(String ID) {
        String[] result = ID.split(",");
        Bundle bundle = new Bundle();
        bundle.putStringArray("placeCoordinates", new String[]{String.valueOf(result[0]), String.valueOf(result[1])});
        Navigation.findNavController(view).navigate(R.id.action_search_to_place, bundle);
    }

    @Override
    public void onListItemClickUser(String ID) {
        Bundle bundle = new Bundle();
        bundle.putString("email", ID);
        Navigation.findNavController(view).navigate(R.id.action_search_to_otherProfile, bundle);
    }

    @Override
    public void onPause() {
        super.onPause();
        searchText.setText("");
    }
}