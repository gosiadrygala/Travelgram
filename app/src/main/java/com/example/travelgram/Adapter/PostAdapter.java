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
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


/* Post Adapter used to initialize Recycler View */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<Post> postsList;
    private String user;

    public PostAdapter(ArrayList<Post> postsList, String user) {
        this.postsList = postsList;
        this.user = user;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        holder.username.setText(postsList.get(position).getUsername());
        holder.postDescription.setText(postsList.get(position).getContent());
        Picasso.get().load(postsList.get(position).getPostPicture()).into(holder.postPicture);
        Picasso.get().load(postsList.get(position).getUserPicture()).into(holder.profilePicture);
        if(!postsList.get(position).getUserEmail().equals(user)){
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        TextView username;
        ImageButton deleteButton;
        ImageView postPicture;
        ImageButton likePost;
        ImageButton commentPost;
        TextView postDescription;

        ViewHolder(View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profilePicture);
            username = itemView.findViewById(R.id.usernamePost);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            postPicture = itemView.findViewById(R.id.postImage);
            likePost = itemView.findViewById(R.id.likePostButton);
            commentPost = itemView.findViewById(R.id.commentButton);
            postDescription = itemView.findViewById(R.id.postDescription);
        }
    }
}
