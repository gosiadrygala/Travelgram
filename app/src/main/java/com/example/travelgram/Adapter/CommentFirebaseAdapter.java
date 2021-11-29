package com.example.travelgram.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelgram.Models.Comment;
import com.example.travelgram.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class CommentFirebaseAdapter extends FirebaseRecyclerAdapter<Comment, CommentFirebaseAdapter.ViewHolder> {
    private final CommentFirebaseAdapter.OnListItemClickListener mOnListItemClickListener;
    public CommentFirebaseAdapter(@NonNull FirebaseRecyclerOptions<Comment> options, CommentFirebaseAdapter.OnListItemClickListener listener) {
        super(options);
        mOnListItemClickListener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comment model) {
        holder.commentAuthor.setText(model.getUsername());
        holder.commentDate.setText(model.getDateOfCreation());
        holder.commentContent.setText(model.getContent());
        holder.commentID.setText(model.getCommentID());
        Picasso.get().load(model.getUserPictureID()).into(holder.commentPictureAuthor);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment_layout, parent, false);
        return new CommentFirebaseAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView commentAuthor;
        TextView commentDate;
        TextView commentContent;
        ImageView commentPictureAuthor;
        TextView commentID;

        ViewHolder(View itemView) {
            super(itemView);
            commentAuthor = itemView.findViewById(R.id.commentAuthor);
            commentDate = itemView.findViewById(R.id.commentDate);
            commentContent = itemView.findViewById(R.id.commentContent);
            commentPictureAuthor = itemView.findViewById(R.id.commentPicture);
            commentID = itemView.findViewById(R.id.commentID);

            commentAuthor.setOnClickListener(r -> mOnListItemClickListener.onListItemClickProfile(commentAuthor.getText().toString()));
        }
    }

    public interface OnListItemClickListener {
        void onListItemClickProfile(String username);
    }
}
