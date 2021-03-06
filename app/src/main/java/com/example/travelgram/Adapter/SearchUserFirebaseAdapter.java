package com.example.travelgram.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelgram.Models.User;
import com.example.travelgram.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

/* The adapter handles the data collection about users searched by the user (search item)
 * taken from the Realtime database and binds it to the view */

public class SearchUserFirebaseAdapter extends FirebaseRecyclerAdapter<User, SearchUserFirebaseAdapter.ViewHolder> {

    private final SearchUserFirebaseAdapter.OnListItemClickListenerUser mOnListItemClickListener;

    public SearchUserFirebaseAdapter(@NonNull FirebaseRecyclerOptions<User> options, SearchUserFirebaseAdapter.OnListItemClickListenerUser listener) {
        super(options);
        mOnListItemClickListener = listener;
    }

    /* Method binding data from the data source to each search item */
    @Override
    protected void onBindViewHolder(@NonNull SearchUserFirebaseAdapter.ViewHolder holder, int position, @NonNull User model) {
        Picasso.get().load(model.getPictureID()).into(holder.searchPicture);
        holder.searchName.setText(model.getUsername());
        holder.hiddenID.setText(model.getEmail());
    }

    @NonNull
    @Override
    public SearchUserFirebaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_item_layout, parent, false);
        return new SearchUserFirebaseAdapter.ViewHolder(view);
    }


    /* The view holder caching the views associated with each search item */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView searchPicture;
        TextView searchName;
        TextView hiddenID;
        LinearLayout searchResult;

        ViewHolder(View itemView) {
            super(itemView);
            searchPicture = itemView.findViewById(R.id.searchPicture);
            searchName = itemView.findViewById(R.id.name);
            hiddenID = itemView.findViewById(R.id.hiddenID);
            searchResult = itemView.findViewById(R.id.resultSearch);

            searchResult.setOnClickListener(r -> onClick(itemView));
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClickUser(hiddenID.getText().toString());
        }

    }

    /* Interface providing a method for reacting to clicking on the search item */
    public interface OnListItemClickListenerUser {
        void onListItemClickUser(String ID);
    }
}
