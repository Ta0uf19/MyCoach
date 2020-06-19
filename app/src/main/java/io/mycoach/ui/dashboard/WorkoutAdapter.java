package io.mycoach.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.mycoach.R;

public class TrainingAdapter extends RecyclerView<TrainingAdapter.ViewHolder> {

    public TrainingAdapter(@NonNull Context context) {
        super(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textAlbumName;
        private ImageView imageView;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            textAlbumName = itemView.findViewById(R.id.textAlbumName);
            imageView = itemView.findViewById(R.id.imageAlbum);
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public TrainingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_album, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


    }
}
