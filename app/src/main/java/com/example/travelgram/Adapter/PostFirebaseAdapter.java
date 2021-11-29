package com.example.travelgram.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelgram.Models.Post;
import com.example.travelgram.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostFirebaseAdapter extends FirebaseRecyclerAdapter<Post, PostFirebaseAdapter.ViewHolder> {

    private String userEmail;
    private String placeID;
    private final OnListItemClickListener mOnListItemClickListener;

    public PostFirebaseAdapter(@NonNull FirebaseRecyclerOptions<Post> options, String userEmail, OnListItemClickListener listener, String placeID) {
        super(options);
        this.userEmail = userEmail;
        mOnListItemClickListener = listener;
        this.placeID = placeID;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post model) {
        System.out.println(model.getPostID());
        holder.username.setText(model.getUsername());

        holder.postDescription.setText(model.getContent());

        Picasso.get().load(model.getPostPicture()).into(holder.postPicture);

        holder.dateOfCreation.setText(model.getDateOfCreation());

        Picasso.get().load(model.getUserPicture()).into(holder.profilePicture);

        holder.postId.setText(model.getPostID());

        List<String> likedByUsers = model.getLikedByUsers();
        if(likedByUsers != null) {
            boolean contains = likedByUsers.contains(userEmail);
            if (contains)
                holder.likePost.setImageResource(R.drawable.ic_heartfollowed1);
            else
                holder.likePost.setImageResource(R.drawable.ic_heartunfollowed);
        }

        if(!model.getUserEmail().equals(userEmail))
            holder.deleteButton.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_layout, parent, false);
        return new PostFirebaseAdapter.ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profilePicture;
        TextView username;
        ImageButton deleteButton;
        ImageView postPicture;
        ImageButton likePost;
        ImageButton commentPost;
        TextView postDescription;
        TextView dateOfCreation;
        TextView postId;

        ViewHolder(View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profilePicture);
            username = itemView.findViewById(R.id.usernamePost);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            postPicture = itemView.findViewById(R.id.postImage);
            likePost = itemView.findViewById(R.id.likePostButton);
            commentPost = itemView.findViewById(R.id.commentButton);
            postDescription = itemView.findViewById(R.id.postDescription);
            dateOfCreation = itemView.findViewById(R.id.dateOfCreation);
            postId = itemView.findViewById(R.id.postId);

            likePost.setOnClickListener(r -> mOnListItemClickListener.onListItemClickLike(postId.getText().toString()));
            deleteButton.setOnClickListener(this);
            commentPost.setOnClickListener(r -> mOnListItemClickListener.onListItemClickComment(postId.getText().toString()));
            username.setOnClickListener(r -> mOnListItemClickListener.onListItemClickProfile(username.getText().toString()));
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(postId.getText().toString());
        }

    }

    public interface OnListItemClickListener {
        void onListItemClick(String postId);
        void onListItemClickLike(String postId);
        void onListItemClickComment(String postId);
        void onListItemClickProfile(String userEmail);
    }
}
