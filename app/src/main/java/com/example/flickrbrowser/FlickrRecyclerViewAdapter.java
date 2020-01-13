package com.example.flickrbrowser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecyclerViewAdapt";
    private List<Photo> memberPhotoList;
    private Context memberContext;

    public FlickrRecyclerViewAdapter(List<Photo> memberPhotoList, Context memberContext) {
        this.memberPhotoList = memberPhotoList;
        this.memberContext = memberContext;
    }

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //called by layout manager when it needs new view
        Log.d(TAG, "onCreateViewHolder: New view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
    //called by the layout manager when it needs more data in existing row
        if(memberPhotoList==null || memberPhotoList.size() == 0)
        {
            holder.thumbnail.setImageResource(R.drawable.place_holder);
            holder.title.setText("No Photos Match of your search, use the search icon to search for photos");
        }
        else {
            Photo photoItem = memberPhotoList.get(position);
            Log.d(TAG, "onBindViewHolder: " + photoItem.getMemberTitle() + "---->" + position);
            Picasso.get().load(photoItem.getMemberImage())
                    .error(R.drawable.place_holder)
                    .placeholder(R.drawable.place_holder)
                    .into(holder.thumbnail);

            holder.title.setText(photoItem.getMemberTitle());
        }
    }

    @Override
    public int getItemCount() {
 //       Log.d(TAG, "getItemCount: Called");
        return ((memberPhotoList!=null) && (memberPhotoList.size() != 0)?memberPhotoList.size():1);
    }

    void loadNewData(List<Photo> newPhoto)
    {
        memberPhotoList = newPhoto;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position)
    {
        return ((memberPhotoList!=null) && (memberPhotoList.size() != 0)?memberPhotoList.get(position):null);
    }


    static class FlickrImageViewHolder extends RecyclerView.ViewHolder
    {
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;
        public FlickrImageViewHolder(View itemView)
        {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: Starts");
            this.thumbnail = (ImageView)itemView.findViewById(R.id.thumbnail);
            this.title = (TextView)itemView.findViewById(R.id.title);
        }
    }
}
