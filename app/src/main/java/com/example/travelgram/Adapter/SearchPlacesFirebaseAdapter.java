package com.example.travelgram.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelgram.Models.Place;
import com.example.travelgram.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

/* The adapter handles the data collection about posts searched by the user (search item)
 * taken from the Realtime database and binds it to the view */

public class SearchPlacesFirebaseAdapter extends FirebaseRecyclerAdapter<Place, SearchPlacesFirebaseAdapter.ViewHolder> {

    private final SearchPlacesFirebaseAdapter.OnListItemClickListener mOnListItemClickListener;

    public SearchPlacesFirebaseAdapter(@NonNull FirebaseRecyclerOptions<Place> options, SearchPlacesFirebaseAdapter.OnListItemClickListener listener) {
        super(options);
        mOnListItemClickListener = listener;
    }


    /* Method binding data from the data source to each search item */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull SearchPlacesFirebaseAdapter.ViewHolder holder, int position, @NonNull Place model) {
        Picasso.get().load(model.getPlaceImageID()).into(holder.searchPicture);
        holder.searchName.setText(model.getPlaceName());
        holder.hiddenID.setText(model.getLatitude() + "," + model.getLongitude());
    }

    @NonNull
    @Override
    public SearchPlacesFirebaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_item_layout, parent, false);
        return new SearchPlacesFirebaseAdapter.ViewHolder(view);
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
            mOnListItemClickListener.onListItemClick(hiddenID.getText().toString());
        }

    }

    /* Interface providing a method for reacting to clicking on the search item */
    public interface OnListItemClickListener {
        void onListItemClick(String ID);
    }
}