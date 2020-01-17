package com.example.flickrbrowser;

import android.content.Context;
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
        this.memberPhotoList = memberPhotoList;//List containing all the photos with appropriate details
        this.memberContext = memberContext;//called from MainActivity
    }

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //called by layout manager when it needs new view
        //the layout inflater instantiates a layout XML file into its corresponding View objects
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);//creates a view holder by inflating the browse layout
        return new FlickrImageViewHolder(view);//returns the whole object, as in returns the view holder itself
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {
    //called by the layout manager when it needs more data in existing row
        if(memberPhotoList==null || memberPhotoList.size() == 0)
        {
            holder.thumbnail.setImageResource(R.drawable.place_holder);
            //setting the default image in the image holder
            holder.title.setText("No Photos Match of your search, use the search icon to search for photos");
            //setting the default text in the image holder
        }
        else {
            Photo photoItem = memberPhotoList.get(position);//makes a new object of Photo
            Picasso.get().load(photoItem.getMemberImage())//load the image
                    .error(R.drawable.place_holder)//in case of error put the error image
                    .placeholder(R.drawable.place_holder)//by default as well put the error image
                    .into(holder.thumbnail);//put the image into the widget thumbnail

            holder.title.setText(photoItem.getMemberTitle());//setting the text
        }
    }

    @Override
    public int getItemCount() {
        return ((memberPhotoList!=null) && (memberPhotoList.size() != 0)?memberPhotoList.size():1);//returns the number of objects of Photo present in the list memberPhotoList
    }

    void loadNewData(List<Photo> newPhoto)
    {
        memberPhotoList = newPhoto;
        notifyDataSetChanged();
        //Notifies the attached observers that the underlying data
        // has been changed and any View reflecting the data set should refresh itself.
        //https://developer.android.com/reference/android/widget/BaseAdapter#notifyDataSetChanged()
    }

    public Photo getPhoto(int position)
    {
        return ((memberPhotoList!=null) && (memberPhotoList.size() != 0)?memberPhotoList.get(position):null);
    }


    static class FlickrImageViewHolder extends RecyclerView.ViewHolder
    {
        ImageView thumbnail;
        TextView title;
        public FlickrImageViewHolder(View itemView)//constructor for the feed
        {
            super(itemView);
            this.thumbnail = itemView.findViewById(R.id.thumbnail);
            this.title = itemView.findViewById(R.id.title);
        }
    }
}
