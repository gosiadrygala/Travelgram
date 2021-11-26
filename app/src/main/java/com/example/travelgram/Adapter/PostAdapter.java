package com.example.travelgram.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.ArrayList;


/* Post Adapter used to initialize Recycler View */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<Post> postsList;

    public PostAdapter(ArrayList<Post> postsList) {
        this.postsList = postsList;
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
        //TODO profile pic and other pic
        holder.username.setText(postsList.get(position).getUserID());
        holder.postDescription.setText(postsList.get(position).getContent());
        Bitmap bm = BitmapFactory.decodeByteArray(postsList.get(position).getPicture(), 0, postsList.get(position).getPicture().length);
        holder.postPicture.setImageBitmap(bm);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePicture;
        TextView username;
        ImageButton moreOptions;
        ImageView postPicture;
        ImageButton likePost;
        ImageButton commentPost;
        TextView postDescription;

        ViewHolder(View itemView) {
            super(itemView);
            profilePicture = itemView.findViewById(R.id.profilePicture);
            username = itemView.findViewById(R.id.usernamePost);
            moreOptions = itemView.findViewById(R.id.moreButton);
            postPicture = itemView.findViewById(R.id.postImage);
            likePost = itemView.findViewById(R.id.likePostButton);
            commentPost = itemView.findViewById(R.id.commentButton);
            postDescription = itemView.findViewById(R.id.postDescription);
        }
    }
}
