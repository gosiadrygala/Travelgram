package com.example.travelgram.Views.Fragments;

import android.graphics.Canvas;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.travelgram.Adapter.CommentFirebaseAdapter;
import com.example.travelgram.Controller.SwipeController;
import com.example.travelgram.Models.Comment;
import com.example.travelgram.R;
import com.example.travelgram.ViewModels.PlaceVM.PlaceVM;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/* Class managing the comment fragment: displaying comments, adding and deleting comments */

public class CommentFragment extends Fragment implements CommentFirebaseAdapter.OnListItemClickListener {
    private View view;
    private String postID;
    private String userEmail;
    private PlaceVM placeVM;
    private EditText commentContent;
    private ImageButton createCommentBtn;
    private RecyclerView recyclerView;
    private CommentFirebaseAdapter commentFirebaseAdapter;
    private SwipeController swipeController;
    private String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] userEmailpostID = requireArguments().getStringArray("userEmailpostID");
        userEmail = userEmailpostID[0];
        postID = userEmailpostID[1];
        placeVM = new ViewModelProvider(requireActivity()).get(PlaceVM.class);
    }

    /* Method used to set the Firebase adapter for the comment recycler view
    * and attaching the swipe controller to enable the delete button for the comment */
    private void setAdapterAndSwipeController() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("comments").child(postID);
        FirebaseRecyclerOptions<Comment> options
                = new FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(reference.orderByChild("dateOfCreation"), Comment.class)
                .build();


        /* Connecting object of required Adapter class to
        * the Adapter class itself */
        commentFirebaseAdapter = new CommentFirebaseAdapter(options, this);
        /* Connecting Adapter class with the Recycler view */
        recyclerView.setAdapter(commentFirebaseAdapter);
        commentFirebaseAdapter.startListening();
        setupRecyclerView();
        swipeController = new SwipeController(new SwipeController.SwipeControllerActions() {
            @Override
            public void onRightClicked(String commentID) {
               placeVM.deleteComment(postID, commentID);
            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    /* Method used for adding the delete buttons to each item from the view - comment item */
    private void setupRecyclerView() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c, username);
            }
        });
    }

    /* Function to tell the app to start getting
     * data from database */
    @Override
    public void onStart() {
        super.onStart();
        if(commentFirebaseAdapter != null)
            commentFirebaseAdapter.startListening();
    }

    /* Function to tell the app to stop getting
    * data from database on stopping */
    @Override public void onStop()
    {
        super.onStop();
        commentFirebaseAdapter.stopListening();
    }

    public void createComment(View view) {
        String commentContent = this.commentContent.getText().toString();
        placeVM.createComment(postID, userEmail, commentContent);
        this.commentContent.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comment, container, false);

        commentContent = view.findViewById(R.id.commentField);
        createCommentBtn = view.findViewById(R.id.addCommentButton);
        recyclerView = view.findViewById(R.id.recycleViewComments);
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        createCommentBtn.setOnClickListener(this::createComment);

        /* This method is called to get a username to then further pass the username to the adapter
        so that the delete button is only enabled for the owner of the comment */
        placeVM.getUsernameByEmail(userEmail);

        observingTheUsernameResponse();

        setAdapterAndSwipeController();

        observingTheCreateCommentResponse();

        return view;
    }

    private void observingTheCreateCommentResponse() {
        placeVM.getCreateCommentResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String commentResponse) {
                        makeToast(commentResponse);
                    }
                }
        );
    }

    private void observingTheUsernameResponse() {
        placeVM.getUsernameResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String username) {
                        setUsername(username);
                    }
                }
        );
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private void makeToast(String response) {
        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
    }

    /* Method used for redirecting to the profile of the selected user from the comment */
    @Override
    public void onListItemClickProfile(String username) {
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        Navigation.findNavController(view).navigate(R.id.action_commentFragment_to_otherProfile, bundle);
    }
}